package org.smartregister.goldsmith.configuration;

import org.smartregister.configuration.BottomNavigationOptions;
import org.smartregister.configuration.ConfigurableNavigationOptions;
import org.smartregister.configuration.ToolbarOptions;

public class AncPncNavigationOptions implements ConfigurableNavigationOptions {
    @Override
    public ToolbarOptions getToolbarOptions() {
        return new AncPncToolbarOptions();
    }

    @Override
    public BottomNavigationOptions getBottomNavigationOptions() {
        return null;
    }

    private class AncPncToolbarOptions extends org.smartregister.goldsmith.configuration.ToolbarOptions {
        @Override
        public boolean isFabEnabled() {
            return false;
        }
    }
}
