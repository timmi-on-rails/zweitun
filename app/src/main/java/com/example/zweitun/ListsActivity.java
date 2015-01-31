package com.example.zweitun;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

public class ListsActivity extends ListActivity {
    //CategoryCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//adapter = new CursorAdapter(getActivity(), R.layout.list_item_category, null, 0);
        //setListAdapter(adapter);
        swapCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lists, menu);
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {


        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        int category_id = cursor.getInt(cursor.getColumnIndex("_id"));



        super.onListItemClick(l, v, position, id);
    }


    private void swapCursor() {
        //adapter.swapCursor(((MainActivity) getActivity()).sssm.getCategories());
    }
}