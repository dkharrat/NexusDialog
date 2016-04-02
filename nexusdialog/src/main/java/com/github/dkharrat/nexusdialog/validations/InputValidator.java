package com.github.dkharrat.nexusdialog.validations;

public interface InputValidator {
    /**
     * Defines the validity of an object against a specific requirement.
     *
     * @param value      The input value to check.
     * @param fieldName  The name of the field,
     *                   can be used to generate a specific error message.
     * @param fieldLabel The label of the field,
     *                   can be used to generate a specific error message.
     * @return ValidationError If the input does not pass the validation requirements, null otherwise.
     */
    ValidationError validate(Object value, String fieldName, String fieldLabel);
}
