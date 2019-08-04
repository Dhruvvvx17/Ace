package com.project.ace.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ace.Fragments.AttendanceFragment;
import com.project.ace.Fragments.FilesFragment;
import com.project.ace.Fragments.RemindersFragment;
import com.project.ace.Fragments.TimetableFragment;
import com.project.ace.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;


    String userName;
    String userEmail;
    String userProfileURI;

    TextView userNameTextview;
    TextView userEmailTextview;
    ImageView userProfileImage;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

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

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent launchNextActivity = new Intent(MainActivity.this,SignUpActivity.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(launchNextActivity);
                }
            }
        };

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("timeTableSet",false);
        editor.apply();

        String received = getIntent().getStringExtra("notificationClick");
        if(received != null && mAuth.getCurrentUser()!=null){
            switch (received){
                case "openReminders":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RemindersFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_reminders);
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AttendanceFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_attendance);
                    break;
            }
        }
        else if(received != null && mAuth.getCurrentUser() == null){
            startActivity(new Intent(this,SignUpActivity.class));
        }
        else if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AttendanceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_attendance);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userName = user.getDisplayName();
            userEmail = user.getEmail();
            userProfileURI = user.getPhotoUrl().toString();


            navigationView = findViewById(R.id.nav_view);
            View v = navigationView.getHeaderView(0);

            userNameTextview = v.findViewById(R.id.nav_header_name);
            userEmailTextview = v.findViewById(R.id.nav_header_email);
            userProfileImage = v.findViewById(R.id.nav_header_profile);

            userNameTextview.setText(userName);
            userEmailTextview.setText(userEmail);
            Picasso.get().load(userProfileURI).into(userProfileImage);
        }
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });

        Intent launchNextActivity = new Intent(MainActivity.this,SignUpActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
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
            case R.id.nav_sign_out:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Wait for 1.5 seconds
                        mAuth.signOut();
                    }
                }, 1000);
                break;
            case R.id.nav_switch_account:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Wait for 1.5 seconds
                        signOut();
                    }
                }, 1000);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            finish(); // finish activity
        } else {
            super.onBackPressed();
        }
    }
}
