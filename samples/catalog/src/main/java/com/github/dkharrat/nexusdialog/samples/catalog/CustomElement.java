package com.github.dkharrat.nexusdialog.samples.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;

public class CustomElement extends LabeledFieldController {

    public CustomElement(Context ctx, String name, String label) {
        super(ctx, name, label, false);
    }

    @Override
    protected View constructFieldView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.custom_element, null);
    }

    public Button getButton() {
        return (Button)getView().findViewById(R.id.btn);
    }

    public void refresh() {
        // nothing to refresh
    }
}
