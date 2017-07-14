package com.github.dkharrat.nexusdialog.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.MapFormModel;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;

import java.util.Arrays;

/**
 * Demonstrates how to implement a custom Activity that uses {@link FormController} directly. Note that, this example does NOT handle
 * retaining the form model on rotations (as Android will recreate the Activity), which means data will be lost when the activity is
 * rotated. See the Android documentation for how to handle this, or take a look at {@link com.github.dkharrat.nexusdialog.FormActivity}
 * for the implementation provided by NexusDialog.
 */
public class CustomFormActivity extends Activity {

    private FormController formController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_form);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle("Simple Example");

        setupForm();
    }

    protected void setupForm() {
        formController = new FormController(this, new MapFormModel());

        FormSectionController section = new FormSectionController(this, "Personal Info");
        section.addElement(new EditTextController(this, "firstName", "First name"));
        section.addElement(new EditTextController(this, "lastName", "Last name"));
        section.addElement(new SelectionController(this, "gender", "Gender", true, "Select", Arrays.asList("Male", "Female"), true));

        formController.addSection(section);

        ViewGroup containerView = (ViewGroup)findViewById(R.id.form_elements_container);
        formController.recreateViews(containerView);
    }
}
