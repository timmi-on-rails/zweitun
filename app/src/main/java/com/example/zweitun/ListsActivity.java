package com.example.zweitun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;


public class ListsActivity extends ActionBarActivity {
    private ListsFragment listsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listsFragment = (ListsFragment) getSupportFragmentManager().findFragmentByTag("lists_fragment");
        if (listsFragment == null) {
            listsFragment = new ListsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, listsFragment, "lists_fragment")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_new_list:
                final EditText input = new EditText(this);

                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setMessage(R.string.alert_new_list)
                        .setView(input)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                StorageManager.getInstance(ListsActivity.this).createList(input.getText().toString());
                                listsFragment.reload();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ListsFragment extends ListFragment {
        private ListsCursorAdapter adapter;
        private DynamicListView dynamicListView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            adapter = new ListsCursorAdapter(getActivity(), getLoaderManager(), 0);
            setListAdapter(adapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            dynamicListView = new DynamicListView(getActivity());
            dynamicListView.setId(android.R.id.list);

            return dynamicListView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            dynamicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                    final EditText input = new EditText(getActivity());
                    input.setText(StorageManager.getInstance(getActivity()).getListName(id));

                    new AlertDialog.Builder(getActivity())
                            .setCancelable(true)
                            .setMessage(R.string.alert_edit_list)
                            .setView(input)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    StorageManager.getInstance(getActivity()).renameList(id, input.getText().toString());
                                    reload();
                                }
                            }).setNegativeButton(R.string.cancel, null).show();
                    return true;
                }
            });

            dynamicListView.enableSwipeToDismiss(
                    new OnDismissCallback() {
                        @Override
                        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                            for (final int position : reverseSortedPositions) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage(R.string.alert_delete_list)
                                        .setCancelable(true)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                long count = StorageManager.getInstance(getActivity()).getListCount(adapter.getItemId(position));

                                                if (count == 0) {
                                                    StorageManager.getInstance(getActivity()).deleteList(adapter.getItemId(position));
                                                    reload();
                                                } else {
                                                    new AlertDialog.Builder(getActivity())
                                                            .setMessage(R.string.alert_delete_list_not_empty)
                                                            .setCancelable(true)
                                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    StorageManager.getInstance(getActivity()).deleteList(adapter.getItemId(position));
                                                                    reload();
                                                                }
                                                            })
                                                            .setNegativeButton(R.string.no, null)
                                                            .show();
                                                }
                                            }
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .show();
                            }
                        }
                    }
            );
        }

        public void reload() {
            adapter.reload();
        }
    }
}