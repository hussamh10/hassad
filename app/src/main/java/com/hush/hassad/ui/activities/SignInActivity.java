package com.hush.hassad.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.dal.DAL;

public class SignInActivity extends AppCompatActivity {

	private static final boolean AUTO_HIDE = true;

	private GoogleSignInClient mGoogleSignInClient;
	private FirebaseAuth mAuth;
	private SignInButton btn_signin;
	private boolean exists;

	private static final int NEW_USER = 0;
	private static final int OLD_USER = 1;


	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final int UI_ANIMATION_DELAY = 300;
	private final Handler mHideHandler = new Handler();
	private View mContentView;
	private final Runnable mHidePart2Runnable = new Runnable() {
		@SuppressLint("InlinedApi")
		@Override
		public void run() {
			// Delayed removal of status and navigation bar

			// Note that some of these constants are new as of API 16 (Jelly Bean)
			// and API 19 (KitKat). It is safe to use them, as they are inlined
			// at compile-time and do nothing on earlier devices.
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	};
	private View mControlsView;
	private final Runnable mShowPart2Runnable = new Runnable() {
		@Override
		public void run() {
			// Delayed display of UI elements
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.show();
			}
			mControlsView.setVisibility(View.VISIBLE);
		}
	};
	private boolean mVisible;
	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hide();
		}
	};
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		FirebaseUser user = mAuth.getCurrentUser();
		update(user, OLD_USER);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		if (!isNetworkAvailable()){
			Intent intent = new Intent(SignInActivity.this, NoConnectionActivity.class);
			startActivity(intent);
			finish();
		}

		mAuth = FirebaseAuth.getInstance();

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		btn_signin = (SignInButton) findViewById(R.id.btn_signin);
		btn_signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signIn();
			}
		});
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				Toast.makeText(this, "Success 1", Toast.LENGTH_SHORT).show();
				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				Toast.makeText(this, "Failed 1", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}	
	
	public void signIn(){
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, 1);
	}

	public void update(FirebaseUser acc, int type){
		if (acc != null){
			if(type == NEW_USER)
			{
				FirebaseAuth m = FirebaseAuth.getInstance();
				FirebaseUser userAcc = m.getCurrentUser();
				User user = DAL.getInstance().createUser(userAcc);

				Manager.getInstance().setPlayingUser(user);

				Intent intent = new Intent(SignInActivity.this, TournamentPredictionActivity.class);
				startActivity(intent);
				finish();
			}
			else {
				Intent intent = new Intent(SignInActivity.this, MainActivity.class);
				intent.putExtra("prediction", false);
				DAL.getInstance().setPlayingUser(acc.getUid());
				startActivity(intent);
				finish();
			}
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		int a =0;
		AuthCredential credential = null;
		try{
			Toast.makeText(this, "Success 2", Toast.LENGTH_SHORT).show();
			credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		}
		catch (Exception e){
			Toast.makeText(this, "Failed 2", Toast.LENGTH_SHORT).show();
			System.out.print(e.getMessage());
			e.printStackTrace();
		}

		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							FirebaseUser user = mAuth.getCurrentUser();
							checkExistence(user.getUid());
						} else {
							// If sign in fails, display a message to the user.
							update(null, NEW_USER);
						}
						// ...
					}
				});
	}

	public void checkExistence(String uid) {

		FirebaseFirestore db = FirebaseFirestore.getInstance();
		Query query = db.collection("users").whereEqualTo("id", uid);

		query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				FirebaseUser user = mAuth.getCurrentUser();
				if(queryDocumentSnapshots.isEmpty()){
					//The user exists...
					update(user, NEW_USER);
				}
				else {
					//The user doesn't exist...
					update(user, OLD_USER);
				}
			}
		});
		/*final String id = uid;
		DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		DatabaseReference userNameRef = rootRef.child("users");
		ValueEventListener eventListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				FirebaseUser user = mAuth.getCurrentUser();
				for(DataSnapshot data: dataSnapshot.getChildren()){
					if (data.child(id).exists()) {
						update(user, OLD_USER);
					} else {
						update(user, NEW_USER);
					}
				}*/
				/*if(dataSnapshot.exists()){
					update(user, OLD_USER);
				} else {
					update(user, NEW_USER);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		};
		userNameRef.addListenerForSingleValueEvent(eventListener);

		/*FirebaseDatabase.getInstance().getReference(uid).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				FirebaseUser user = mAuth.getCurrentUser();
				if(dataSnapshot.exists()){
					update(user, NEW_USER);
				} else {
					update(user, OLD_USER);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});*/
	}

/*	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}*/

	private void hide() {
		// Hide UI first
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
		mControlsView.setVisibility(View.GONE);
		mVisible = false;

		// Schedule a runnable to remove the status and navigation bar after a delay
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
	}

	@SuppressLint("InlinedApi")
	private void show() {
		// Show the system bar
		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		mVisible = true;

		// Schedule a runnable to display UI elements after a delay
		mHideHandler.removeCallbacks(mHidePart2Runnable);
		mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
	}

	/**
	 * Schedules a call to hide() in delay milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
