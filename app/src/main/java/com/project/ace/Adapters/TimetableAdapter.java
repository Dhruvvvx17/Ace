package com.project.ace.Adapters;

import android.graphics.Color;
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
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Timetable;

public class TimetableAdapter extends FirestoreRecyclerAdapter<Timetable, TimetableAdapter.TimetableHolder> {

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

        if(!professor.isEmpty()){
            holder.lectureProfessor.setText(professor);
            holder.lectureProfessor.setVisibility(View.VISIBLE);
        }

        if(!roomNumber.isEmpty()){
            holder.lectureRoomNumber.setText(roomNumber);
            holder.lectureRoomNumber.setVisibility(View.VISIBLE);
        }

        switch (day){
            case "Monday":
                holder.lectureTimings.setTextColor(Color.parseColor("#003380"));
                holder.timetableCard.setCardBackgroundColor(Color.parseColor("#ccf2ff"));
                break;
            case "Tuesday":
                holder.lectureTimings.setTextColor(Color.parseColor("#00b300"));
                holder.timetableCard.setCardBackgroundColor(Color.parseColor("#ccffcc"));
                break;
            case "Wednesday":
                holder.lectureTimings.setTextColor(Color.parseColor("#990000"));
                holder.timetableCard.setCardBackgroundColor(Color.parseColor("#ffcccc"));
                break;
            case "Thursday":
                holder.lectureTimings.setTextColor(Color.parseColor("#732673"));
                holder.timetableCard.setCardBackgroundColor(Color.parseColor("#ecc6ec"));
                break;
            case "Friday":
                holder.lectureTimings.setTextColor(Color.parseColor("#003380"));
                holder.timetableCard.setCardBackgroundColor(Color.parseColor("#ccf2ff"));
                break;
        }
    }

    @NonNull
    @Override
    public TimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_item,parent,false);
        return new TimetableHolder(v);
    }

    class TimetableHolder extends RecyclerView.ViewHolder{

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
        }
    }
}
