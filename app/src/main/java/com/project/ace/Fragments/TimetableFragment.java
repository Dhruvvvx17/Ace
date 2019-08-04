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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Dialogs.NewCourseDialog;
import com.project.ace.Dialogs.TimeTableDialog;
import com.project.ace.R;

import static android.content.Context.MODE_PRIVATE;

public class TimetableFragment extends Fragment {

    private View view;
    private boolean checkFirestore,checkSharedPrefs;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference TimetableRef = db.collection("Timetable");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable,container,false);
        getActivity().setTitle("Timetable");

        BottomNavigationView bottomNav = view.findViewById(R.id.timetable_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(!checkIfTimeTableExists()){
            TimeTableDialog timeTableDialog = new TimeTableDialog();
            timeTableDialog.show(getFragmentManager(),"Add time table dialog");
        }
        else {
            getChildFragmentManager().beginTransaction().replace(R.id.timetable_fragment_container,new MondayFragment()).commit();

        }
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
                            break;
                        case R.id.tuesday_item:
                            selectedFragment = new TuesdayFragment();
                            break;
                        case R.id.wednesday_item:
                            selectedFragment = new WednesdayFragment();
                            break;
                        case R.id.thursday_item:
                            selectedFragment = new ThursdayFragment();
                            break;
                        case R.id.friday_item:
                            selectedFragment = new FridayFragment();
                            break;
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.timetable_fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    private boolean checkIfTimeTableExists(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        checkSharedPrefs = sharedPreferences.getBoolean("timeTableSet",false);

        if(checkSharedPrefs)
            return true;

        else{
            TimetableRef.document(user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists()){
                                    checkFirestore = false;
                                } else {
                                    checkFirestore = true;
                                }
                            }
                            else {
                                Log.d("check","Failed with: ",task.getException());
                            }
                        }
                    });

        }

        if(!checkFirestore && !checkSharedPrefs)
            return false;   //The timetable has not been set, show dialog
        else
            return true;
    }
}
