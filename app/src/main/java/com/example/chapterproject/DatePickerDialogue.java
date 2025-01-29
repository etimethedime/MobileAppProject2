package com.example.chapterproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogue extends DialogFragment {
    Calendar selectedDate;

    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedDate);
    }

    public DatePickerDialogue() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_date_layout, container);

        if (getDialog() != null) {
            getDialog().setTitle("Select Date");
        }

        selectedDate = Calendar.getInstance();
        CalendarView calendarView = view.findViewById(R.id.calendarView);


        calendarView.setOnDateChangeListener((calendar, year, month, day) -> selectedDate.set(year, month, day));

        Button saveButton = view.findViewById(R.id.saveDateButton);
        saveButton.setOnClickListener(v -> saveItem(selectedDate));

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void saveItem(Calendar selectedDate) {
        SaveDateListener activity = (SaveDateListener) getActivity();
        if (activity != null) {
            activity.didFinishDatePickerDialog(selectedDate);
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        }
    }
}
