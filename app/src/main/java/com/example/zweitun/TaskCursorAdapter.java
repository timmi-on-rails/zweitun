package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TaskCursorAdapter extends ResourceCursorAdapter {
    public TaskCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        view.setOnTouchListener(new OnSwipeTouchListener(view.getContext(), position) {
        @Override
        public void onSwipeRight(int position) {
            Log.d("ba", "id: " + position);
        }
        });

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task_name = (TextView) view.findViewById(R.id.task_name);
        TextView category_name = (TextView) view.findViewById(R.id.category_name);
        TextView task_due_at = (TextView) view.findViewById(R.id.task_due_at);

        task_name.setText(cursor.getString(cursor.getColumnIndex("task_name")));
        task_name.setTextColor(context.getResources().getIntArray(R.array.priority_colors)[cursor.getInt(cursor.getColumnIndex("task_priority"))]);
        category_name.setText(cursor.getString(cursor.getColumnIndex("category_name")));
        task_due_at.setText(cursor.getString(cursor.getColumnIndex("task_due_at")));
    }
}
