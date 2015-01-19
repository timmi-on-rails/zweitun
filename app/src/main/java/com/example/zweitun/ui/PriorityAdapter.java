package com.example.zweitun.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.zweitun.R;


/*
    PriorityAdapter - adapter for the priority selection spinner
 */
public class PriorityAdapter extends ArrayAdapter<CharSequence> {
    public PriorityAdapter(Context context) {
        super(context, R.layout.spinner_priority_item, context.getResources().getStringArray(R.array.priorities));
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setBackgroundColor(parent.getResources().getIntArray(R.array.priority_colors)[position]);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        view.setBackgroundColor(parent.getResources().getIntArray(R.array.priority_colors)[position]);
        return view;
    }
}
