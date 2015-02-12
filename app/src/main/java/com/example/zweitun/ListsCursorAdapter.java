package com.example.zweitun;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;


public class ListsCursorAdapter extends SimpleCursorAdapter {
    private Context context;

    public ListsCursorAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, null, new String[] { "name" }, new int[] { android.R.id.text1 }, 0);
        this.context = context;

        reload();
    }

    public void reload() {
        changeCursor(StorageManager.getInstance(mContext).getLists());
    }
}
