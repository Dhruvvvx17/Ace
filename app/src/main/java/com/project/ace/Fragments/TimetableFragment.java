package com.project.ace.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Activities.AddLecture;
import com.project.ace.Dialogs.TimeTableAlertDialog;
import com.project.ace.R;

import static android.content.Context.MODE_PRIVATE;

public class TimetableFragment extends Fragment {

    private View view;
    private boolean checkSharedPrefs;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference TimetableRef = db.collection("Timetable");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private String navDay;
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable,container,false);
        getActivity().setTitle("Timetable");

        floatingActionButton = view.findViewById(R.id.add_timetable_details_button);
        BottomNavigationView bottomNav = view.findViewById(R.id.timetable_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(!checkIfTimeTableExists()){
            TimeTableAlertDialog timeTableDialog = new TimeTableAlertDialog();
            timeTableDialog.show(getFragmentManager(),"Timetable dialog");
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("timeTableSet",true);
            editor.putBoolean("TTValidCheck",true);
            editor.apply();
        }

        getChildFragmentManager().beginTransaction().replace(R.id.timetable_fragment_container,new MondayFragment()).commit();
        navDay = "Monday";

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLectureDetails(navDay);
            }
        });

        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.monday_item:
                            selectedFragment = new MondayFragment();
                            navDay = "Monday";
                            break;
                        case R.id.tuesday_item:
                            selectedFragment = new TuesdayFragment();
                            navDay = "Tuesday";
                            break;
                        case R.id.wednesday_item:
                            selectedFragment = new WednesdayFragment();
                            navDay = "Wednesday";
                            break;
                        case R.id.thursday_item:
                            selectedFragment = new ThursdayFragment();
                            navDay = "Thursday";
                            break;
                        case R.id.friday_item:
                            selectedFragment = new FridayFragment();
                            navDay = "Friday";
                            break;
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.timetable_fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    private void addLectureDetails(String currentDay){
        switch(currentDay){
            case "Monday":
                Intent i1 = new Intent(getContext(), AddLecture.class);
                i1.putExtra("title",currentDay);
                getContext().startActivity(i1);
                break;
            case "Tuesday":
                Intent i2 = new Intent(getContext(), AddLecture.class);
                i2.putExtra("title",currentDay);
                getContext().startActivity(i2);
                break;
            case "Wednesday":
                Intent i3= new Intent(getContext(), AddLecture.class);
                i3.putExtra("title",currentDay);
                getContext().startActivity(i3);
                break;
            case "Thursday":
                Intent i4 = new Intent(getContext(), AddLecture.class);
                i4.putExtra("title",currentDay);
                getContext().startActivity(i4);
                 break;
            case "Friday":
                Intent i5 = new Intent(getContext(), AddLecture.class);
                i5.putExtra("title",currentDay);
                getContext().startActivity(i5);
                break;
        }
    }

    private boolean checkIfTimeTableExists(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        checkSharedPrefs = sharedPreferences.getBoolean("timeTableSet",false);
        if(checkSharedPrefs)
            return true;
        else
            return false;
    }
}
