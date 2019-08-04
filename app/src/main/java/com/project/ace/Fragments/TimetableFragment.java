package com.project.ace.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.ace.R;

public class TimetableFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable,container,false);
        getActivity().setTitle("Timetable");

        BottomNavigationView bottomNav = view.findViewById(R.id.timetable_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        getChildFragmentManager().beginTransaction().replace(R.id.timetable_fragment_container,new MondayFragment()).commit();
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
}
