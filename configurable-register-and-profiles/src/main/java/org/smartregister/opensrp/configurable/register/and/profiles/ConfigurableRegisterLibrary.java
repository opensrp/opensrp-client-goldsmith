package org.smartregister.opensrp.configurable.register.and.profiles;

import androidx.annotation.NonNull;

import org.smartregister.Context;
import org.smartregister.opensrp.configurable.register.and.profiles.pojo.ConfigurableRegisterConfiguration;

public class ConfigurableRegisterLibrary {
    private static ConfigurableRegisterLibrary instance;
    private final Context context;
    private ConfigurableRegisterConfiguration registerConfiguration;
    private int applicationVersion;
    private int databaseVersion;

    private ConfigurableRegisterLibrary(@NonNull Context context, @NonNull ConfigurableRegisterConfiguration registerConfiguration
            , int applicationVersion, int databaseVersion) {
        this.context = context;
        this.registerConfiguration = registerConfiguration;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public static void init(@NonNull Context context, @NonNull ConfigurableRegisterConfiguration registerConfiguration
            , int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ConfigurableRegisterLibrary(context, registerConfiguration, applicationVersion, databaseVersion);
        }
    }

    public static ConfigurableRegisterLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance does not exist!!! Call "
                    + ConfigurableRegisterLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class");
        }
        return instance;
    }

    @NonNull
    public Context context() {
        return context;
    }

    @NonNull
    public ConfigurableRegisterConfiguration getRegisterConfiguration() {
        return registerConfiguration;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

}
