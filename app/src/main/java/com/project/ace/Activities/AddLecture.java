package com.project.ace.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.ace.Dialogs.TimePickerDialog;
import com.project.ace.R;

import java.text.DateFormat;
import java.util.Calendar;

public class AddLecture extends AppCompatActivity implements android.app.TimePickerDialog.OnTimeSetListener{

    EditText lectureTitle,lectureCode;
    TextView startTime, endTime;
    Button setStartTime,setEndTime,addLecture;

    String startTimeString,endTimeString;
    boolean startTimeCheck;
    int startTimeHour = -1,startTimeMin,endTimeHour = -1,endTimeMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        Intent intent = getIntent();
        String currentDay = intent.getStringExtra("title");
        setTitle(currentDay+"'s Lecture");

        lectureTitle = findViewById(R.id.new_lecture_title);
        lectureCode = findViewById(R.id.new_lecture_code);
        startTime = findViewById(R.id.lecture_start_time_text_view);
        endTime = findViewById(R.id.lecture_end_time_text_view);
        setStartTime = findViewById(R.id.start_time_picker);
        setEndTime = findViewById(R.id.end_time_picker);
        addLecture = findViewById(R.id.add_lecture);

        setStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTimeCheck = true;

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                DialogFragment timePicker = new TimePickerDialog();
                timePicker.show(getSupportFragmentManager(),"start time picker");
            }
        });

        setEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTimeCheck = false;

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                DialogFragment timePicker = new TimePickerDialog();
                timePicker.show(getSupportFragmentManager(),"start time picker");
            }
        });

        addLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add to fire-base appropriate document
                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hours);
        c.set(Calendar.MINUTE,minutes);
        c.set(Calendar.SECOND,0);

        if(startTimeCheck){
            //set all start Time related variables and views.
            if(endTimeHour == -1){
                startTimeHour = hours;
                startTimeMin = minutes;
                startTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                startTime.setText(startTimeString);
            }
            else if(hours > endTimeHour) {
                Toast.makeText(this,"Lecture start time cannot be after its end time",Toast.LENGTH_LONG).show();
            }
            else if(hours == endTimeHour && minutes > endTimeMin){
                Toast.makeText(this,"Lecture end time cannot be before its start time",Toast.LENGTH_LONG).show();
            }
            else{
                startTimeHour = hours;
                startTimeMin = minutes;
                startTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                startTime.setText(startTimeString);
            }
        }


        else{
            //set all end Time related variables and views.
            if(startTimeHour == -1){
                endTimeHour = hours;
                endTimeMin = minutes;
                endTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                endTime.setText(endTimeString);
            }
            else if(hours < startTimeHour){
                Toast.makeText(this,"Lecture end time cannot be before its start time",Toast.LENGTH_LONG).show();
            }
            else if(hours == startTimeHour && minutes < startTimeMin){
                Toast.makeText(this,"Lecture end time cannot be before its start time",Toast.LENGTH_LONG).show();
            }
            else{
                endTimeHour = hours;
                endTimeMin = minutes;
                endTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                endTime.setText(endTimeString);
            }
        }
    }
}
