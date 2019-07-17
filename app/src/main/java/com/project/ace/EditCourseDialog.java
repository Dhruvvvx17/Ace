package com.project.ace;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCourseDialog extends AppCompatDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String id, courseName, courseCode, newCourseName, newCourseCode, TAG = "fetch";
    private int target, newTarget;
    boolean check = false;

    private EditText editTextCourseName;
    private EditText editTextCourseCode;
    private SeekBar courseTarget;
    private TextView seekBarProgress;

    EditCourseDialog(String id){
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_course_dialog,null);

        editTextCourseName = (EditText) view.findViewById(R.id.edit_course_name_edit);
        editTextCourseCode = (EditText) view.findViewById(R.id.edit_course_code_edit);
        courseTarget = view.findViewById(R.id.edit_seek_bar_target);
        seekBarProgress = view.findViewById(R.id.edit_text_view_target_value);

        final DocumentReference docRef = db.collection("Attendance").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        courseName = document.getString("courseName");
                        courseCode = document.getString("courseCode");
                        target = document.getLong("target").intValue();

                        //Set initial values
                        editTextCourseName.setText(courseName);
                        editTextCourseCode.setText(courseCode);
                        seekBarProgress.setText(target+"%");
                        courseTarget.setProgress(target);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Edit course")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                    newCourseName = editTextCourseName.getText().toString();
                    newCourseCode = editTextCourseCode.getText().toString();
                    String temp = seekBarProgress.getText().toString();
                    newTarget = Integer.valueOf(temp.substring(0,temp.length()-1));

                    if(!newCourseName.equalsIgnoreCase(courseName) && !newCourseName.isEmpty()){
                        docRef.update("courseName",newCourseName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error updating document", e);
                                    }
                                });
                    }

                    if(!newCourseCode.equalsIgnoreCase(courseCode) && !newCourseCode.isEmpty()){
                        docRef.update("courseCode",newCourseCode)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error updating document", e);
                                    }
                                });
                    }

                    if(newTarget != target){
                        docRef.update("target",newTarget)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error updating document", e);
                                    }
                                });
                    }
                }
            });


        courseTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
