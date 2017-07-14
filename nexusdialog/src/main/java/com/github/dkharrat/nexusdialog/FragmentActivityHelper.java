package com.github.dkharrat.nexusdialog;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FragmentActivityHelper {

    /**
     * Returns a retained Fragment that stores the FormModel. The Fragment is associated with the specified <code>FragmentActivity</code>.
     * If the Fragment does not exist yet, an instance will be created and added to the <code>FragmentActivity</code>
     *
     * @param enclosing the <code>FragmentActivity</code> that stores the Fragment
     */
    public static FormModelFragment getFormModelFragment(FragmentActivity enclosing) {
        // find the retained fragment on activity restarts
        FragmentManager fm = enclosing.getSupportFragmentManager();
        FormModelFragment formModelFragment = (FormModelFragment) fm.findFragmentByTag(FormModelFragment.TAG);

        if (formModelFragment == null) {
            // create the retained fragment and data the first time
            formModelFragment = new FormModelFragment();
            fm.beginTransaction().add(formModelFragment, FormModelFragment.TAG).commit();
            formModelFragment.setModel(new MapFormModel());
        }

        return formModelFragment;
    }

}
