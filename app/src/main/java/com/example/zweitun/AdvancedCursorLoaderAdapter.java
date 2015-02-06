package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ResourceCursorAdapter;
import java.util.ArrayList;


abstract public class AdvancedCursorLoaderAdapter extends ResourceCursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {
    protected Context context;
    private LoaderManager loaderManager;
    private int loaderId;
    private ArrayList<Long> expandedIds = new ArrayList<>();

    public AdvancedCursorLoaderAdapter(Context context, int layout, LoaderManager loaderManager, int loaderId) {
        super(context, layout, null, 0);
        this.context = context;
        this.loaderId = loaderId;
        this.loaderManager = loaderManager;
    }

    public void reload() {
        loaderManager.restartLoader(loaderId, getLoaderArgs(), this);
    }

    abstract public Bundle getLoaderArgs();

    public boolean idExpanded(long id) {
        return expandedIds.contains(id);
    }

    public void toggleId(long id) {
        if (expandedIds.contains(id)) {
            expandedIds.remove(id);
        } else {
            expandedIds.add(id);
        }

        notifyDataSetChanged();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }
}
