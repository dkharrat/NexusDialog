package com.github.dkharrat.nexusdialog.sample;

import android.content.res.Resources;

import com.github.dkharrat.nexusdialog.validations.InputValidator;
import com.github.dkharrat.nexusdialog.validations.ValidationError;

public class CustomValidation implements InputValidator {
    @Override
    public ValidationError validate(Object value, String fieldName, String fieldLabel) {
        if (value != null) {
            if (value instanceof String) {
                try {
                    return assertEven(Integer.decode((String) value), fieldName, fieldLabel);
                } catch (NumberFormatException e) {
                    // Do nothing as the input isn't a number
                }
            }
            if (value instanceof Number) {
                return assertEven((Integer) value, fieldName, fieldLabel);
            }
            // Any other type doesn't have to be checked by this validator
        }
        return null;
    }

    private ValidationError assertEven(int value, String fieldName, String fieldLabel) {
        if (value % 2 != 0) {
            return new ValidationError(fieldName, fieldLabel) {
                @Override
                public String getMessage(Resources resources) {
                    return resources.getString(R.string.custom_validation_error);
                }
            };
        }
        return null;
    }
}
