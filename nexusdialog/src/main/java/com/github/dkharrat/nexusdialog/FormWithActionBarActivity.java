package com.github.dkharrat.nexusdialog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

/**
 * <code>FormWithActionBarActivity</code> is provides a default Activity implementation for using NexusDialog. If you'd
 * like the Activity to be based on the standard Android <code>Activity</code>, you can use {@link FormActivity}
 */
public abstract class FormWithActionBarActivity extends ActionBarActivity {

    private FormController formController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form_activity);
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        formController = new FormController(this);
        initForm();

        ViewGroup containerView = (ViewGroup)findViewById(R.id.form_elements_container);
        formController.addFormElementsToView(containerView);
    }

    /**
     * An abstract method that must be overridden by subclasses where the form fields are initialized.
     */
    protected abstract void initForm();

    /**
     * Returns the associated form controller
     */
    public FormController getFormController() {
        return formController;
    }

    /**
     * Returns the associated form model
     */
    public FormModel getModel() {
        return formController.getModel();
    }
}
