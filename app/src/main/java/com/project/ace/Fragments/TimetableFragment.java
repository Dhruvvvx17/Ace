package com.project.ace.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Dialogs.AddNewLectureDialog;
import com.project.ace.Dialogs.NewCourseDialog;
import com.project.ace.Dialogs.TimeTableDialog;
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
            TimeTableDialog timeTableDialog = new TimeTableDialog();
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
                AddNewLectureDialog mondayDialog = new AddNewLectureDialog(currentDay);
                mondayDialog.show(getFragmentManager(),"Add monday lecture");
                Toast.makeText(getContext(),"It's Monday",Toast.LENGTH_SHORT).show();
                break;
            case "Tuesday":
                AddNewLectureDialog tuesdayDialog = new AddNewLectureDialog(currentDay);
                tuesdayDialog.show(getFragmentManager(),"Add monday lecture");
                Toast.makeText(getContext(),"It's Tuesday",Toast.LENGTH_SHORT).show();
                break;
            case "Wednesday":
                AddNewLectureDialog wednesdayDialog = new AddNewLectureDialog(currentDay);
                wednesdayDialog.show(getFragmentManager(),"Add monday lecture");
                Toast.makeText(getContext(),"It's Wednesday",Toast.LENGTH_SHORT).show();
                break;
            case "Thursday":
                AddNewLectureDialog thursdayDialog = new AddNewLectureDialog(currentDay);
                thursdayDialog.show(getFragmentManager(),"Add monday lecture");
                Toast.makeText(getContext(),"It's Thursday",Toast.LENGTH_SHORT).show();
                break;
            case "Friday":
                AddNewLectureDialog fridayDialog = new AddNewLectureDialog(currentDay);
                fridayDialog.show(getFragmentManager(),"Add monday lecture");
                Toast.makeText(getContext(),"It's Friday",Toast.LENGTH_SHORT).show();
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
