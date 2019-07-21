package com.project.ace.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        android.app.TimePickerDialog.OnTimeSetListener timeSetListener =  (android.app.TimePickerDialog.OnTimeSetListener) getActivity();
        return new android.app.TimePickerDialog(getContext(),timeSetListener,hour,min, DateFormat.is24HourFormat(getActivity()));
    }
}
