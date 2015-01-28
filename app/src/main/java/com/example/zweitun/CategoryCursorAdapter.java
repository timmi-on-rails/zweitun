package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class CategoryCursorAdapter extends ResourceCursorAdapter {
    public CategoryCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CheckBox category_checked = (CheckBox) view.findViewById(R.id.category_checked);
        TextView category_text = (TextView) view.findViewById(R.id.category_text);

        boolean checked = (cursor.getInt(cursor.getColumnIndex("visible")) == 1);
        String text = cursor.getString(cursor.getColumnIndex("name"));

        category_checked.setChecked(checked);
        category_text.setText(text);
    }
}
