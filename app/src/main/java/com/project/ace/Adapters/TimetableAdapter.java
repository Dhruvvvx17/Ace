package com.project.ace.Adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Timetable;

public class TimetableAdapter extends FirestoreRecyclerAdapter<Timetable, TimetableAdapter.TimetableHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();     //firestore instance

    public TimetableAdapter(@NonNull FirestoreRecyclerOptions<Timetable> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TimetableHolder holder, int position, @NonNull Timetable model) {
        String timing = model.getLectureStartTime() + " - " + model.getLectureEndTime();
        String day = model.getDay();
        String professor = model.getLectureProfessor();
        String lectureCode = model.getLectureCode();
        String roomNumber = model.getLectureRoom();

        holder.lectureTimings.setText(timing);
        holder.lectureTitle.setText(model.getLectureTitle());
        holder.lectureCourseCode.setText(lectureCode);
        holder.lectureCourseCode.setVisibility(View.VISIBLE);

        holder.lectureProfessor.setText(professor);
        holder.lectureProfessor.setVisibility(View.VISIBLE);

        holder.lectureRoomNumber.setText(roomNumber);
        holder.lectureRoomNumber.setVisibility(View.VISIBLE);

        switch (day){
            case "Monday":
                holder.lectureTimings.setTextColor(Color.parseColor("#003380"));            //blue
                holder.timetableCard.setBackgroundResource(R.drawable.monday_item_background);
                break;
            case "Tuesday":
                holder.lectureTimings.setTextColor(Color.parseColor("#008000"));            //green
                holder.timetableCard.setBackgroundResource(R.drawable.tuesday_item_background);
                break;
            case "Wednesday":
                holder.lectureTimings.setTextColor(Color.parseColor("#990000"));            //red
                holder.timetableCard.setBackgroundResource(R.drawable.wednesday_item_background);
                break;
            case "Thursday":
                holder.lectureTimings.setTextColor(Color.parseColor("#732673"));            //purple
                holder.timetableCard.setBackgroundResource(R.drawable.thursday_item_background);
                break;
            case "Friday":
                holder.lectureTimings.setTextColor(Color.parseColor("#ff6600"));            //orange
                holder.timetableCard.setBackgroundResource(R.drawable.friday_item_background);
                break;
        }
    }

    public void deleteLecture(int position){
        //delete from firebase
        final String id = getSnapshots().getSnapshot(position).getId();
        db.collection("Timetable").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("delete",id+" deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("delete",id+" could not be deleted");
                    }
                });
    }

    @NonNull
    @Override
    public TimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_item,parent,false);
        return new TimetableHolder(v);
    }

    class TimetableHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView lectureTimings;
        TextView lectureTitle;
        TextView lectureCourseCode;
        TextView lectureProfessor;
        TextView lectureRoomNumber;
        ImageButton lectureMoreOptions;

        CardView timetableCard;

        public TimetableHolder(@NonNull View itemView) {
            super(itemView);

            lectureTimings = itemView.findViewById(R.id.lecture_start_and_end_time);
            lectureTitle = itemView.findViewById(R.id.lecture_course_title);
            lectureCourseCode = itemView.findViewById(R.id.lecture_course_code);
            lectureProfessor= itemView.findViewById(R.id.lecture_course_professor);
            lectureRoomNumber = itemView.findViewById(R.id.lecture_room_number);
            lectureMoreOptions = itemView.findViewById(R.id.lecture_more_options);
            timetableCard = itemView.findViewById(R.id.timetable_item);

            lectureMoreOptions.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.add(this.getAdapterPosition(),201,0,"Delete lecture");
        }
    }
}
