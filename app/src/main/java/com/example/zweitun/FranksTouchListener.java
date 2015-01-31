package com.example.zweitun;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class FranksTouchListener implements View.OnTouchListener {
    private int position;

    public FranksTouchListener(int position) {
        this.position = position;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ba", "id: " + position);
                return true;
        }
        return false;
    }
}
