package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;


public class TaskCursorAdapter extends ResourceCursorAdapter {
    public TaskCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task_name = (TextView) view.findViewById(R.id.task_name);
        TextView task_due_at = (TextView) view.findViewById(R.id.task_due_at);

        task_name.setText(cursor.getString(cursor.getColumnIndex("name")));
        task_name.setTextColor(context.getResources().getIntArray(R.array.priority_colors)[cursor.getInt(cursor.getColumnIndex("priority"))]);

        task_due_at.setText(cursor.getString(cursor.getColumnIndex("due_at")));
    }
}
