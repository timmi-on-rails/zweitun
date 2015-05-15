package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class ListsCursorAdapter extends AdvancedCursorLoaderAdapter {
    public ListsCursorAdapter(Context context, int layout, int dropdown_layout, LoaderManager loaderManager, int loaderId) {
        super(context, layout, loaderManager, loaderId);
        setDropDownViewResource(dropdown_layout);

        reload();
    }

    public ListsCursorAdapter(Context context, LoaderManager loaderManager, int loaderId) {
        super(context, android.R.layout.simple_list_item_1, loaderManager, loaderId);

        reload();
    }

    @Override
    public Bundle getLoaderArgs() {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(android.R.id.text1);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ListsLoader(context);
    }

    public int getPosition(long id) {
        for (int position=0; position<getCount(); position++)
            if (getItemId(position) == id)
                return position;
        return -1;
    }
}
