package com.github.dkharrat.nexusdialog.controllers;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.dkharrat.nexusdialog.R;

/**
 * Represents a field that allows a user to select from a list of items.
 * <p/>
 * For the field value, the associated FormModel must return either a String or a 0-based index, representing the
 * currently selected item. Which representation to use is specified by the constructor. In either representation, no
 * selection can be represented by returning {@code null} for the value of the field.
 */
public class SelectionController extends LabeledFieldController {

    private final static int SPINNER_ID = 1001;

    private final String prompt;
    private final List<String> items;
    private final List<?> values;

    /**
     * Constructs a selection field
     *
     * @param ctx                   the Android context
     * @param name                  the name of the field
     * @param labelText             the label to display beside the field. Set to {@code null} to not show a label.
     * @param isRequired            indicates if the field is required or not
     * @param prompt                if nothing is selected, 'prompt' is displayed
     * @param items                 a list of Strings defining the selection items to show
     * @param useItemsAsValues      if true, {@code SelectionController} expects the associated form model to use
     *                              the same string of the selected item when getting or setting the field; otherwise,
     *                              {@code SelectionController} expects the form model to use index (as an Integer) to
     *                              represent the selected item
     */
    public SelectionController(Context ctx, String name, String labelText, boolean isRequired, String prompt, List<String> items, boolean useItemsAsValues) {
        this(ctx, name, labelText, isRequired, prompt, items, useItemsAsValues ? items : null);
    }

    /**
     * Constructs a selection field
     *
     * @param ctx                   the Android context
     * @param name                  the name of the field
     * @param labelText             the label to display beside the field
     * @param isRequired            indicates if the field is required or not
     * @param prompt                if nothing is selected, 'prompt' is displayed
     * @param items                 a list of Strings defining the selection items to show
     * @param values                a list of Objects representing the values to set the form model on a selection (in
     *                              the same order as the {@code items}.
     */
    public SelectionController(Context ctx, String name, String labelText, boolean isRequired, String prompt, List<String> items, List<?> values) {
        super(ctx, name, labelText, isRequired);
        this.prompt = prompt;
        this.items = items;
        this.values = values;
    }

    /**
     * Returns the Spinner view associated with this element.
     *
     * @return the Spinner view associated with this element
     */
    public Spinner getSpinner() {
        return (Spinner)getView().findViewById(SPINNER_ID);
    }

    @Override
    protected View constructFieldView() {
        Spinner spinnerView = new Spinner(getContext());
        spinnerView.setId(SPINNER_ID);
        spinnerView.setPrompt(prompt);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerView.setAdapter(new NothingSelectedSpinnerAdapter(spinnerAdapter, R.layout.nothing_selected, getContext()));
        spinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object value;
                // if no values are specified, set the index on the model
                if (values == null) {
                    value = pos;
                } else {
                    // pos of 0 indicates nothing is selected
                    if (pos == 0) {
                        value = null;
                    } else {    // if something is selected, set the value on the model
                        value = values.get(pos-1);
                    }
                }

                getModel().setValue(getName(), value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        refresh(spinnerView);

        return spinnerView;
    }

    private void refresh(Spinner spinner) {
        Object value = getModel().getValue(getName());
        int selectionIndex = 0;

        if (values != null) {
            for (int i=0; i< values.size(); i++) {
                if (values.get(i).equals(value)) {
                    selectionIndex = i+1;
                    break;
                }
            }
        } else if (value instanceof Integer) {
            selectionIndex = (Integer)value;
        }

        spinner.setSelection(selectionIndex);
    }

    @Override
    public void refresh() {
        refresh(getSpinner());
    }
}