package com.example.zweitun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class TrashActivity extends ActionBarActivity {
    private TrashFragment trashFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trashFragment = (TrashFragment) getSupportFragmentManager().findFragmentByTag("trash_fragment");
        if (trashFragment == null) {
            trashFragment = new TrashFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, trashFragment, "trash_fragment")
                    .commit();
        }
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
                                StorageManager.getInstance(TrashActivity.this).emptyTrash();
                                trashFragment.reload();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class TrashFragment extends ListFragment {
        private TrashCursorAdapter adapter;

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            adapter = new TrashCursorAdapter(getActivity(), getLoaderManager(), 0);
            setListAdapter(adapter);
        }

        public void reload() {
            adapter.reload();
        }

        public void onListItemClick(ListView listView, View view, int position, long id) {

        }
    }
}
