package com.github.dkharrat.nexusdialog.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.validations.InputValidator;
import com.github.dkharrat.nexusdialog.validations.RequiredFieldValidator;
import com.github.dkharrat.nexusdialog.validations.ValidationError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract class that represents a generic form field with an associated label.
 */
public abstract class LabeledFieldController extends FormElementController {
    private static final RequiredFieldValidator REQUIRED_FIELD_VALIDATOR = new RequiredFieldValidator();
    private final String labelText;
    private View fieldView;
    private TextView errorView;
    private Set<InputValidator> validators;

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
        this(ctx, name, labelText, new HashSet<InputValidator>());
        setIsRequired(isRequired);
    }

    /**
     * Creates a labeled field.
     *
     * @param ctx           the Android context
     * @param name          the name of the field
     * @param labelText     the label to display beside the field. If null, no label is displayed and the field will
     *                      occupy the entire length of the row.
     * @param validators    The list of input validations to add to the field.
     */
    public LabeledFieldController(Context ctx, String name, String labelText, Set<InputValidator> validators) {
        super(ctx, name);
        this.labelText = labelText;
        this.validators = validators;
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
        if (! required) {
            validators.remove(REQUIRED_FIELD_VALIDATOR);
        } else if (! isRequired()) {
            validators.add(REQUIRED_FIELD_VALIDATOR);
        }
    }

    /**
     * Changes the validators for the given field.
     *
     * @param newValidators THe new validators to use.
     */
    public void setValidators(Set<InputValidator> newValidators) {
        validators = newValidators;
    }

    /**
     * Indicates whether this field requires an input value.
     *
     * @return  true if this field is required to have input, otherwise false
     */
    public boolean isRequired() {
        return validators.contains(REQUIRED_FIELD_VALIDATOR);
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
        List<ValidationError> errors = new ArrayList<>();
        Object value = getModel().getValue(getName());
        ValidationError error;
        for (InputValidator validator : validators) {
            error = validator.validate(value, getName(), getLabel());
            if (error != null){
                errors.add(error);
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
