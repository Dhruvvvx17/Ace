package com.project.ace.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Dialogs.TimePickerDialog;
import com.project.ace.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewReminders extends AppCompatActivity implements android.app.TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "add reminder";
    EditText reminderTitle, reminderDescription;
    TextView reminderTime, reminderDate;
    Button setTime,setDate,setReminder;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userUID,title,description,time,date;
    int currentDay,check1,check2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminders);

        Calendar c = Calendar.getInstance();
        currentDay = c.get(Calendar.DAY_OF_MONTH);
        check1 = 0;
        check2 = 0;

        reminderTitle = findViewById(R.id.new_reminder_title);
        reminderDescription = findViewById(R.id.new_reminder_description);
        reminderTime = findViewById(R.id.time_text_view);
        reminderDate = findViewById(R.id.date_text_view);
        setTime = findViewById(R.id.time_picker);
        setDate = findViewById(R.id.date_picker);
        setReminder = findViewById(R.id.set_reminder);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new com.project.ace.Dialogs.DatePickerDialog();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerDialog();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });


        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUID = user.getUid();
                title = reminderTitle.getText().toString().trim();
                description = reminderDescription.getText().toString().trim();
                time = reminderTime.getText().toString();
                date = reminderDate.getText().toString();
                if(title.isEmpty())
                    Toast.makeText(getApplicationContext(),"Enter a title",Toast.LENGTH_SHORT).show();
                else if(date.isEmpty())
                    Toast.makeText(getApplicationContext(),"Set a valid date",Toast.LENGTH_SHORT).show();
                else if(time.isEmpty())
                    Toast.makeText(getApplicationContext(),"Set a valid time",Toast.LENGTH_SHORT).show();
                else{
                    //Everything is valid, upload data to fire-base. Set alarm.

                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("title", title);
                    reminder.put("description",description);
                    reminder.put("date",date);
                    reminder.put("time",time);
                    reminder.put("userUID",userUID);
                    db.collection("Reminder")
                            .document()
                            .set(reminder)
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
                    finish();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        int currentDate = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.LONG).format(c.getTime());
        if(check2==1 && currentDate==dayOfMonth)
            Toast.makeText(getApplicationContext(), "Cannot set reminder in the past.Enter a valid time", Toast.LENGTH_LONG).show();
        else{
            reminderDate.setText(currentDateString);
            reminderDate.setVisibility(View.VISIBLE);
            if(currentDay == dayOfMonth)
                check1 = 1;
            else
                check1 = 0;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        if(check1==1) {
            if (currentHour > hours)
                Toast.makeText(getApplicationContext(), "Cannot set reminder in the past.Enter a valid time", Toast.LENGTH_LONG).show();
            else if (currentHour == hours && currentMinute > minutes)
                Toast.makeText(getApplicationContext(), "Cannot set reminder in the past.Enter a valid time", Toast.LENGTH_LONG).show();
            else{
                setTime(minutes,hours);
            }
        }
        else{
            if (currentHour > hours)
                check2 = 1;
            else if (currentHour == hours && currentMinute > minutes)
                check2 = 1;
            else
                check2 = 0;
            setTime(minutes,hours);
        }
    }

    public void setTime(int minutes,int hours){
        String mins,hrs,AMorPM = "AM";
        mins = Integer.toString(minutes);
        hrs = Integer.toString(hours);
        switch (hrs){
            case "13":  hrs = "1";   AMorPM = "PM";  break;
            case "14":  hrs = "2";   AMorPM = "PM";  break;
            case "15":  hrs = "3";   AMorPM = "PM";  break;
            case "16":  hrs = "4";   AMorPM = "PM";  break;
            case "17":  hrs = "5";   AMorPM = "PM";  break;
            case "18":  hrs = "6";   AMorPM = "PM";  break;
            case "19":  hrs = "7";   AMorPM = "PM";  break;
            case "20":  hrs = "8";   AMorPM = "PM";  break;
            case "21":  hrs = "9";   AMorPM = "PM";  break;
            case "22":  hrs = "10";  AMorPM = "PM";  break;
            case "23":  hrs = "11";  AMorPM = "PM";  break;
            case "24":  hrs = "12";  AMorPM = "PM";  break;
        }
        if(mins.length() == 1)
            mins = "0"+mins;
        if(hrs.length() == 1)
            hrs = "0"+hrs;

        String time = hrs+":"+mins+" "+AMorPM;
        reminderTime.setText(time);
        reminderTime.setVisibility(View.VISIBLE);
    }
}
