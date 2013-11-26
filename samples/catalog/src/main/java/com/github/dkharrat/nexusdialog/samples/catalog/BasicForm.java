package com.github.dkharrat.nexusdialog.samples.catalog;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.github.dkharrat.nexusdialog.*;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SearchableSelectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicForm extends FormActivity {
    @Override
    protected void initForm() {
        FormSectionController section = new FormSectionController("Personal Info");
        section.addElement(new EditTextController("firstName", "First name"));
        section.addElement(new EditTextController("lastName", "Last name"));
        section.addElement(new SelectionController("gender", "Gender", false, "Select",
                new ArrayList<String>(Arrays.asList("Male", "Female")), true) {{
            setIsRequired(true);
        }});
        section.addElement(new SearchableSelectionController("favColor", "Favorite Color", false, "Blue", new SearchableSelectionController.SelectionDataSource() {
            @Override
            public List<String> getItems() {
                return new ArrayList<String>(Arrays.asList("Red", "Blue", "Green", "Purple", "Pink"));
            }
        }));

        addSection(section);

        setTitle("Simple Form");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        super.onOptionsItemSelected(item);
        showValidationErrors();
        return true;
    }
}
