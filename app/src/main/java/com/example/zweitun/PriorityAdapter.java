package com.example.zweitun;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/*
    PriorityAdapter - adapter for the priority selection spinner
 */
public class PriorityAdapter extends ArrayAdapter<CharSequence> {
    public PriorityAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.priorities));
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextColor(parent.getResources().getIntArray(R.array.priority_colors)[position]);
            textView.setTextSize(25);
            textView.setGravity(Gravity.CENTER);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            //textView.setTextSize(25);
        }

        return view;
    }
}
