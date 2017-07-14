package com.github.dkharrat.nexusdialog.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.validations.InputValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a field that allows a user to select multiple items from checkboxes.
 * <p/>
 * For the field value, the associated FormModel must return either a Set<String> or a Set<0-based index>, representing the
 * currently selected items. Which representation to use is specified by the constructor. In either representation, no
 * selection can be represented by returning {@code null} for the value of the field.
 */
public class CheckBoxController extends LabeledFieldController {
    private final int CHECKBOX_ID = FormController.generateViewId();
    private final List<String> items;
    private final List<?> values;

    /**
     * Constructs a new instance of a checkboxes field.
     *
     * @param ctx              the Android context
     * @param name             the name of the field
     * @param labelText        the label to display beside the field. Set to {@code null} to not show a label
     * @param validators       contains the validations to process on the field
     * @param items            a list of Strings defining the selection items to show
     * @param useItemsAsValues if true, {@code CheckBoxController} expects the associated form model to use
     *                         the same string of the selected item when getting or setting the field; otherwise,
     *                         {@code CheckBoxController} expects the form model to use index (as an Integer) to
     *                         represent the selected item
     */
    public CheckBoxController(Context ctx, String name, String labelText, Set<InputValidator> validators, List<String> items, boolean useItemsAsValues) {
        this(ctx, name, labelText, validators, items, useItemsAsValues ? items : null);
    }

    /**
     * Constructs a new instance of a checkboxes field.
     *
     * @param ctx        the Android context
     * @param name       the name of the field
     * @param labelText  the label to display beside the field
     * @param validators contains the validations to process on the field
     * @param items      a list of Strings defining the selection items to show
     * @param values     a list of Objects representing the values to set the form model on a selection (in
     *                   the same order as the {@code items}.
     */
    public CheckBoxController(Context ctx, String name, String labelText, Set<InputValidator> validators, List<String> items, List<?> values) {
        super(ctx, name, labelText, validators);
        this.items = items;
        this.values = values;

        if(values != null && items.size() != values.size()) {
            throw new IllegalArgumentException("Size of Values and Items must be equal.");
        }
    }


    /**
     * Constructs a new instance of a checkboxes field.
     *
     * @param ctx              the Android context
     * @param name             the name of the field
     * @param labelText        the label to display beside the field. Set to {@code null} to not show a label
     * @param isRequired       indicates if the field is required or not
     * @param items            a list of Strings defining the selection items to show
     * @param useItemsAsValues if true, {@code CheckBoxController} expects the associated form model to use
     *                         the same string of the selected item when getting or setting the field; otherwise,
     *                         {@code CheckBoxController} expects the form model to use index (as an Integer) to
     *                         represent the selected item
     */
    public CheckBoxController(Context ctx, String name, String labelText, boolean isRequired, List<String> items, boolean useItemsAsValues) {
        this(ctx, name, labelText, isRequired, items, useItemsAsValues ? items : null);
    }

    /**
     * Constructs a new instance of a checkboxes field.
     *
     * @param ctx        the Android context
     * @param name       the name of the field
     * @param labelText  the label to display beside the field
     * @param isRequired indicates if the field is required or not
     * @param items      a list of Strings defining the selection items to show
     * @param values     a list of Objects representing the values to set the form model on a selection (in
     *                   the same order as the {@code items}.
     */
    public CheckBoxController(Context ctx, String name, String labelText, boolean isRequired, List<String> items, List<?> values) {
        super(ctx, name, labelText, isRequired);
        this.items = items;
        this.values = values;

        if(values != null && items.size() != values.size()) {
            throw new IllegalArgumentException("Size of Values and Items must be equal.");
        }
    }

    @Override
    protected View createFieldView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup checkboxContainer = (ViewGroup) inflater.inflate(R.layout.form_checkbox_container, null);

        CheckBox checkBox;
        int nbItem = items.size();
        for (int index = 0; index < nbItem; index++) {
            checkBox = new CheckBox(getContext());
            checkBox.setText(items.get(index));
            checkBox.setId(CHECKBOX_ID + index);
            checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = buttonView.getId() - CHECKBOX_ID;
                    Object value = areValuesDefined() ? values.get(position): position;
                    Set<Object> modelValues = new HashSet<>(retrieveModelValues());
                    if (isChecked) {
                        modelValues.add(value);
                    } else {
                        modelValues.remove(value);
                    }
                    getModel().setValue(getName(), modelValues);
                }
            });

            checkboxContainer.addView(checkBox);
            refresh(checkBox, index);
        }
        return checkboxContainer;
    }

    public void refresh(CheckBox checkbox, int index) {
        Set<Object> modelValues = retrieveModelValues();
        checkbox.setChecked(
                modelValues.contains(
                        areValuesDefined() ? values.get(index) : index
                )
        );
    }

    @Override
    public void refresh() {
        ViewGroup layout = getContainer();

        CheckBox checkbox;
        int nbItem = items.size();
        for (int index = 0; index < nbItem; index++) {
            checkbox = (CheckBox) layout.findViewById(CHECKBOX_ID + index);
            refresh(checkbox, index);
        }
    }

    /**
     * Returns the status of the values entry.
     *
     * @return true if values entry can be used. false otherwise.
     */
    private boolean areValuesDefined() {
        return values != null;
    }

    /**
     * Returns the values hold in the model.
     *
     * @return The values from the model.
     */
    private Set<Object> retrieveModelValues() {
        Set<Object> modelValues = (Set<Object>) getModel().getValue(getName());
        if (modelValues == null) {
            modelValues = new HashSet<>();
        }
        return modelValues;
    }

    /**
     * Returns the View containing the checkboxes.
     *
     * @return The View containing the checkboxes.
     */
    private ViewGroup getContainer() {
        return (ViewGroup) getView().findViewById(R.id.form_checkbox_container);
    }
}
