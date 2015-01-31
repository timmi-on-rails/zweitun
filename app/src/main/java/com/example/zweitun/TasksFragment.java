package com.example.zweitun;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;


public class TasksFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String LIST_ID_KEY = "list_id";
    private SwipeRefreshLayout swipeRefreshLayout;
    private long list_id;
    private MainActivity mainActivity;

    TaskCursorAdapter adapter;

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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        adapter = new TaskCursorAdapter(mainActivity, R.layout.list_item_task, null, 0);
        setListAdapter(adapter);
        refreshCursor();

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

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
                    mainActivity.storageManager.completeTask(adapter.getItemId(position));
                    refreshCursor();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshCursor();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshCursor() {
        adapter.changeCursor(mainActivity.storageManager.getTasks(list_id));
    }
}
