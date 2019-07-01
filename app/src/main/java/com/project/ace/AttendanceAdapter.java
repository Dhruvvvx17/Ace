package com.project.ace;

import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AttendanceAdapter extends FirestoreRecyclerAdapter<Attendance, AttendanceAdapter.AttendanceHolder> {


    public AttendanceAdapter(@NonNull FirestoreRecyclerOptions<Attendance> options) {
        super(options);
        Log.d("Hi",String.valueOf(getItemCount()));
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceHolder holder, int position, @NonNull Attendance model) {
        int classTotal,classAttended;
        float attendancePercentage,target,classRequired;
        String attendanceFraction,attendancePerc,finalclassRequired;

        classTotal = model.getClassTotal();
        classAttended = model.getClassAttended();
        attendancePercentage = ((float)classAttended/(float)classTotal)*100;                            //percentage - float
        attendancePerc = (Float.toString(attendancePercentage)+"%");                                    //percentage - string
        attendanceFraction = (Integer.toString(classAttended)+"/"+Integer.toString(classTotal));        //fraction - string
        target = model.getTarget();
        if(target > attendancePercentage){
            classRequired = (float) ((target*classTotal) - (100*classAttended)) /(float) (100 - target);
            finalclassRequired = Integer.toString((int)Math.ceil((double)classRequired));
            holder.textViewMessage.setText("Attend next "+finalclassRequired+" classes to reach target");
        }
        else{
            holder.textViewMessage.setText("Attendance above target");
        }

        holder.textViewCourseName.setText(model.getCourseName());
        holder.textViewCourseCode.setText(model.getCourseCode());
        holder.textViewFraction.setText(attendanceFraction);
        holder.textViewPercentage.setText(attendancePerc);
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_item,
                viewGroup,false);
        return new AttendanceHolder(v);
    }

    class AttendanceHolder extends RecyclerView.ViewHolder{

        TextView textViewCourseName;
        TextView textViewCourseCode;
        TextView textViewMessage;
        TextView textViewPercentage;
        TextView textViewFraction;
        Button attendedButton;
        Button missedButton;

        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseName = itemView.findViewById(R.id.course_name);
            textViewCourseCode = itemView.findViewById(R.id.course_code);
            textViewMessage = itemView.findViewById(R.id.attendance_message);
            textViewPercentage = itemView.findViewById(R.id.course_attendance_percentage);
            textViewFraction = itemView.findViewById(R.id.course_attendance_fraction);
            attendedButton = itemView.findViewById(R.id.add_one_class);
            missedButton = itemView.findViewById(R.id.missed_one_class);
        }
    }
}
