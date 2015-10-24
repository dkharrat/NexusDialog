package com.github.dkharrat.nexusdialog.validations;

import android.content.Context;
import android.content.res.Resources;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.utils.MessageUtil;

public enum ValidationErrorDisplayMethod {
    GENERAL {
        @Override
        public void showErrors() {
            StringBuilder sb = new StringBuilder();
            Resources res = context.getResources();
            for (ValidationError error : controller.validateInput()) {
                sb.append(error.getMessage(res) + "\n");
            }
            MessageUtil.showAlertMessage(context.getString(R.string.validation_error_title), sb.toString(), context);
        }
    }, PER_FIELD {
        @Override
        public void showErrors() {
            Resources res = context.getResources();
            for (ValidationError error : controller.validateInput()) {
                FormElementController element = controller.getElement(error.getFieldName());
                element.setError(error.getMessage(res));
            }
        }
    };

    public void setContext(Context context) {
        this.context = context;
    }

    public void setController(FormController controller) {
        this.controller = controller;
    }

    protected Context context;
    protected FormController controller;

    public abstract void showErrors();
}
