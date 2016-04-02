package com.github.dkharrat.nexusdialog.controllers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.github.dkharrat.nexusdialog.FormController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimePickerController extends LabeledFieldController {
    private final int editTextId = FormController.generateViewId();

    private TimePickerDialog timePickerDialog = null;
    private final SimpleDateFormat displayFormat;
    private final TimeZone timeZone;
    private final boolean is24HourView;

    /**
     * Constructs a new instance of a time picker field.
     *
     * @param ctx               the Android context
     * @param name              the name of the field
     * @param labelText         the label to display beside the field. Set to {@code null} to not show a label.
     * @param isRequired        indicates if the field is required or not
     * @param displayFormat     the format of the time to show in the text box when a time is set
     * @param is24HourView      the format of time picker dialog should be 24 hour format or not
     */
    public TimePickerController(Context ctx, String name, String labelText, boolean isRequired, SimpleDateFormat displayFormat, boolean is24HourView) {
        super(ctx, name, labelText, isRequired);
        this.displayFormat = displayFormat;
        this.timeZone = displayFormat.getTimeZone();
        this.is24HourView = is24HourView;
    }

    /**
     * Constructs a new instance of a time picker field, with the selected time displayed in "HH:mm" format.
     *
     * @param name              the name of the field
     * @param labelText         the label to display beside the field
     */
    public TimePickerController(Context context, String name, String labelText) {
        this(context, name, labelText, false, new SimpleDateFormat("hh:mm a", Locale.getDefault()), false);
    }

    @Override
    protected View createFieldView() {
        final EditText editText = new EditText(getContext());
        editText.setId(editTextId);

        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME);
        editText.setKeyListener(null);
        refresh(editText);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(getContext(), editText);
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog(getContext(), editText);
                }
            }
        });

        return editText;
    }

    private void showTimePickerDialog(Context context, final EditText editText) {
        // don't show dialog again if it's already being shown
        if (timePickerDialog == null) {
            Date date = (Date)getModel().getValue(getName());
            if (date == null) {
                date = new Date();
            }
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeZone(timeZone);
            calendar.setTime(date);

            timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeZone(timeZone);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    getModel().setValue(getName(), calendar.getTime());
                    editText.setText(displayFormat.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HourView);

            timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    timePickerDialog = null;
                }
            });

            timePickerDialog.show();
        }
    }

    private EditText getEditText() {
        return (EditText)getView().findViewById(editTextId);
    }

    private void refresh(EditText editText) {
        Date value = (Date)getModel().getValue(getName());
        editText.setText(value != null ? displayFormat.format(value) : "");
    }

    @Override
    public void refresh() {
        refresh(getEditText());
    }
}
