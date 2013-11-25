package com.github.dkharrat.nexusdialog.validations;

import android.content.res.Resources;

/**
 * Represents a validation error of a form's input field.
 */
public abstract class ValidationError {
    private final String fieldName;

    /**
     * Creates a new instance with the specified field name.
     *
     * @param fieldName     the field name
     */
    public ValidationError(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the name of the field associated with the validation error.
     *
     * @return  the name of the field that has the error
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns a human-readable description of the validation error.
     *
     * @param resources the application's resources
     * @return          a string describing the error
     */
    public abstract String getMessage(Resources resources);
}
