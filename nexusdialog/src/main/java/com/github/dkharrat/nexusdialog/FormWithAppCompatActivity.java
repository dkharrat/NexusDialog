package com.github.dkharrat.nexusdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * <code>FormWithAppCompatActivity</code> is provides a default Activity implementation for using NexusDialog. If you'd
 * like the Activity to be based on the standard Android <code>Activity</code>, you can use {@link FormActivity}
 */
public abstract class FormWithAppCompatActivity extends AppCompatActivity implements FormInitializer {
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
