package com.project.ace.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.ace.Activities.NewReminders;
import com.project.ace.Adapters.AttendanceAdapter;
import com.project.ace.Adapters.ReminderAdapter;
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Attendance;
import com.project.ace.RecyclerViewItems.Reminder;

public class RemindersFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ReminderRef = db.collection("Reminder");
    private ReminderAdapter adapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    View view;
    FloatingActionButton addNewReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reminders,container,false);
        getActivity().setTitle("Reminders");
        addNewReminder = view.findViewById(R.id.add_reminder_button);
        addNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewReminder();
            }
        });
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView(){
        Query query = ReminderRef
                .whereEqualTo("userUID",user.getUid());

        FirestoreRecyclerOptions<Reminder> options = new FirestoreRecyclerOptions.Builder<Reminder>()
                .setQuery(query, Reminder.class)
                .build();

        adapter = new ReminderAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.reminder_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }

    private void setNewReminder(){
        Intent i = new Intent(getActivity(), NewReminders.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
