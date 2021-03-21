package org.smartregister.goldsmith.configuration.chw_practitioner;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.smartregister.configuration.BottomNavigationOptions;
import org.smartregister.configuration.BottomNavigationOptionsModel;
import org.smartregister.goldsmith.R;

public class ChwRegisterBottomNavigationOptions implements BottomNavigationOptions {

    @Override
    public int getBottomNavigationLayoutId() {
        return R.id.bottom_navigation;
    }

    @Override
    public BottomNavigationOptionsModel getBottomNavigationOptionsModel() {
        return new ChwRegisterBottomNavigationOptionsModel();
    }

    @Override
    public int checkedItemId() {
        return R.id.action_my_chws;
    }

    private class ChwRegisterBottomNavigationOptionsModel implements BottomNavigationOptionsModel {

        @Override
        public void onBottomOptionSelection(Activity activity, @NonNull MenuItem menuItem) {

        }
    }
}
