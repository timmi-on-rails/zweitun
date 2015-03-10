package com.example.zweitun;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.internal.widget.TintEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import java.text.DateFormat;
import java.util.Calendar;


public class DateEditText extends TintEditText implements DialogInterface.OnClickListener {
    private Calendar calendar;

    public DateEditText(Context context) {
        super(context);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        setText(dateFormat.format(calendar.getTime()));
    }

    private void clearDate() {
        calendar = Calendar.getInstance();
        setText("");
    }

    private void openDialog() {
        DatePickerDialog dlg = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                syncText();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dlg.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.clear), this);
        dlg.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DatePickerDialog.BUTTON_NEGATIVE) {
            clearDate();
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
            clearDate();
        }
    }
}
