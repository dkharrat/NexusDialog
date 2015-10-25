package com.github.dkharrat.nexusdialog.validations;

import android.content.Context;
import android.content.res.Resources;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.FormElementController;

import java.util.List;

public class PerFieldValidationErrorDisplay implements ValidationErrorDisplay {
    private final Context context;
    private final FormController controller;

    public PerFieldValidationErrorDisplay(Context context, FormController controller) {
        this.context = context;
        this.controller = controller;
    }

    @Override
    public void showErrors(List<ValidationError> errors) {
        Resources res = context.getResources();
        FormElementController element;
        for (ValidationError error : errors) {
            element = controller.getElement(error.getFieldName());
            element.setError(error.getMessage(res));
        }
    }
}
