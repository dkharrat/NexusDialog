package com.github.dkharrat.nexusdialog.samples.catalog;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.github.dkharrat.nexusdialog.*;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SearchableSelectionController;
import com.github.dkharrat.nexusdialog.controllers.SearchableSelectionController.SelectionDataSource;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;
import com.github.dkharrat.nexusdialog.util.MessageUtil;

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
    private final static String GENDER = "gender";
    private final static String FAVORITE_COLOR = "favColor";
    private final static String CUSTOM_ELEM = "customElem";

    @Override
    protected void initForm() {
        FormSectionController section = new FormSectionController(this, "Personal Info");
        section.addElement(new EditTextController(this, FIRST_NAME, "First name", "Change me"));
        section.addElement(new EditTextController(this, LAST_NAME, "Last name", "Last name"));
        section.addElement(new SelectionController(this, GENDER, "Gender", true, "Select", Arrays.asList("Male", "Female"), true));
        section.addElement(new SearchableSelectionController(this, FAVORITE_COLOR, "Favorite Color", false, "Blue", dataSource));

        CustomElement customElem = new CustomElement(this, CUSTOM_ELEM, "Custom Element");
        customElem.getButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // setting a field value will automatically refresh the form element
                getModel().setValue(FAVORITE_COLOR, "Green");
                MessageUtil.showAlertMessage("Greatness", "You just changed your favorite color :) Check that field to find out what it is.", BasicForm.this);
            }
        });
        section.addElement(customElem);

        addSection(section);

        getModel().addPropertyChangeListener(FIRST_NAME, new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent event) {
                MessageUtil.showAlertMessage(
                        "First name changed!",
                        "Value was: " + event.getOldValue() + ", now: " + event.getNewValue(),
                        BasicForm.this);
            }
        });

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
