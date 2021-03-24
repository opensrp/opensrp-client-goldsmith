package org.smartregister.goldsmith.configuration.allfamilies;

import org.smartregister.configuration.BottomNavigationOptions;
import org.smartregister.configuration.ConfigurableNavigationOptions;
import org.smartregister.configuration.ToolbarOptions;

public class AllFamiliesNavigationOptions implements ConfigurableNavigationOptions {
    @Override
    public ToolbarOptions getToolbarOptions() {
        return new org.smartregister.goldsmith.configuration.ToolbarOptions();
    }

    @Override
    public BottomNavigationOptions getBottomNavigationOptions() {
        return null;
    }
}
