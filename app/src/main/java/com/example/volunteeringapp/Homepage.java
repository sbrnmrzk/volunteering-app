package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    final Fragment homeF = new HomeFragment();
    final Fragment bookmarkedF = new BookmarkedFragment();
    final Fragment notificationsF = new NotificationsFragment();
    final Fragment accountF = new AccountFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //loading the default fragment
        fm.beginTransaction().add(R.id.frameFragment, accountF, "4").hide(accountF).commit();
        fm.beginTransaction().add(R.id.frameFragment, notificationsF, "3").hide(notificationsF).commit();
        fm.beginTransaction().add(R.id.frameFragment, bookmarkedF, "2").hide(bookmarkedF).commit();
        fm.beginTransaction().add(R.id.frameFragment, homeF, "1").commit();

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean navigateToEventDetails(View view){
        Intent eventDetails = new Intent (this, EventDetails.class);
        startActivityForResult(eventDetails, 1);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                fm.beginTransaction().hide(active).show(homeF).commit();
                active = homeF;
                return true;
            case R.id.bookmarkedEvents:
                fm.beginTransaction().hide(active).show(bookmarkedF).commit();
                active = bookmarkedF;
                return true;
            case R.id.notifications:
                fm.beginTransaction().hide(active).show(notificationsF).commit();
                active = notificationsF;
                return true;

            case R.id.account:
                fm.beginTransaction().hide(active).show(accountF).commit();
                active = accountF;
                return true;
        }

        return false;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}