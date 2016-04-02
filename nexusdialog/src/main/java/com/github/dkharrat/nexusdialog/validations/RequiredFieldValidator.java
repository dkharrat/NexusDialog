package com.github.dkharrat.nexusdialog.validations;

import android.text.TextUtils;

public class RequiredFieldValidator implements InputValidator {
    @Override
    public ValidationError validate(Object value, String fieldName, String fieldLabel) {
        if (value == null || (value instanceof String && TextUtils.isEmpty((String) value))) {
            return new RequiredField(fieldName, fieldLabel);
        }
        return null;
    }

    /**
     * Makes every instances of {@link RequiredFieldValidator} equal.
     *
     * @param o The object to compare.
     * @return true if o is also an instance of RequiredFieldValidator, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o) || o != null && getClass() == o.getClass();
    }

    /**
     * Every instance of {{@link RequiredFieldValidator}} share the same hashcode.
     *
     * @return 0
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
