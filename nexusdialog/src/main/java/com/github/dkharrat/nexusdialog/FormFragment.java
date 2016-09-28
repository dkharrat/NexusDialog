package com.github.dkharrat.nexusdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <code>FormFragment</code> provides a default Fragment implementation for using NexusDialog.
 */
public abstract class FormFragment extends Fragment implements FormInitializer {
    private FormManager formManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_activity, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
