package com.example.zweitun;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.widget.TintEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.util.Calendar;

public class TimeEditText extends TintEditText implements DialogInterface.OnClickListener {
    private Calendar calendar;

    public TimeEditText(Context context) {
        super(context);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        calendar = Calendar.getInstance();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openDialog();
                }
            }
        });
    }

    private void syncText() {
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        setText(timeFormat.format(calendar.getTime()));
    }

    private void clearTime() {
        calendar = Calendar.getInstance();
        setText("");
    }

    private void openDialog() {
        /*TimePickerDialog dlg = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                syncText();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getContext()));

        dlg.setButton(TimePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.clear), this);
        dlg.show();*/
        DialogFragment newFragment = new TimePickerFragment();

        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        newFragment.show(fragmentManager, "timePicker");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == TimePickerDialog.BUTTON_NEGATIVE) {
            clearTime();
        }
    }

    public Calendar getCalendar() {
        if (!getText().toString().isEmpty()) {
            return calendar;
        }

        return null;
    }

    public void setCalendar(Calendar calendar) {
        if (calendar != null) {
            this.calendar = calendar;
            syncText();
        } else {
            clearTime();
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        public static TimePickerFragment newInstance(int hour, int day) {
            TasksFragment tasksFragment = new TasksFragment();
            Bundle args = new Bundle();
            args.putLong(LIST_ID_KEY, listId);
            tasksFragment.setArguments(args);

            return tasksFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog dlg = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getContext()));

            dlg.setButton(TimePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.clear), this);
            // Create a new instance of TimePickerDialog and return it
            return dlg;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            syncText();
        }
    }
}
