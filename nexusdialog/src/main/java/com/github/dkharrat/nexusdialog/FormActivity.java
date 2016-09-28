package com.github.dkharrat.nexusdialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * <code>FormActivity</code> is provides a default Activity implementation for using NexusDialog. It provides simple APIs to quickly
 * create and manage form fields. If you'd like the Activity to be based on <code>AppCompatActivity</code>, you can use
 * {@link FormWithAppCompatActivity}
 */
public abstract class FormActivity extends FragmentActivity implements FormInitializer {
    private FormManager formManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        formManager = new FormManager(this, this, R.id.form_elements_container);
    }

    public FormController getFormController() {
        return formManager.getFormController();
    }

    public FormModel getModel() {
        return getFormController().getModel();
    }

    protected void recreateViews() {
        formManager.recreateViews();
    }
}
