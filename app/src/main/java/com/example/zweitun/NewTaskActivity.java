package com.example.zweitun;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;


public class NewTaskActivity extends ActionBarActivity {
    public static final String TASK_NAME = "task_name";
    public static final String MAX_PRIORITY = "max_priority";
    public static final String DUE_AT = "due_at";
    public static final String TIME_SCALE = "time_scale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        PriorityAdapter adapter = new PriorityAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(2);

        Spinner ts = (Spinner) findViewById(R.id.time_scale);
        ArrayAdapter<String> tsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.time_scales));
        tsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ts.setAdapter(tsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new_task_accept:
                Intent data = new Intent();
                data.putExtra(TASK_NAME, ((EditText) findViewById(R.id.taskName)).getText().toString());

                data.putExtra(MAX_PRIORITY, ((Spinner) findViewById(R.id.spinner)).getSelectedItemPosition());

                /*DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day_of_month = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                data.putExtra(DUE_AT, String.format("%04d-%02d-%02d %02d:%02d", year, month, day_of_month, hour, minute));
*/
                data.putExtra(TIME_SCALE, ((Spinner) findViewById(R.id.time_scale)).getSelectedItemPosition());

                setResult(RESULT_OK, data);
                finish();
                return true;

            case android.R.id.home:
                onBackPressed();// or finish()??
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
