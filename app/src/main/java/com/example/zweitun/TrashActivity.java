package com.example.zweitun;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class TrashActivity extends ListActivity {
    TrashCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* adapter = new TrashCursorAdapter(getActivity(), R.layout.list_item_trash, null, 0);
        setListAdapter(adapter);
        swapCursor();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.alert_discard)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //((MainActivity) this).ssdsdm.emptyTrash();
                                swapCursor();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    public void onRefresh() {
        swapCursor();
        //setRefreshing(false);
    }

    private void swapCursor() {
        //adapter.swapCursor(((MainActivity) getActivity()).ssdsdm.getCompletedTasks());
    }
}
