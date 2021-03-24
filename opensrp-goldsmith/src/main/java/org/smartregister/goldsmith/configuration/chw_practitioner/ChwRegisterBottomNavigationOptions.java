package org.smartregister.goldsmith.configuration.chw_practitioner;

import org.smartregister.configuration.BottomNavigationOptions;
import org.smartregister.configuration.BottomNavigationOptionsModel;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.model.ChwRegisterBottomNavigationOptionsModel;

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
}
