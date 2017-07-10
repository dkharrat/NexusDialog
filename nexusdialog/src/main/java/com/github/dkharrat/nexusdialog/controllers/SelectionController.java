package com.github.dkharrat.nexusdialog.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.validations.InputValidator;

/**
 * Represents a field that allows a user to select from a list of items.
 * <p/>
 * For the field value, the associated FormModel must return either a String or a 0-based index, representing the
 * currently selected item. Which representation to use is specified by the constructor. In either representation, no
 * selection can be represented by returning {@code null} for the value of the field.
 */
public class SelectionController extends LabeledFieldController {

    private final int spinnerId = FormController.generateViewId();

    private final String prompt;
    private final List<String> items;
    private final List<?> values;

    /**
     * Constructs a selection field
     *
     * @param ctx                   the Android context
     * @param name                  the name of the field
     * @param labelText             the label to display beside the field. Set to {@code null} to not show a label.
     * @param validators            contains the validations to process on the field
     * @param prompt                if nothing is selected, 'prompt' is displayed
     * @param items                 a list of Strings defining the selection items to show
     * @param useItemsAsValues      if true, {@code SelectionController} expects the associated form model to use
     *                              the same string of the selected item when getting or setting the field; otherwise,
     *                              {@code SelectionController} expects the form model to use index (as an Integer) to
     *                              represent the selected item
     */
    public SelectionController(Context ctx, String name, String labelText, Set<InputValidator> validators, String prompt, List<String> items, boolean useItemsAsValues) {
        this(ctx, name, labelText, validators, prompt, items, useItemsAsValues ? items : null);
    }

    /**
     * Constructs a selection field
     *
     * @param ctx                   the Android context
     * @param name                  the name of the field
     * @param labelText             the label to display beside the field
     * @param validators            contains the validations to process on the field
     * @param prompt                if nothing is selected, 'prompt' is displayed
     * @param items                 a list of Strings defining the selection items to show
     * @param values                a list of Objects representing the values to set the form model on a selection (in
     *                              the same order as the {@code items}.
     */
    public SelectionController(Context ctx, String name, String labelText, Set<InputValidator> validators, String prompt, List<String> items, List<?> values) {
        super(ctx, name, labelText, validators);
        this.prompt = prompt;
        this.items = new ArrayList<>(items);
        this.items.add(prompt);     // last item is used for the 'prompt' by the SpinnerView
        this.values = new ArrayList<>(values);
    }

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
        this.items = new ArrayList<>(items);
        this.items.add(prompt);     // last item is used for the 'prompt' by the SpinnerView
        this.values = new ArrayList<>(values);
    }

    /**
     * Returns the Spinner view associated with this element.
     *
     * @return the Spinner view associated with this element
     */
    public Spinner getSpinner() {
        return (Spinner)getView().findViewById(spinnerId);
    }

    @Override
    protected View createFieldView() {
        Spinner spinnerView = new Spinner(getContext());
        spinnerView.setId(spinnerId);
        spinnerView.setPrompt(prompt);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView itemView = ((TextView)view.findViewById(android.R.id.text1));
                    itemView.setText("");
                    itemView.setHint(getItem(getCount()));
                }

                return view;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // don't display last item (it's used for the prompt)
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerView.setAdapter(spinnerAdapter);

        spinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object value;
                // if no values are specified, set the index on the model
                if (values == null) {
                    value = pos;
                } else {
                    // last pos indicates nothing is selected
                    if (pos == items.size()-1) {
                        value = null;
                    } else {    // if something is selected, set the value on the model
                        value = values.get(pos);
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
        int selectionIndex = items.size()-1;    // index of last item shows the 'prompt'

        if (values != null) {
            for (int i=0; i< values.size(); i++) {
                if (values.get(i).equals(value)) {
                    selectionIndex = i;
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
