package com.github.dkharrat.nexusdialog.controllers;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.*;
import com.github.dkharrat.nexusdialog.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a field that displays a value.
 * <p/>
 * For the field value, the associated FormModel can return a string or any object. The value's {@code toString} method
 * will be used to display the value.
 */
public class ValueController extends LabeledFieldController {

    /**
     * Constructs a new instance of a value field.
     *
     * @param ctx               the Android context
     * @param name              the name of the field
     * @param labelText         the label to display beside the field. Set to {@code null} to not show a label.
     */
    public ValueController(Context ctx, String name, String labelText) {
        super(ctx, name, labelText, false);
    }

    @Override
    protected View constructFieldView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final TextView textView = (TextView)layoutInflater.inflate(R.layout.value_field, null);
        refresh(textView);

        return textView;
    }

    private TextView getTextView() {
        return (TextView)getView().findViewById(R.id.value_text);
    }

    private void refresh(TextView textView) {
        Object value = getModel().getValue(getName());
        textView.setText(value != null ? value.toString() : "");
    }

    public void refresh() {
        refresh(getTextView());
    }
}