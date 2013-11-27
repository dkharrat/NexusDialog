package com.github.dkharrat.nexusdialog.samples.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;

public class CustomElement extends LabeledFieldController {

    public CustomElement(String name, String label) {
        super(name, label, false);
    }

    @Override
    protected View constructFieldView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.custom_element, null);
    }
}
