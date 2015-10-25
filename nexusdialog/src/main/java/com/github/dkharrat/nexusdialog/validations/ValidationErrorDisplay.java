package com.github.dkharrat.nexusdialog.validations;

import android.content.Context;
import android.content.res.Resources;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.utils.MessageUtil;

import java.util.List;

/**
 * Defines methods to display the validation errors.
 */
public interface ValidationErrorDisplay {
    /**
     * Display the validation errors.
     *
     * @param errors The errors to show.
     */
    void showErrors(List<ValidationError> errors);

    /**
     * Dismiss the validation errors.
     */
    void resetErrors();
}
