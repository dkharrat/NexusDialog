package com.github.dkharrat.nexusdialog.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.dkharrat.nexusdialog.R;
import com.github.dkharrat.nexusdialog.utils.MessageUtil;

/**
 * Represents a field that allows a user to select from a list of items, with the ability to search for specific items.
 * It also allows for free-form entry if the input text does not exist in the predefined list. This field is useful when
 * the number of items are large. While the list of items are retrieved in the background from the data source, a
 * loading indicator is displayed. For similar functionality, but with a small number of items, use
 * {@link SelectionController} instead.
 * <p/>
 * For the field value, the associated FormModel must return a String representing the currently selected item.
 * If the value does not exist in the list, 'Other (x)' will be displayed, where 'x' is the field value. No selection
 * can be represented by returning {@code null} for the value of the field.
 */
public class SearchableSelectionController extends LabeledFieldController {
    private final static int EDIT_TEXT_ID = 1001;

    private final String placeholder;
    private boolean isFreeFormTextAllowed = true;
    private Dialog selectionDialog = null;
    private final SelectionDataSource dataSource;
    private List<String> items = null;
    private final LoadItemsTask loadItemsTask;
    private ProgressDialog loadingIndicator;
    private boolean otherSelectionIsShowing = false;

    /**
     * An interface that provides the list of items to display for the {@link SearchableSelectionController}.
     */
    public static interface SelectionDataSource {
        /**
         * Returns a list of all the items that can be selected or searched. This method will be called by the
         * {@link SearchableSelectionController} in a background thread.
         *
         * @return a list of all the items that can be selected or searched.
         */
        List<String> getItems();
    }

    /**
     * Creates a new instance of a selection field.
     *
     * @param ctx           the Android context
     * @param name          the name of the field
     * @param labelText     the label to display beside the field. Set to {@code null} to not show a label.
     * @param isRequired    indicates if the field is required or not
     * @param placeholder   a placeholder text to show when the input field is empty
     * @param dataSource    the data source that provides the list of items to display
     */
    public SearchableSelectionController(Context ctx, String name, String labelText, boolean isRequired, String placeholder, SelectionDataSource dataSource) {
        super(ctx, name, labelText, isRequired);
        this.placeholder = placeholder;
        this.dataSource = dataSource;

        loadItemsTask = new LoadItemsTask();
        loadItemsTask.execute(new Void[0]);
    }

    public void setFreeFormTextAllowed(boolean allowed) {
        isFreeFormTextAllowed = allowed;
    }

    public boolean isFreeFormTextAllowed() {
        return isFreeFormTextAllowed;
    }

    protected View createFieldView() {
        final EditText editText = new EditText(getContext());
        editText.setId(EDIT_TEXT_ID);

        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setKeyListener(null);
        if (placeholder != null) {
            editText.setHint(placeholder);
        }
        refresh(editText);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog(getContext(), editText);
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSelectionDialog(getContext(), editText);
                }
            }
        });

        return editText;
    }

    private void showSelectionDialog(final Context context, final EditText editText) {
        if (items == null) {
            assert(loadItemsTask.getStatus() != Status.FINISHED);
            loadItemsTask.runTaskOnFinished(new Runnable() {
                @Override
                public void run() {
                    showSelectionDialog(context, editText);
                }
            });

            if (loadingIndicator == null) {
                loadingIndicator = MessageUtil.newProgressIndicator("Getting required data", context);
                loadingIndicator.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadItemsTask.runTaskOnFinished(null);
                    }
                });
            }

            loadingIndicator.show();
        }
        else if (selectionDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select " + getLabel());

            View searchableList = LayoutInflater.from(context).inflate(R.layout.searchable_listview, null);
            final List<String> filteredItems = new ArrayList<String>(items);
            final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, filteredItems);

            final EditText searchField = (EditText)searchableList.findViewById(R.id.search_field);
            searchField.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
                    String textLowerCase = text.toLowerCase(Locale.getDefault());
                    filteredItems.clear();
                    if (s.length() > 0) {
                        for (String item : items) {
                            if (item.toLowerCase(Locale.getDefault()).startsWith(textLowerCase)) {
                                filteredItems.add(item);
                            }
                        }
                    } else {
                        filteredItems.addAll(items);
                    }

                    otherSelectionIsShowing = false;
                    if (isFreeFormTextAllowed
                            && !text.isEmpty()
                            && (filteredItems.size() != 1 || !filteredItems.get(0).equalsIgnoreCase(textLowerCase))) {
                        filteredItems.add(0, "Other (" + text + ")");
                        otherSelectionIsShowing = true;
                    }

                    itemsAdapter.notifyDataSetChanged();
                }
            });

            final ListView listView = (ListView)searchableList.findViewById(R.id.selection_list);
            listView.setAdapter(itemsAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selection;
                    if (otherSelectionIsShowing && position == 0) {
                        selection = searchField.getText().toString();
                    } else {
                        selection = itemsAdapter.getItem(position);
                    }
                    getModel().setValue(getName(), selection);
                    editText.setText(selection);
                    selectionDialog.dismiss();
                }
            });

            builder.setView(searchableList);
            builder.setInverseBackgroundForced(true);
            selectionDialog = builder.create();
            selectionDialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    selectionDialog = null;
                }
            });

            selectionDialog.show();
        }
    }

    private EditText getEditText() {
        return (EditText)getView().findViewById(EDIT_TEXT_ID);
    }

    private void refresh(EditText editText) {
        String value = (String)getModel().getValue(getName());
        editText.setText(value != null ? value : "");
    }

    @Override
    public void refresh() {
        refresh(getEditText());
    }

    private class LoadItemsTask extends AsyncTask<Void, Void, List<String>> {

        Runnable doneRunnable;

        @Override
        protected List<String> doInBackground(Void... params) {
            return dataSource.getItems();
        }

        @Override
        protected void onPostExecute(List<String> results) {
            if (loadingIndicator != null) {
                loadingIndicator.dismiss();
                loadingIndicator = null;
            }

            items = results;

            if (doneRunnable != null) {
                doneRunnable.run();
            }
        }

        protected void runTaskOnFinished(Runnable runnable) {
            doneRunnable = runnable;
        }
    }
}
