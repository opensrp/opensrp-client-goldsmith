package org.smartregister.goldsmith.configuration.pnc;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.ActivityStarter;
import org.smartregister.tasking.TaskingLibrary;

public class PncRegisterActivityStarter implements ActivityStarter {
    @Override
    public void startProfileActivity(@NonNull Activity activity, @NonNull CommonPersonObjectClient commonPersonObjectClient) {
        // TODO (allan) : Implement navigation
    }

    @Override
    public void startMapActivity(@NonNull Activity contextActivity) {
        TaskingLibrary.getInstance().getTaskingLibraryConfiguration().startMapActivity(contextActivity, null, null);
    }
}
