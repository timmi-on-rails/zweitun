package com.example.zweitun;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

/**
 * Fragment handles display/edit of a task list
 */
public class TasksFragment extends ListFragment {
    public static final String LIST_ID_KEY = "list_id";

    private long listId;
    private TaskCursorAdapter adapter;
    private DynamicListView dynamicListView;

    public static TasksFragment newInstance(long listId) {
        TasksFragment tasksFragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putLong(LIST_ID_KEY, listId);
        tasksFragment.setArguments(args);

        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listId = getArguments().getLong(LIST_ID_KEY);

        adapter = new TaskCursorAdapter(getActivity(), listId, getLoaderManager(), 0);
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
        dynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.toggleId(id);
            }
        });

        dynamicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        dynamicListView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            StorageManager.getInstance(getActivity()).completeTask(getListView().getItemIdAtPosition(position));
                        }

                        reload();
                    }
                }
        );
    }

    public void reload() {
        adapter.reload();
    }

    public long getListId() {
        return listId;
    }
}
