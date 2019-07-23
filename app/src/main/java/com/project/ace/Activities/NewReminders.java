package com.project.ace.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.project.ace.Notifications.AlertReciever;
import com.project.ace.Notifications.NotificationHelper;
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
    int nYear,nMonth,nDay,nHours,nMinutes;

//    private NotificationHelper mNotif;

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

//        mNotif = new NotificationHelper(this);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

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

//        setDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NotificationCompat.Builder nb = mNotif.getChannel2Notification("Hi","This is also working");
//                mNotif.getManager().notify(2,nb.build());
//            }
//        });
//
//        setTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NotificationCompat.Builder nb = mNotif.getChannel1Notification("Hi","This is working");
//                mNotif.getManager().notify(1,nb.build());
//            }
//        });


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
                    reminder.put("reminderTitle", title);
                    reminder.put("reminderDescription",description);
                    reminder.put("reminderDate",date);
                    reminder.put("reminderTime",time);
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

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY,nHours);
                    c.set(Calendar.MINUTE,nMinutes);
                    c.set(Calendar.SECOND,0);
                    c.set(Calendar.YEAR,nYear);
                    c.set(Calendar.MONTH,nMonth);
                    c.set(Calendar.DAY_OF_MONTH,nDay);

                    startAlarm(c);

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
            nDay = dayOfMonth;
            nMonth = month;
            nYear = year;
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

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hours);
        c.set(Calendar.MINUTE,minutes);
        c.set(Calendar.SECOND,0);

        nMinutes = minutes;
        nHours = hours;

        time = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        reminderTime.setText(time);
        reminderTime.setVisibility(View.VISIBLE);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReciever.class);
        intent.putExtra("Title",reminderTitle.getText().toString());
        intent.putExtra("Description",reminderDescription.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
