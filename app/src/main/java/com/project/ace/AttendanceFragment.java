package com.project.ace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AttendanceFragment extends Fragment  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference AttendanceRef = db.collection("Attendance");
    private AttendanceAdapter adapter;
    View view;
    FloatingActionButton addNewCourse;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendance,container,false);
        addNewCourse = view.findViewById(R.id.add_course_button);
        addNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = AttendanceRef
                .whereEqualTo("userUID",user.getUid());

        FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                .setQuery(query, Attendance.class)
                .build();

        adapter = new AttendanceAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.attendance_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void openDialog(){
        NewCourseDialog newCourseDialog = new NewCourseDialog();
        newCourseDialog.show(getFragmentManager(),"New Course Dialog");
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
