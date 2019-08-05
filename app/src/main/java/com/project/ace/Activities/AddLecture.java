package com.project.ace.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Dialogs.TimePickerDialog;
import com.project.ace.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddLecture extends AppCompatActivity implements android.app.TimePickerDialog.OnTimeSetListener{

    EditText lectureTitle,lectureCode,lectureProfessor,lectureRoomNo;
    TextView startTime, endTime;
    Button setStartTime,setEndTime,addLecture;

    String startTimeString="",endTimeString="",userUID,lectureTitleString,lectureCodeString,lectureProfessorString,lectureRoomNoString;
    boolean startTimeCheck;
    int startTimeHour = -1,startTimeMin,endTimeHour = -1,endTimeMin;
    int checkStart;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        Intent intent = getIntent();
        final String currentDay = intent.getStringExtra("title");
        setTitle(currentDay+"'s Lecture");

        lectureTitle = findViewById(R.id.new_lecture_title);
        lectureCode = findViewById(R.id.new_lecture_code);
        startTime = findViewById(R.id.lecture_start_time_text_view);
        endTime = findViewById(R.id.lecture_end_time_text_view);
        setStartTime = findViewById(R.id.start_time_picker);
        setEndTime = findViewById(R.id.end_time_picker);
        addLecture = findViewById(R.id.add_lecture);
        lectureProfessor = findViewById(R.id.new_lecture_professor);
        lectureRoomNo = findViewById(R.id.new_lecture_room_number);

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

                userUID = user.getUid();
                lectureTitleString = lectureTitle.getText().toString().trim();
                lectureCodeString = lectureCode.getText().toString().trim();
                if(lectureTitleString.isEmpty())
                    Toast.makeText(getApplicationContext(),"Enter a title",Toast.LENGTH_SHORT).show();
                else if(startTimeString.isEmpty())
                    Toast.makeText(getApplicationContext(),"Set a valid lecture start time",Toast.LENGTH_SHORT).show();
                else if(endTimeString.isEmpty())
                    Toast.makeText(getApplicationContext(),"Set a valid lecture end time",Toast.LENGTH_SHORT).show();
                else {
                    //Everything is valid, upload data to fire-base. Set alarm.

                    Random rand = new Random();
                    int lectureNotificationID = rand.nextInt((20000-10000)+1)+10000;

                    lectureProfessorString = lectureProfessor.getText().toString().trim();
                    if(lectureProfessorString.isEmpty())
                        lectureProfessorString = "";
                    lectureRoomNoString = lectureRoomNo.getText().toString().trim();
                    if(lectureRoomNoString.isEmpty())
                        lectureRoomNoString = "";
                    if(lectureCodeString.isEmpty())
                        lectureCodeString = "";

                    Map<String, Object> lecture = new HashMap<>();
                    lecture.put("lectureTitle", lectureTitleString);
                    lecture.put("lectureCode",lectureCodeString);
                    lecture.put("lectureStartTime",startTimeString);
                    lecture.put("lectureEndTime",endTimeString);
                    lecture.put("lectureProfessor",lectureProfessorString);
                    lecture.put("lectureRoom",lectureRoomNoString);
                    lecture.put("lectureNotificationID",lectureNotificationID);
                    lecture.put("checkStart",checkStart);
                    lecture.put("day",currentDay);
                    lecture.put("userUID",userUID);
                    db.collection("Timetable")
                            .document()
                            .set(lecture)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("new_lecture", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("new_lecture", "Error writing document", e);
                                }
                            });

                    finish();
                }
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
                checkStart = hours;
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
                checkStart = hours;
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
