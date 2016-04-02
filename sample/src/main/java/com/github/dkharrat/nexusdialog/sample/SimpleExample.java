package com.github.dkharrat.nexusdialog.sample;

import com.github.dkharrat.nexusdialog.FormWithAppCompatActivity;
import com.github.dkharrat.nexusdialog.controllers.CheckBoxController;
import com.github.dkharrat.nexusdialog.controllers.DatePickerController;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;
import com.github.dkharrat.nexusdialog.controllers.TimePickerController;

import java.util.Arrays;

/**
 * Demonstrates the bare minimum to display a form in an Activity.
 */
public class SimpleExample extends FormWithAppCompatActivity {

    @Override protected void initForm() {
        setTitle("Simple Example");

        FormSectionController section = new FormSectionController(this, "Personal Info");
        section.addElement(new EditTextController(this, "firstName", "First name"));
        section.addElement(new EditTextController(this, "lastName", "Last name"));
        section.addElement(new SelectionController(this, "gender", "Gender", true, "Select", Arrays.asList("Male", "Female"), true));
        section.addElement(new CheckBoxController(this, "hobbies", "You like", true, Arrays.asList("sport", "gaming", "relaxation", "development"), true));
        section.addElement(new DatePickerController(this, "date", "Choose Date"));
        section.addElement(new TimePickerController(this, "time", "Choose Time"));
        getFormController().addSection(section);
    }
}
