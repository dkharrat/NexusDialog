package com.github.dkharrat.nexusdialog;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

/**
 * <code>FormActivity</code> is provides a default Activity implementation for using NexusDialog. It provides simple APIs to quickly
 * create and manage form fields. If you'd like the Activity to be based on <code>AppCompatActivity</code>, you can use
 * {@link FormWithAppCompatActivity}
 */
public abstract class FormActivity extends FragmentActivity {
    private static final String MODEL_BUNDLE_KEY = "nd_model";
    private FormController formController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form_activity);
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        formController = new FormController(this);
        initForm();

        FragmentManager fm = getSupportFragmentManager();
        FormModel retainedModel = (FormModel) fm.findFragmentByTag(MODEL_BUNDLE_KEY);

        if (retainedModel == null) {
            retainedModel = formController.getModel();
            fm.beginTransaction().add(retainedModel, MODEL_BUNDLE_KEY).commit();
        }
        formController.setModel(retainedModel);
        recreateViews();
    }

    /**
     * Reconstructs the form element views. This must be called after form elements are dynamically added or removed.
     */
    protected void recreateViews() {
        ViewGroup containerView = (ViewGroup) findViewById(R.id.form_elements_container);
        formController.recreateViews(containerView);
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
