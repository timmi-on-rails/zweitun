package com.example.zweitun;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.widget.TintEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.util.Calendar;


public class TimeEditText extends TintEditText {
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

    private void openDialog() {
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        Fragment existingFragment = fragmentManager.findFragmentByTag("timePicker");
        if (existingFragment == null) {
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(R.id.taskTime);
            timePickerFragment.show(fragmentManager, "timePicker");
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener  {
        public static final String TIME_EDIT_TEXT_ID_KEY = "time_edit_text_id_key";

        private TimeEditText timeEditText;

        public static TimePickerFragment newInstance(int timeEditTextId) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            Bundle args = new Bundle();
            args.putInt(TIME_EDIT_TEXT_ID_KEY, timeEditTextId);
            timePickerFragment.setArguments(args);

            return timePickerFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            timeEditText  = (TimeEditText) getActivity().findViewById(getArguments().getInt(TIME_EDIT_TEXT_ID_KEY));
            Calendar calendar = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getActivity()));
            timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.clear), this);

            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getActivity());
            timeEditText.setText(timeFormat.format(calendar.getTime()));
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == TimePickerDialog.BUTTON_NEGATIVE) {
                timeEditText.setText("");
            }
        }
    }
}
