package com.github.dkharrat.nexusdialog.sample;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.controllers.*;
import com.github.dkharrat.nexusdialog.controllers.SearchableSelectionController.SelectionDataSource;
import com.github.dkharrat.nexusdialog.utils.MessageUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the following functionality:
 * <ul>
 *  <li>SearchableSelectionController</li>
 *  <li>Event handling</li>
 *  <li>Using a Custom Element</li>
 *  <li>Basic Validations</li>
 *  <li>Property change notifications</li>
 * </ul>
 */
public class BasicForm extends FormActivity {

    private final static String FIRST_NAME = "firstName";
    private final static String LAST_NAME = "lastName";
    private final static String FULL_NAME = "fullName";
    private final static String GENDER = "gender";
    private final static String FAVORITE_COLOR = "favColor";
    private final static String CUSTOM_ELEM = "customElem";

    @Override
    protected void initForm() {
        FormSectionController section = new FormSectionController(this, "Personal Info");
        section.addElement(new EditTextController(this, FIRST_NAME, "First name", "Change me"));
        section.addElement(new EditTextController(this, LAST_NAME, "Last name"));
        section.addElement(new ValueController(this, FULL_NAME, "Full name"));
        section.addElement(new SelectionController(this, GENDER, "Gender", true, "Select", Arrays.asList("Male", "Female"), true));
        section.addElement(new SearchableSelectionController(this, FAVORITE_COLOR, "Favorite Color", false, "Blue", dataSource));

        CustomElement customElem = new CustomElement(this, CUSTOM_ELEM, "Custom Element");
        customElem.getButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MessageUtil.showAlertMessage("Greatness", "Yay!!", BasicForm.this);
            }
        });
        section.addElement(customElem);

        addSection(section);

        PropertyChangeListener nameFieldListener = new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent event) {
                Object firstName = getModel().getValue(FIRST_NAME);
                Object lastName = getModel().getValue(LAST_NAME);

                // setting a field value will automatically refresh the form element
                getModel().setValue(FULL_NAME, firstName + " " + lastName);
            }
        };

        // add a listener to get notifications for any field changes
        getModel().addPropertyChangeListener(FIRST_NAME, nameFieldListener);
        getModel().addPropertyChangeListener(LAST_NAME, nameFieldListener);

        // initialize field with a value
        getModel().setValue(LAST_NAME, "Smith");

        setTitle("Basic Form");
    }

    private final SelectionDataSource dataSource = new SelectionDataSource() {
        @Override public List<String> getItems() {
            return Arrays.asList(
                    "Red",
                    "Blue",
                    "Green",
                    "Purple",
                    "Pink"
            );
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        super.onOptionsItemSelected(item);

        if (isValidInput()) {
            Object firstName = getModel().getValue(FIRST_NAME);
            Object lastName = getModel().getValue(LAST_NAME);
            Object gender = getModel().getValue(GENDER);
            Object favColor = getModel().getValue(FAVORITE_COLOR);

            String msg = "First name: " + firstName + "\n"
                    + "Last name: " + lastName + "\n"
                    + "Gender: " + gender + "\n"
                    + "Favorite Color: " + favColor + "\n";
            MessageUtil.showAlertMessage("Field Values", msg, this);
        } else {
            showValidationErrors();
        }
        return true;
    }
}
