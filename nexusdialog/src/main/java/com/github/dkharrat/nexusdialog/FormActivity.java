package com.github.dkharrat.nexusdialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

/**
 * <code>FormActivity</code> is provides a default Activity implementation for using NexusDialog. It provides simple APIs to quickly
 * create and manage form fields. If you'd like the Activity to be based on <code>AppCompatActivity</code>, you can use
 * {@link FormWithAppCompatActivity}
 */
public abstract class FormActivity extends Activity {

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
