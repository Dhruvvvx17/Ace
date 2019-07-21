package com.project.ace.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ace.Activities.NewReminders;
import com.project.ace.R;

public class RemindersFragment extends Fragment {

    View view;
    FloatingActionButton addNewReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reminders,container,false);
        addNewReminder = view.findViewById(R.id.add_reminder_button);
        addNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewReminder();
            }
        });

        return view;
    }

    public void setNewReminder(){
        Intent i = new Intent(getActivity(), NewReminders.class);
        startActivity(i);
    }
}
