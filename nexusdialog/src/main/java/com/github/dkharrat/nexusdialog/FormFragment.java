package com.github.dkharrat.nexusdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * <code>FormFragment</code> provides a default Fragment implementation for using NexusDialog. It also handles retaining the
 * <code>FormModel</code> when the Fragment is recreated.
 */
public abstract class FormFragment extends Fragment {
    private FormModelFragment formModelFragment;
    private FormController formController;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_activity, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.formModelFragment = FragmentActivityHelper.getFormModelFragment(this.getActivity());
        this.formController = new FormController(context, formModelFragment.getModel());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initForm(formController);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recreateViews();
    }

    /**
     * An abstract method that must be overridden by subclasses where the form fields are initialized.
     */
    abstract public void initForm(FormController controller);

    /**
     * Returns the associated <code>FormController</code> that manages the form fields.
     */
    public FormController getFormController() {
        return formController;
    }

    /**
     * Returns the associated model of this form.
     */
    public FormModel getModel() {
        return formModelFragment.getModel();
    }

    /**
     * Sets the model to use for this form
     *
     * @param formModel the model to use
     */
    public void setModel(FormModel formModel) {
        this.formModelFragment.setModel(formModel);
        formController.setModel(formModel);
    }

    /**
     * Recreates the views for all the elements that are in the form. This method needs to be called when field are dynamically added or
     * removed
     */
    protected void recreateViews() {
        ViewGroup containerView = (ViewGroup) getActivity().findViewById(R.id.form_elements_container);
        formController.recreateViews(containerView);
    }
}
