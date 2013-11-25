package com.github.dkharrat.nexusdialog.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Represents a field that allows free-form text.
 */
public class EditTextController extends LabeledFieldController {

    private final int inputType;
    private final String placeholder;

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param name          the name of the field
     * @param labelText     the label to display beside the field
     * @param isRequired    indicates if the field is required or not
     * @param placeholder   a placeholder text to show when the input field is empty
     * @param inputType     the content type of the text box as a mask; possible values are defined by {@link InputType}.
     *                      For example, to enable multi-line, enable {@code InputType.TYPE_TEXT_FLAG_MULTI_LINE}.
     */
    public EditTextController(final String name, String labelText, boolean isRequired, String placeholder, int inputType) {
        super(name, labelText, isRequired);
        this.placeholder = placeholder;
        this.inputType = inputType;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param name          the name of the field
     * @param labelText     the label to display beside the field
     * @param isRequired    indicates if the field is required or not
     */
    public EditTextController(final String name, String labelText, boolean isRequired) {
        this(name, labelText, isRequired, null, InputType.TYPE_CLASS_TEXT);
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param name          the name of the field
     * @param labelText     the label to display beside the field
     */
    public EditTextController(final String name, String labelText) {
        this(name, labelText, false, null, InputType.TYPE_CLASS_TEXT);
    }

    /**
     * Indicates whether this text box has multi-line enabled.
     *
     * @return  true if this text box has multi-line enabled, or false otherwise
     */
    public boolean isMultiLine() {
        return (inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0;
    }

    /**
     * Returns a mask representing the content input type. Possible values are defined by {@link InputType}.
     *
     * @return a mask representing the content input type
     */
    public int getInputType() {
        return inputType;
    }

    @Override
    protected View constructFieldView(Context context) {
        final EditText editText = new EditText(context);

        editText.setSingleLine(!isMultiLine());
        if (placeholder != null) {
            editText.setHint(placeholder);
        }
        editText.setInputType(inputType);
        refreshView(editText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getModel().setValue(getName(), editText.getText().toString());
            }
        });

        return editText;
    }

    private void refreshView(EditText editText) {
        Object value = getModel().getValue(getName());
        editText.setText(value != null ? value.toString() : "");
    }
}