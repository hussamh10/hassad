package com.hush.hassad.ui.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hush.hassad.ui.fragments.ProfileFragment;
import com.hush.hassad.R;
import com.hush.hassad.ui.fragments.SettingsFragment;
import com.hush.hassad.ui.fragments.TableFragment;
import com.hush.hassad.ui.fragments.AboutFragment;
import com.hush.hassad.ui.fragments.FriendsFragment;
import com.hush.hassad.ui.fragments.HomeFragment;
import com.hush.hassad.ui.fragments.LeaderboardFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.app.FragmentManager fragmentManager = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            fragmentManager.beginTransaction().replace(R.id.content_frame,new HomeFragment()).commit();
        } else if (id == R.id.nav_friends) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new FriendsFragment()).commit();
        } else if (id == R.id.nav_table) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new TableFragment()).commit();
        } else if (id == R.id.nav_leaderboard) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new LeaderboardFragment()).commit();
        }  else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new ProfileFragment()).commit();
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