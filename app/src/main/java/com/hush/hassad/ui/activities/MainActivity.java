package com.hush.hassad.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.receiver.AlarmReceiver;
import com.hush.hassad.ui.fragments.profile.ProfileFragment;
import com.hush.hassad.R;
import com.hush.hassad.ui.fragments.SettingsFragment;
import com.hush.hassad.ui.fragments.AboutFragment;
import com.hush.hassad.ui.fragments.FriendsFragment;
import com.hush.hassad.ui.fragments.home.HomeFragment;
import com.hush.hassad.ui.fragments.LeaderboardFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.app.FragmentManager fragmentManager = getFragmentManager();

    FriendsFragment friends_fragment;
    ProfileFragment profile_fragment;
	LeaderboardFragment leaderboard_fragment;

    private void initUser(String id){
    	DAL.getInstance().getUser(id);
		initFragments();
        //Manager.getInstance().setPlayingUser(u);
		//TODO
        //return u;
    }

    public void initFragments(){
		loadFriendsFragment();
		loadProfileFragment();
		loadLeaderboardFragment();
	}
	public void loadLeaderboardFragment(){
    	leaderboard_fragment = new LeaderboardFragment();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO change notification to match timings
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,5);

		Intent intent = new Intent(this, AlarmReceiver.class);//"com.hush.hassad.action.DISPLAY_NOTIFICATION");
		PendingIntent broadcast = PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);

        DAL.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int type = getIntent().getIntExtra("type", 1);

		FirebaseAuth m = FirebaseAuth.getInstance();
		FirebaseUser acc = m.getCurrentUser();


		DAL.getInstance().createUser(acc);

		initUser(acc.getUid().toString());

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new HomeFragment() ).commit();
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
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        } else if (id == R.id.nav_friends) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,friends_fragment).commit();
        } else if (id == R.id.nav_leaderboard) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new LeaderboardFragment()).commit();
        }  else if (id == R.id.nav_profile) {
			fragmentManager.beginTransaction().replace(R.id.content_frame, profile_fragment).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new SettingsFragment()).commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new AboutFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}