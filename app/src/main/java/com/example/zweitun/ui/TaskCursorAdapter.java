package com.example.zweitun.ui;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

import android.widget.TextView;

import com.example.zweitun.R;

public class TaskCursorAdapter extends ResourceCursorAdapter {
    public TaskCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.continent);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));

        //TextView phone = (TextView) view.findViewById(R.id.phone);
        //phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
    }

}
