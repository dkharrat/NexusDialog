package com.github.dkharrat.nexusdialog;

import android.content.Context;
import android.view.View;

/**
 * The base class for all form elements, such as text fields, buttons, sections, etc. Each {@code FormElementController}
 * is referred by a name and has an associated {@link FormModel}.
 */
public abstract class FormElementController {
    private final String name;
    private FormModel model;

    /**
     * Constructs a new instance with the specified name
     * @param name the name of this instance
     */
    protected FormElementController(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this form element.
     *
     * @return  the name of the element
     */
    public String getName() {
        return name;
    }

    void setModel(FormModel model) {
        this.model = model;
    }

    /**
     * Returns the associated model of this form element.
     *
     * @return the associated model of this form element
     */
    public FormModel getModel() {
        return model;
    }

    /**
     * Construct the view to display for this element.
     *
     * @param context   the Android context
     * @return          a newly created view for this element
     */
    public abstract View constructView(Context context);
}