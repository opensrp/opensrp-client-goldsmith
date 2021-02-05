package org.smartregister.goldsmith.configuration.allfamilies;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.ActivityStarter;
import org.smartregister.tasking.TaskingLibrary;

public class AllFamiliesRegisterActivityStarter implements ActivityStarter {
    @Override
    public void startProfileActivity(@NonNull Activity contextActivity, @NonNull CommonPersonObjectClient commonPersonObjectClient) {
        // TODO -> Go to Family profile Activity
    }

    @Override
    public void startMapActivity(@NonNull Activity contextActivity) {
        TaskingLibrary.getInstance().getTaskingLibraryConfiguration().startMapActivity(contextActivity, null, null);
    }
}
