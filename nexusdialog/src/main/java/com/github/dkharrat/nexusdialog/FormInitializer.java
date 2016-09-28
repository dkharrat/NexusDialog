package com.github.dkharrat.nexusdialog;

/**
 * Provides method inflating the form initial content.
 */
public interface FormInitializer {
    /**
     * An abstract method that must be overridden
     * by subclasses where the form fields are initialized.
     */
    void initForm(FormController controller);
}
