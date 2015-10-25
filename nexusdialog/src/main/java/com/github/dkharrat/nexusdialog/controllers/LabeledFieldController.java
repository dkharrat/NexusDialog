package com.github.dkharrat.nexusdialog.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.validations.RequiredField;
import com.github.dkharrat.nexusdialog.validations.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that represents a generic form field with an associated label.
 */
public abstract class LabeledFieldController extends FormElementController {
    private final String labelText;
    private boolean required;
    private View fieldView;
    private TextView errorView;

    /**
     * Creates a labeled field.
     *
     * @param ctx           the Android context
     * @param name          the name of the field
     * @param labelText     the label to display beside the field. If null, no label is displayed and the field will
     *                      occupy the entire length of the row.
     * @param isRequired    indicates whether this field is required. If true, this field checks for a non-empty or
     *                      non-null value upon validation. Otherwise, this field can be empty.
     */
    public LabeledFieldController(Context ctx, String name, String labelText, boolean isRequired) {
        super(ctx, name);
        this.labelText = labelText;
        required = isRequired;
    }

    /**
     * Returns the associated label for this field.
     *
     * @return the associated label for this field
     */
    public String getLabel() {
        return labelText;
    }

    /**
     * Sets whether this field is required to have user input.
     *
     * @param required  if true, this field checks for a non-empty or non-null value upon validation. Otherwise, this
     *                  field can be empty.
     */
    public void setIsRequired(boolean required) {
        this.required = required;
    }

    /**
     * Indicates whether this field requires an input value.
     *
     * @return  true if this field is required to have input, otherwise false
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Indicates whether the input of this field has any validation errors.
     *
     * @return  true if there are some validation errors, otherwise false
     */
    public boolean isValidInput() {
        return validateInput().isEmpty();
    }

    /**
     * Runs a validation on the user input and returns all the validation errors of this field.
     * Previous error messages are removed when calling {@code validateInput()}.
     *
     * @return  a list containing all the validation errors
     */
    public List<ValidationError> validateInput() {
        setError(null);
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (isRequired()) {
            Object value = getModel().getValue(getName());
            if (value == null || (value instanceof String && TextUtils.isEmpty((String)value))) {
                errors.add(new RequiredField(getName(), getLabel()));
            }
        }

        return errors;
    }

    /**
     * Returns the associated view for the field (without the label view) of this element.
     *
     * @return          the view for this element
     */
    public View getFieldView() {
        if (fieldView == null) {
            fieldView = createFieldView();
        }
        return fieldView;
    }

    /**
     * Constructs the view associated with this field without the label. It will be used to combine with the label.
     *
     * @return          the newly created view for this field
     */
    protected abstract View createFieldView();

    @Override
    protected View createView() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.form_labeled_element, null);
        errorView = (TextView) view.findViewById(R.id.field_error);

        TextView label = (TextView)view.findViewById(R.id.field_label);
        if (labelText == null) {
            label.setVisibility(View.GONE);
        } else {
            label.setText(labelText);
        }

        FrameLayout container = (FrameLayout)view.findViewById(R.id.field_container);
        container.addView(getFieldView());

        return view;
    }

    @Override
    public void setError(String message) {
        if (message == null) {
            errorView.setVisibility(View.GONE);
        } else {
            errorView.setText(message);
            errorView.setVisibility(View.VISIBLE);
        }
    }
}
