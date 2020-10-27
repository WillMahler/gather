package com.WKNS.gather.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.WKNS.gather.CreateEventActivity;
import com.WKNS.gather.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private View root;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}