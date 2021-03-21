package org.smartregister.goldsmith.configuration.chw_practitioner;

import org.smartregister.configuration.BottomNavigationOptions;
import org.smartregister.configuration.ConfigurableNavigationOptions;
import org.smartregister.configuration.ToolbarOptions;

public class ChwRegisterNavigationOptions implements ConfigurableNavigationOptions {
    @Override
    public ToolbarOptions getToolbarOptions() {
        return new CHWRegisterToolbarOptions();
    }

    @Override
    public BottomNavigationOptions getBottomNavigationOptions() {
        return new ChwRegisterBottomNavigationOptions();
    }
}
