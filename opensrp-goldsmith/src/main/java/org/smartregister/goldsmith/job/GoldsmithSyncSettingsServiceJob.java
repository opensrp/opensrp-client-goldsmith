package org.smartregister.goldsmith.job;

import android.content.Intent;

import androidx.annotation.NonNull;

import org.smartregister.AllConstants;
import org.smartregister.goldsmith.service.GoldsmithSettingsSyncIntentService;
import org.smartregister.job.SyncSettingsServiceJob;

public class GoldsmithSyncSettingsServiceJob extends SyncSettingsServiceJob {
    public static final String TAG = "GoldsmithSyncSettingsServiceJob";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent intent = new Intent(getApplicationContext(), GoldsmithSettingsSyncIntentService.class);
        getApplicationContext().startService(intent);
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Result.RESCHEDULE : Result.SUCCESS;
    }
}
