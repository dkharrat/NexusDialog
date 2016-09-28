package com.github.dkharrat.nexusdialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Manage a form lifecycle inside a {@link FragmentActivity} or {@link Fragment}.
 */
public class FormManager {
    private static final String MODEL_BUNDLE_KEY = "nd_model";

    private final FragmentActivity enclosing;
    private final FormInitializer initializer;
    private final FormController formController;
    private final int formResourceID;

    /**
     * Create a <code>FormManager</code> for a {@link FragmentActivity}.
     *
     * @param enclosing      The activity in which the form should be displayed.
     * @param initializer    The form initialization sequence.
     * @param formResourceID Identifier of the view containing the form elements.
     */
    public FormManager(FragmentActivity enclosing, FormInitializer initializer, int formResourceID) {
        this.enclosing = enclosing;
        this.initializer = initializer;
        formController = new FormController(enclosing);
        this.formResourceID = formResourceID;
        initialize();
    }

    /**
     * Create a <code>FormManager</code> for a {@link Fragment}.
     *
     * @param enclosing      The activity in which the form should be displayed.
     * @param initializer    The form initialization sequence.
     * @param formResourceID Identifier of the view containing the form elements.
     */
    public FormManager(Fragment enclosing, FormInitializer initializer, int formResourceID) {
        this(enclosing.getActivity(), initializer, formResourceID);
    }

    public FormController getFormController() {
        return formController;
    }

    /**
     * Initialize the Form as requested in the given {@link FormInitializer} implementation,
     * and manage its retained model.
     */
    private void initialize() {
        enclosing.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initializer.initForm(formController);

        FragmentManager fm = enclosing.getSupportFragmentManager();
        FormModel retainedModel = (FormModel) fm.findFragmentByTag(MODEL_BUNDLE_KEY);

        if (retainedModel == null) {
            retainedModel = formController.getModel();
            fm.beginTransaction().add(retainedModel, MODEL_BUNDLE_KEY).commit();
        }
        formController.setModel(retainedModel);
        recreateViews();
    }

    /**
     * Reconstructs the form element views.
     * This must be called after form elements are dynamically added or removed.
     */
    public void recreateViews() {
        ViewGroup containerView = (ViewGroup) enclosing.findViewById(formResourceID);
        formController.recreateViews(containerView);
    }
}
