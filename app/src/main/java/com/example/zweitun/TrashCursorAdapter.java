package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

// getContext() what is the usual way?
// abstract method with body?!
// activity and action different strings

public class TrashCursorAdapter extends AdvancedCursorLoaderAdapter {
    public TrashCursorAdapter(Context context, LoaderManager loaderManager, int loaderId) {
        super(context, R.layout.list_item_trash, loaderManager, loaderId);

        reload();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task_name = (TextView) view.findViewById(R.id.task_name);
        //TextView category_name = (TextView) view.findViewById(R.id.category_name);
        //TextView task_due_at = (TextView) view.findViewById(R.id.task_due_at);

        task_name.setText(cursor.getString(cursor.getColumnIndex("name")));
        //task_name.setTextColor(context.getResources().getIntArray(R.array.priority_colors)[cursor.getInt(cursor.getColumnIndex("task_priority"))]);
//        category_name.setText(cursor.getString(cursor.getColumnIndex("category_name")));
  //      task_due_at.setText(cursor.getString(cursor.getColumnIndex("task_due_at")));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TrashLoader(context);
    }

    @Override
    public Bundle getLoaderArgs() {
        return null;
    }
}
