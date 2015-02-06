package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Adapter for tasks from a specified list retrieved with TasksLoader
 */
public class TaskCursorAdapter extends AdvancedCursorLoaderAdapter {
    private long listId;

    public TaskCursorAdapter(Context context, long listId, LoaderManager loaderManager, int loaderId) {
        super(context, R.layout.list_item_task, loaderManager, loaderId);
        this.listId = listId;

        reload();
    }

    @Override
    public Bundle getLoaderArgs() {
        Bundle args = new Bundle();
        args.putLong(TasksFragment.LIST_ID_KEY, listId);
        return args;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task_name = (TextView) view.findViewById(R.id.task_name);
        TextView task_due_at = (TextView) view.findViewById(R.id.task_due_at);
        TextView test = (TextView) view.findViewById(R.id.task_test);
        test.setText("test: " + cursor.getInt(cursor.getColumnIndex("_id")));

        task_name.setText(cursor.getString(cursor.getColumnIndex("name")));
        task_name.setTextColor(context.getResources().getIntArray(R.array.priority_colors)[cursor.getInt(cursor.getColumnIndex("priority"))]);
        task_due_at.setText(cursor.getString(cursor.getColumnIndex("due_at")));

        TextView textView = (TextView) view.findViewById(R.id.task_test);

        if (idExpanded(cursor.getLong(cursor.getColumnIndex("_id")))) {
            textView.setVisibility(View.VISIBLE);

        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TasksLoader(context, args);
    }
}
