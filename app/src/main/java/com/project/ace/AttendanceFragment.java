package com.project.ace;

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
import android.widget.Toast;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

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
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 101:
                adapter.editCourse(item.getGroupId(),getActivity(),getFragmentManager());
                return true;
            case 102:
                adapter.deleteCourse(item.getGroupId());
                return true;
             default:
                 return super.onContextItemSelected(item);
        }
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
