package com.project.ace.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.R;

import java.util.HashMap;
import java.util.Map;

public class NewCourseDialog extends AppCompatDialogFragment {
    private static final String TAG = "add course";
    private EditText editTextCourseName;
    private EditText editTextCourseCode;
    private SeekBar CourseTarget;
    private TextView seekBarProgress , enterDetailsMsg;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String courseName;
    String courseCode;
    int target;
    int classAttended = 0,classTotal = 0;
    String userUID;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_course_dialog,null);
        builder.setView(view)
                .setTitle("Add new course")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        courseName = editTextCourseName.getText().toString();
                        courseCode = editTextCourseCode.getText().toString();
                        String temp = seekBarProgress.getText().toString();
                        target = Integer.valueOf(temp.substring(0,temp.length()-1));


                        if(courseName.isEmpty() || target==0 ){
                            enterDetailsMsg.setVisibility(View.VISIBLE);
                        }
                        else{
                            userUID = user.getUid();
                            Map<String, Object> course = new HashMap<>();
                            course.put("courseName", courseName);
                            course.put("courseCode", courseCode);
                            course.put("target", target);
                            course.put("classTotal", classTotal);
                            course.put("classAttended", classAttended);
                            course.put("userUID",userUID);

                            db.collection("Attendance")
                                    .document()
                                    .set(course)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            enterDetailsMsg.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        editTextCourseName = view.findViewById(R.id.new_course_name_edit);
        editTextCourseCode = view.findViewById(R.id.new_course_code_edit);
        CourseTarget = view.findViewById(R.id.seek_bar_target);
        seekBarProgress = view.findViewById(R.id.text_view_target_value);
        enterDetailsMsg = view.findViewById(R.id.text_view_enter_details);

        seekBarProgress.setText(CourseTarget.getProgress()+"%");

        CourseTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return builder.create();
    }
}
