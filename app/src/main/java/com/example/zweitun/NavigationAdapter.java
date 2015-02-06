package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;


public class NavigationAdapter extends ArrayAdapter<String> {
    private List<Long> ids;

    public NavigationAdapter(Context context) {
        super(context, R.layout.navigation_spinner_item);
        setDropDownViewResource(R.layout.navigation_spinner_dropdown_item);

        Cursor cursor = StorageManager.getInstance(context).getLists();
        ids = new ArrayList<>();
        cursor.moveToFirst();
        do {
            ids.add(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.LISTS_COLUMN_ID)));
            add(cursor.getString(cursor.getColumnIndex(DBOpenHelper.LISTS_COLUMN_NAME)));
        } while (cursor.moveToNext());
        cursor.close();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }
}
