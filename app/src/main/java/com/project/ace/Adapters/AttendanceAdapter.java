package com.project.ace.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.RecyclerViewItems.Attendance;
import com.project.ace.Dialogs.EditCourseDialog;
import com.project.ace.R;


public class AttendanceAdapter extends FirestoreRecyclerAdapter<Attendance, AttendanceAdapter.AttendanceHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();     //firestore instance

    public AttendanceAdapter(@NonNull FirestoreRecyclerOptions<Attendance> options) {
        super(options);
        Log.d("count", String.valueOf(getItemCount()));
    }

    @Override
    protected void onBindViewHolder(@NonNull final AttendanceHolder holder, int position, @NonNull Attendance model) {
        int classTotal, classAttended, target;
        float attendancePercentage, classRequired;
        final String attendanceFraction, attendancePerc, finalclassRequired;

        classTotal = model.getClassTotal();
        classAttended = model.getClassAttended();

        if (classTotal == 0) {
            holder.textViewCourseName.setText(model.getCourseName());
            holder.textViewCourseCode.setText(model.getCourseCode());
            holder.textViewFraction.setText("0/0");
            holder.textViewPercentage.setText("");
            holder.textViewMessage.setText("");
        } else {
            attendancePercentage = ((float) classAttended / (float) classTotal) * 100;   //percentage - float
            attendancePerc = (String.format("%.2f", attendancePercentage) + "%");     //percentage - string

            attendanceFraction = ((classAttended) + "/" + (classTotal));        //fraction - string

            target = model.getTarget();


            if (target > attendancePercentage) {
                classRequired = (((float) target * classTotal) - (100 * classAttended)) / (100 - target);
                finalclassRequired = Integer.toString((int) Math.ceil((double) classRequired));
                if ((int) Math.ceil((double) classRequired) == 1) {
                    holder.textViewMessage.setText("Attend next class to reach target");
                } else {
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
            }
        });

        holder.missedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedClass(holder.getAdapterPosition());
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

    public void editCourse(int position,FragmentManager manager){
        //delete from firebase
        final String id = getSnapshots().getSnapshot(position).getId();
        EditCourseDialog editCourseDialog = new EditCourseDialog(id);
        editCourseDialog.show(manager,"Edit Course Dialog");
    }

    public void resetCourse(int position){
        final String id = getSnapshots().getSnapshot(position).getId();
        db.collection("Attendance").document(id)
                .update("classAttended",0,"classTotal",0)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Update",id+"updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Update",id+"updated");
                    }
                });
    }

    public void deleteCourse(int position){
        //delete from firebase
        final String id = getSnapshots().getSnapshot(position).getId();
        db.collection("Attendance").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("delete",id+"deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("delete",id+"could not be deleted");
                    }
                });
    }

    class AttendanceHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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

            imageButton.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.add(this.getAdapterPosition(),101,0,"Edit course");
            contextMenu.add(this.getAdapterPosition(),102,1,"Reset classes");
            contextMenu.add(this.getAdapterPosition(),103,2,"Delete course");
        }
    }
}
