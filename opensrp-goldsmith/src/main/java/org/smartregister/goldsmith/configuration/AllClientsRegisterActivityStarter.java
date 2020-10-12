package org.smartregister.goldsmith.configuration;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.ActivityStarter;

public class AllClientsRegisterActivityStarter implements ActivityStarter {
    @Override
    public void startProfileActivity(@NonNull Activity contextActivity, @NonNull CommonPersonObjectClient commonPersonObjectClient) {
        // TODO -> Go to Family profile Activity
    }
}
