package com.example.zweitun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.zweitun.ui.PriorityAdapter;


public class NewTaskActivity extends ActionBarActivity {
    public static final String TASK_NAME = "task_name";
    public static final String PRIORITY = "priority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        PriorityAdapter adapter = new PriorityAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(2);
    }

    public void selfDestruct(View view) {
        Intent data = new Intent();
        data.putExtra(TASK_NAME, ((EditText) findViewById(R.id.editText)).getText().toString());
        data.putExtra(PRIORITY, ((Spinner) findViewById(R.id.spinner)).getSelectedItemPosition());

        setResult(RESULT_OK, data);
        finish();
    }
}
