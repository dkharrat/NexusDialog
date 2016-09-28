package com.github.dkharrat.nexusdialog.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.FormFragment;
import com.github.dkharrat.nexusdialog.controllers.CheckBoxController;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;
import com.github.dkharrat.nexusdialog.utils.MessageUtil;

import java.util.Arrays;

public class FragmentFormActivity extends FragmentActivity {
    private static final String FORM_FRAGMENT_KEY = "nd_form";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);

        FragmentManager fm = getSupportFragmentManager();
        MyFormFragment formFragment;

        Fragment retainedFragment = fm.findFragmentByTag(FORM_FRAGMENT_KEY);
        if (retainedFragment != null && retainedFragment instanceof MyFormFragment) {
            formFragment = (MyFormFragment) retainedFragment;
        } else {
            formFragment = new MyFormFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, formFragment, FORM_FRAGMENT_KEY)
                    .commit();
        }
        setSubmitAction(formFragment);
    }

    private void setSubmitAction(final MyFormFragment formFragment) {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formFragment.validate();
            }
        });
    }

    public static class MyFormFragment extends FormFragment {
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String GENDER = "gender";
        public static final String HOBBIES = "hobbies";

        public boolean validate() {
            getFormController().resetValidationErrors();
            if (getFormController().isValidInput()) {
                Object firstName = getModel().getValue(FIRST_NAME);
                Object lastName = getModel().getValue(LAST_NAME);
                Object gender = getModel().getValue(GENDER);
                Object favColor = getModel().getValue(HOBBIES);

                String msg = "First name: " + firstName + "\n"
                        + "Last name: " + lastName + "\n"
                        + "Gender: " + gender + "\n"
                        + "Hobbies: " + favColor + "\n";
                MessageUtil.showAlertMessage("Field Values", msg, getActivity());
            } else {
                getFormController().showValidationErrors();
            }
            return true;
        }

        @Override
        public void initForm(FormController controller) {
            Context ctxt = getContext();
            FormSectionController section = new FormSectionController(ctxt, "Personal Info");
            section.addElement(new EditTextController(ctxt, FIRST_NAME, "First name"));
            section.addElement(new EditTextController(ctxt, LAST_NAME, "Last name"));
            section.addElement(new SelectionController(ctxt, GENDER, "Gender", true, "Select", Arrays.asList("Male", "Female"), true));
            section.addElement(new CheckBoxController(ctxt, HOBBIES, "You like", true, Arrays.asList("sport", "gaming", "relaxation", "development"), true));
            controller.addSection(section);
        }
    }
}
