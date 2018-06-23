package com.hush.hassad.ui.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.TournamentResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.predictions.Prediction;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.receiver.AlarmReceiver;
import com.hush.hassad.ui.fragments.profile.ProfileFragment;
import com.hush.hassad.R;
import com.hush.hassad.ui.fragments.SettingsFragment;
import com.hush.hassad.ui.fragments.AboutFragment;
import com.hush.hassad.ui.fragments.FriendsFragment;
import com.hush.hassad.ui.fragments.home.HomeFragment;
import com.hush.hassad.ui.fragments.LeaderboardFragment;
import com.hush.hassad.util.DownloadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.app.FragmentManager fragmentManager = getFragmentManager();
	FirebaseAuth firebaseauth;
    FirebaseUser user;
	TextView profile_name;
	TextView profile_email;
	TextView coins;
	ImageView profile_img;
    FriendsFragment friends_fragment;
    ProfileFragment profile_fragment;
	LeaderboardFragment leaderboard_fragment;
	HomeFragment home_fragment;

    public void initFragments(){
		loadFriendsFragment();
		loadProfileFragment();
		loadLeaderboardFragment();
		loadHomeFragment();
	}

	public void loadLeaderboardFragment(){
    	leaderboard_fragment = new LeaderboardFragment();
    	leaderboard_fragment.setUsers(new ArrayList<User>());
    	leaderboard_fragment.update();
	}

	public void loadFriendsFragment() {
		friends_fragment = new FriendsFragment();
		friends_fragment.update();
	}

	public void loadProfileFragment(){
		profile_fragment = new ProfileFragment();
		profile_fragment.update(Manager.getInstance().getPlayingUser());
	}

	public void loadHomeFragment(){
		home_fragment = new HomeFragment();
	}

	public void submitTournamentPrediction(Intent intent){
    	// Make sure to call this after the loading is completed

		// Also check if prediction is already made

		String gold = intent.getStringExtra("gold");
		String silver = intent.getStringExtra("silver");
		String bronze = intent.getStringExtra("bronze");

		Random rand = new Random();
		int id = rand.nextInt(1000000) + 1;

		TournamentResult result = new TournamentResult(id, Manager.getInstance().getTeamCached(gold), Manager.getInstance().getTeamCached(silver), Manager.getInstance().getTeamCached(bronze));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		loadProfile();

		boolean prediction_made = getIntent().getBooleanExtra("prediction", false);

        //TODO change notification to match timings
		/*AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();


		Intent intent = new Intent(this, AlarmReceiver.class);//"com.hush.hassad.action.DISPLAY_NOTIFICATION");
		PendingIntent broadcast = PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		//alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,broadcast);
		alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

		initFragments();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, home_fragment).commit();
        }

    }
	
	private void loadProfile() {
		firebaseauth = FirebaseAuth.getInstance();
		user = firebaseauth.getCurrentUser();

		if(user != null) {
			NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
			View header = navView.getHeaderView(0);
			profile_name = (TextView) header.findViewById(R.id.profile_name);
			profile_email = (TextView) header.findViewById(R.id.profile_email);
			profile_img = (ImageView) header.findViewById(R.id.profile_img);

			profile_name.setText(user.getDisplayName());
			profile_email.setText(user.getEmail());
			String p = user.getPhotoUrl().toString();
			new DownloadImageTask(profile_img).execute(p);
		}
	}

	@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, home_fragment).commit();
        } else if (id == R.id.nav_friends) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,friends_fragment).commit();
        } else if (id == R.id.nav_leaderboard) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,leaderboard_fragment).commit();
        }  else if (id == R.id.nav_profile) {
        	profile_fragment.update(Manager.getInstance().getPlayingUser());
			fragmentManager.beginTransaction().replace(R.id.content_frame, profile_fragment).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new SettingsFragment()).commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new AboutFragment()).commit();
        }
		else if (id == R.id.nav_signout) {
			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestIdToken(getString(R.string.default_web_client_id))
					.requestEmail()
					.build();

			GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
			mGoogleSignInClient.signOut()
					.addOnCompleteListener(this, new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							FirebaseAuth.getInstance().signOut();
							Intent intent = new Intent(MainActivity.this, SignInActivity.class);
							startActivity(intent);
							finish();

						}
					});
					}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}