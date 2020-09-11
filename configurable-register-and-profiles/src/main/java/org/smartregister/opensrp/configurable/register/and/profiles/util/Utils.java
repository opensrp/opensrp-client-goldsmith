package org.smartregister.opensrp.configurable.register.and.profiles.util;

import org.smartregister.Context;
import org.smartregister.opensrp.configurable.register.and.profiles.ConfigurableRegisterLibrary;

public class Utils {
    public static Context context() {
        return ConfigurableRegisterLibrary.getInstance().context();
    }
}
