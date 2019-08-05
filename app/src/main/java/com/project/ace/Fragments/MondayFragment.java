package com.project.ace.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.ace.Adapters.TimetableAdapter;
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Timetable;

public class MondayFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference TimetableRef = db.collection("Timetable");
    private TimetableAdapter adapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.monday_fragment,container,false);
        getActivity().setTitle("Timetable - Monday");
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView(){
        Query query = TimetableRef.whereEqualTo("userUID",user.getUid())
                .whereEqualTo("day","Monday")
                .orderBy("checkStart", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Timetable> options = new FirestoreRecyclerOptions.Builder<Timetable>()
                .setQuery(query,Timetable.class)
                .build();

        adapter = new TimetableAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.monday_timetable_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
