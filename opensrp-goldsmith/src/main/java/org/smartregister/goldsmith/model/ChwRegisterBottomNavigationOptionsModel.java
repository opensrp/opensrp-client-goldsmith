package org.smartregister.goldsmith.model;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.smartregister.configuration.BottomNavigationOptionsModel;
import org.smartregister.goldsmith.util.Utils;

public class ChwRegisterBottomNavigationOptionsModel implements BottomNavigationOptionsModel {

    @Override
    public void onBottomOptionSelection(Activity activity, @NonNull MenuItem menuItem) {
        Utils.handleBottomNavigationSelection(activity, menuItem);
    }
}
