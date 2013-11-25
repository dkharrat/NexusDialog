package com.github.dkharrat.nexusdialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;
import com.github.dkharrat.nexusdialog.util.MessageUtil;
import com.github.dkharrat.nexusdialog.validations.ValidationError;

/**
 * <code>FormActivity</code> is the base class for an Activity that uses NexusDialog. It provides simple APIs to quickly
 * create and manage form fields. <code>FormActivity</code> itself inherits from <code>ActionBarActivity</code>, which
 * is a class provided by Google's <a href="http://developer.android.com/tools/support-library/features.html#v7">v7
 * appcompat library</a>. ActionBarActivity provides
 * <a href="http://developer.android.com/guide/topics/ui/actionbar.html">ActionBar</a> support on older Android API
 * versions which don't natively support it. If you don't need ActionBar support for your Activity, you can disable it
 * by calling <code>getSupportActionBar().hide()</code> in the {@link #initForm} method.
 *
 * The form's data is backed by a model represented by {@link FormModel}, which provides a generic interface to access
 * the data. Form elements use the model to retrieve current field values and set them upon user input. By default,
 * <code>FormActivity</code> uses a default Map-based model keyed by the element's names. You can also use a custom
 * implementation of a <code>FormModel</code> if desired.
 */
public abstract class FormActivity extends ActionBarActivity {

    private final List<FormSectionController> sectionControllers = new ArrayList<FormSectionController>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form_activity);
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initForm();
        setupView();
    }

    /**
     * An abstract method that must be overridden by subclasses where the form fields are initialized.
     */
    protected abstract void initForm();

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    /**
     * Returns the associated model of this form.
     *
     * @return the associated model of this form
     */
    protected FormModel getModel() {
        return model;
    }

    /**
     * Sets the model to use for this form
     *
     * @param formModel the model to use
     */
    protected void setModel(FormModel formModel) {
        this.model = formModel;
    }

    /**
     * Returns a list of the sections of this form.
     *
     * @return a list containing all the <code>FormSectionController</code>'s of this form
     */
    protected List<FormSectionController> getSections() {
        return sectionControllers;
    }

    /**
     * Returns the corresponding <code>FormSectionController</code> from the specified name.
     *
     * @param name  the name of the section
     * @return      the instance of <code>FormSectionController</code> with the specified name, or null if no such
     *              section exists
     */
    protected FormSectionController getSection(String name) {
        for (FormSectionController section : getSections()) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    /**
     * Adds the specified section to the form.
     *
     * @param section the form section to add
     */
    protected void addSection(FormSectionController section) {
        sectionControllers.add(section);
    }

    /**
     * Returns the corresponding <code>FormElementController</code> from the specified name.
     * @param name  the name of the form element
     * @return      the instance of <code>FormElementController</code> with the specified name, or null if no such
     *              element exists
     */
    protected FormElementController getElement(String name) {
        for (FormSectionController section : getSections()) {
            FormElementController element = section.getElement(name);
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    /**
     * Returns the total number of elements in this form, not including sections.
     *
     * @return  the total number of elements in this form, not including sections
     */
    protected int getNumberOfElements() {
        int count = 0;
        for (FormSectionController section : getSections()) {
            count += section.getElements().size();
        }

        return count;
    }

    /**
     * Returns a list of validation errors of the form's input
     *
     * @return a list of validation errors of the form's input
     */
    protected List<ValidationError> validateInput() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        for (FormSectionController section : getSections()) {
            for (FormElementController element : section.getElements()) {
                if (element instanceof LabeledFieldController) {
                    LabeledFieldController field = (LabeledFieldController)element;
                    errors.addAll(field.validateInput());
                }
            }
        }

        return errors;
    }

    /**
     * Indicates if the current user input is valid.
     *
     * @return  true if the current user input is valid, otherwise false
     */
    protected boolean isValidInput() {
        return validateInput().isEmpty();
    }

    /**
     * Shows an appropriate error message if there are validation errors in the form's input.
     */
    protected void showValidationErrors() {
        Resources res = getResources();
        for (ValidationError error : validateInput()) {
            MessageUtil.showAlertMessage(res.getString(R.string.validation_error_title), error.getMessage(res), this);
        }
    }

    private void setupView() {
        LinearLayout containerView = (LinearLayout)findViewById(R.id.form_elements_container);

        for (FormSectionController section : getSections()) {
            ((FormElementController)section).setModel(getModel());
            containerView.addView(section.constructView(this));

            for (FormElementController element : section.getElements()) {
                element.setModel(getModel());
                containerView.addView(element.constructView(this));
            }
        }
    }

    private FormModel model = new FormModel() {

        private Map<String,Object> data = new HashMap<String,Object>();

        @Override
        public Object getBackingValue(String name) {
            return data.get(name);
        }

        @Override
        public void setBackingValue(String name, Object value) {
            data.put(name, value);
        }
    };
}
