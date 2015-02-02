package com.example.zweitun;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;


public class TasksFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LIST_ID_KEY = "list_id";
    private SwipeRefreshLayout swipeRefreshLayout;
    private long list_id;
    private static TaskCursorAdapter adapter = null;
    private LoaderManager lm;

    public long getListId() {
        return list_id;
    }

    public static TasksFragment newInstance(long list_id) {
        TasksFragment tasksFragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putLong(LIST_ID_KEY, list_id);
        tasksFragment.setArguments(args);
        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = getArguments().getLong(LIST_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_list_view, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (adapter != null) {
            //adapter.getCursor().close();
        }

        adapter = new TaskCursorAdapter(getActivity(), R.layout.list_item_task, null, 0);
        setListAdapter(adapter);


        lm = getLoaderManager();
        lm.initLoader(1, null, this);

        //refreshCursor();

        ((SwipeListView) getListView()).setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    StorageManager.getInstance(getActivity()).completeTask(getListAdapter().getItemId(position));
                    refreshCursor();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        //refreshCursor();
        refreshCursor();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshCursor() {
        /*if (adapter != null) {
            adapter.changeCursor(StorageManager.getInstance(getActivity()).getTasks(list_id));
        }*/
        lm.restartLoader(1, null, TasksFragment.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TasksLoader(getActivity(), list_id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
