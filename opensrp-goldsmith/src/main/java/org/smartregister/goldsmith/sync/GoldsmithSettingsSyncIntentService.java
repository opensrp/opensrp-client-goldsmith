package org.smartregister.goldsmith.sync;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.smartregister.AllConstants;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.sync.intent.SettingsSyncIntentService;

public class GoldsmithSettingsSyncIntentService extends SettingsSyncIntentService {

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        super.onHandleIntent(intent);
        Bundle data = intent.getExtras();
        if (data != null && data.getInt(AllConstants.INTENT_KEY.SYNC_TOTAL_RECORDS, 0) > 0) {
            ChwApplication.getInstance().processServerConfigs();
            // broadcast sync event
            Intent targetsSyncedIntent = new Intent(CoreConstants.ACTION.REPORTING_TARGETS_SYNCED);
            targetsSyncedIntent.putExtra(Constants.SyncConstants.UPDATE_REPORTING_INDICATORS, true);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(targetsSyncedIntent);
        }
    }
}
