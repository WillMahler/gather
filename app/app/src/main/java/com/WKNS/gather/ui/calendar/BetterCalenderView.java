package com.WKNS.gather.ui.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class BetterCalenderView extends CalendarView {
    private Calendar calendar;
    private int currentMonth;

    public BetterCalenderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        calendar = Calendar.getInstance();
       // Log.d("nick ", "" + calendar.);
    }

    public void test(){

    }
}
