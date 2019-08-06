package com.project.ace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.ace.Dialogs.AttendanceAlertDialog;
import com.project.ace.Dialogs.TimeTableAlertDialog;
import com.project.ace.RecyclerViewItems.Attendance;
import com.project.ace.Adapters.AttendanceAdapter;
import com.project.ace.Dialogs.NewCourseDialog;
import com.project.ace.R;

import static android.content.Context.MODE_PRIVATE;

public class AttendanceFragment extends Fragment  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference AttendanceRef = db.collection("Attendance");
    private AttendanceAdapter adapter;
    View view;
    FloatingActionButton addNewCourse;

    private boolean checkSharedPrefs;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendance,container,false);
        getActivity().setTitle("Attendance");
        addNewCourse = view.findViewById(R.id.add_course_button);
        addNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        if(!checkIfAttendanceExists()){
            AttendanceAlertDialog attendanceAlertDialog = new AttendanceAlertDialog();
            attendanceAlertDialog.show(getFragmentManager(),"Attendance dialog");
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("attendanceSet",true);
            editor.putBoolean("AttendanceCheck",true);
            editor.apply();
        }

        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = AttendanceRef
                .whereEqualTo("userUID",user.getUid())
                .orderBy("courseName", Query.Direction.ASCENDING)
                .whereEqualTo("userUID",user.getUid());

        FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                .setQuery(query, Attendance.class)
                .build();

        adapter = new AttendanceAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.attendance_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }

    private void openDialog(){
        NewCourseDialog newCourseDialog = new NewCourseDialog();
        newCourseDialog.show(getFragmentManager(),"New Course Dialog");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 101:
                adapter.editCourse(item.getGroupId(),getFragmentManager());
                return true;
            case 102:
                adapter.resetCourse(item.getGroupId());
                return true;
            case 103:
                adapter.deleteCourse(item.getGroupId());
                return true;
            default:
                 return super.onContextItemSelected(item);
        }
    }

    private boolean checkIfAttendanceExists(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        checkSharedPrefs = sharedPreferences.getBoolean("attendanceSet",false);
        if(checkSharedPrefs)
            return true;
        else
            return false;
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
