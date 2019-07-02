package com.project.ace;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewCourseDialog extends AppCompatDialogFragment {
    private EditText editTextCourseName;
    private EditText editTextCourseCode;
    private SeekBar CourseTarget;
    private TextView seekBarProgress;

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

                    }
                });

        editTextCourseName = view.findViewById(R.id.new_course_name_edit);
        editTextCourseCode = view.findViewById(R.id.new_course_code_edit);
        CourseTarget = view.findViewById(R.id.seek_bar_target);
        seekBarProgress = view.findViewById(R.id.text_view_target_value);

        seekBarProgress.setText(CourseTarget.getProgress()+"%");

        CourseTarget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
