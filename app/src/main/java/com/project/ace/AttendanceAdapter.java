package com.project.ace;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class AttendanceAdapter extends FirestoreRecyclerAdapter<Attendance, AttendanceAdapter.AttendanceHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();     //firestore instance

    public AttendanceAdapter(@NonNull FirestoreRecyclerOptions<Attendance> options) {
        super(options);
        Log.d("count", String.valueOf(getItemCount()));
    }

    @Override
    protected void onBindViewHolder(@NonNull final AttendanceHolder holder, int position, @NonNull Attendance model) {
        int classTotal, classAttended;
        float attendancePercentage, target, classRequired;
        final String attendanceFraction, attendancePerc, finalclassRequired;

        classTotal = model.getClassTotal();
        classAttended = model.getClassAttended();

        if (classTotal == 0) {
            holder.textViewCourseName.setText(model.getCourseName());
            holder.textViewCourseCode.setText(model.getCourseCode());
            holder.textViewFraction.setText("0/0");
            holder.textViewPercentage.setText("");
            holder.textViewMessage.setText("");
        }
        else{
            attendancePercentage = ((float) classAttended / (float) classTotal) * 100;   //percentage - float
            attendancePerc = (String.format("%.2f", attendancePercentage) + "%");     //percentage - string

            attendanceFraction = ((classAttended) + "/" + (classTotal));        //fraction - string

            target = model.getTarget();


            if (target > attendancePercentage) {
                classRequired = ((target * classTotal) - (100 * classAttended)) / (100 - target);
                finalclassRequired = Integer.toString((int) Math.ceil((double) classRequired));
                if((int)Math.ceil((double)classRequired) == 1){
                    holder.textViewMessage.setText("Attend next class to reach target");
                }
                else{
                    holder.textViewMessage.setText("Attend next " + finalclassRequired + " classes to reach target");
                }
            } else {
                holder.textViewMessage.setText("Attendance above target");
            }

            holder.textViewCourseName.setText(model.getCourseName());
            holder.textViewCourseCode.setText(model.getCourseCode());
            holder.textViewFraction.setText(attendanceFraction);
            holder.textViewPercentage.setText(attendancePerc);

        }

        holder.attendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendedClass(holder.getAdapterPosition());
                Toast.makeText(holder.attendedButton.getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });

        holder.missedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedClass(holder.getAdapterPosition());
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dialog box
                //with options for editing course details and deleting course
            }
        });
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_item,
                viewGroup, false);
        return new AttendanceHolder(v);
    }

    public void attendedClass(int position) {

        String id = getSnapshots().getSnapshot(position).getId();
        final DocumentReference docRef = db.collection("Attendance").document(id);

        docRef.update("classAttended", FieldValue.increment(1) , "classTotal",FieldValue.increment(1));
    }

    public void missedClass(int position) {

        String id = getSnapshots().getSnapshot(position).getId();
        final DocumentReference docRef = db.collection("Attendance").document(id);

        docRef.update("classTotal", FieldValue.increment(1));
    }

    class AttendanceHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseName;
        TextView textViewCourseCode;
        TextView textViewMessage;
        TextView textViewPercentage;
        TextView textViewFraction;
        Button attendedButton;
        Button missedButton;
        ImageButton imageButton;

        public AttendanceHolder(@NonNull final View itemView) {
            super(itemView);
            textViewCourseName = itemView.findViewById(R.id.course_name);
            textViewCourseCode = itemView.findViewById(R.id.course_code);
            textViewMessage = itemView.findViewById(R.id.attendance_message);
            textViewPercentage = itemView.findViewById(R.id.course_attendance_percentage);
            textViewFraction = itemView.findViewById(R.id.course_attendance_fraction);

            attendedButton = itemView.findViewById(R.id.attendedClass);
            missedButton = itemView.findViewById(R.id.missedClass);
            imageButton = itemView.findViewById(R.id.more_options);
        }
    }
}
