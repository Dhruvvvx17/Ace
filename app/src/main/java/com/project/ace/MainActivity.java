package com.project.ace;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AttendanceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_attendance);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_attendance:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AttendanceFragment()).commit();
                break;
            case R.id.nav_reminders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RemindersFragment()).commit();
                break;
            case R.id.nav_timetable:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TimetableFragment()).commit();
                break;
            case R.id.nav_files:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FilesFragment()).commit();
                break;
            case R.id.nav_temp1:
                Toast.makeText(this,"Work in progress",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_temp2:
                Toast.makeText(this,"Work in progress",Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();

        }
    }
}
