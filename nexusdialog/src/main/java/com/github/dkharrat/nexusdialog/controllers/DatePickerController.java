package com.github.dkharrat.nexusdialog.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Represents a field that allows selecting a specific date via a date picker.
 * <p/>
 * For the field value, the associated FormModel must return a {@link Date} instance. No selected date can be
 * represented by returning {@code null} for the value of the field.
 */
public class DatePickerController extends LabeledFieldController {
    private final static int EDIT_TEXT_ID = 1001;

    private DatePickerDialog datePickerDialog = null;
    private final SimpleDateFormat displayFormat;

    /**
     * Constructs a new instance of a date picker field.
     *
     * @param ctx               the Android context
     * @param name              the name of the field
     * @param labelText         the label to display beside the field. Set to {@code null} to not show a label.
     * @param isRequired        indicates if the field is required or not
     * @param displayFormat     the format of the date to show in the text box when a date is set
     */
    public DatePickerController(Context ctx, String name, String labelText, boolean isRequired, SimpleDateFormat displayFormat) {
        super(ctx, name, labelText, isRequired);
        this.displayFormat = displayFormat;
    }

    /**
     * Constructs a new instance of a date picker field, with the selected date displayed in "MMM d, yyyy" format.
     *
     * @param name              the name of the field
     * @param labelText         the label to display beside the field
     */
    public DatePickerController(Context context, String name, String labelText) {
        this(context, name, labelText, false, new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()));
    }

    @Override
    protected View createFieldView() {
        final EditText editText = new EditText(getContext());
        editText.setId(EDIT_TEXT_ID);

        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME);
        editText.setKeyListener(null);
        refresh(editText);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(getContext(), editText);
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(getContext(), editText);
                }
            }
        });

        return editText;
    }

    private void showDatePickerDialog(final Context context, final EditText editText) {
        // don't show dialog again if it's already being shown
        if (datePickerDialog == null) {
            Date date = (Date)getModel().getValue(getName());
            if (date == null) {
                date = new Date();
            }
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(date);

            datePickerDialog = new DatePickerDialog(context, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.set(year, monthOfYear, dayOfMonth);
                    getModel().setValue(getName(), calendar.getTime());
                    editText.setText(displayFormat.format(calendar.getTime()));

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    datePickerDialog = null;
                }
            });

            datePickerDialog.show();
        }
    }

    private EditText getEditText() {
        return (EditText)getView().findViewById(EDIT_TEXT_ID);
    }

    private void refresh(EditText editText) {
        Date value = (Date)getModel().getValue(getName());
        editText.setText(value != null ? displayFormat.format(value) : "");
    }

    public void refresh() {
        refresh(getEditText());
    }
}