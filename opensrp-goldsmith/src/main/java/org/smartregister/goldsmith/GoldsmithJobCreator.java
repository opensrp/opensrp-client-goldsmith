package org.smartregister.goldsmith;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

import org.smartregister.job.DocumentConfigurationServiceJob;
import org.smartregister.job.ExtendedSyncServiceJob;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.job.SyncServiceJob;
import org.smartregister.job.ValidateSyncDataServiceJob;
import org.smartregister.sync.intent.DocumentConfigurationIntentService;
import org.smartregister.tasking.job.RevealSyncSettingsServiceJob;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 05-10-2020.
 */
public class GoldsmithJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            /*case SyncServiceJob.TAG:
                return new SyncServiceJob(RevealSyncIntentService.class);
            case LocationTaskServiceJob.TAG:
                return new LocationTaskServiceJob();*/
            case RevealSyncSettingsServiceJob.TAG:
                return new RevealSyncSettingsServiceJob();
            case ExtendedSyncServiceJob.TAG:
                return new ExtendedSyncServiceJob();
            case PullUniqueIdsServiceJob.TAG:
                return new PullUniqueIdsServiceJob();
            case ValidateSyncDataServiceJob.TAG:
                return new ValidateSyncDataServiceJob();
            case DocumentConfigurationServiceJob.TAG:
                return new DocumentConfigurationServiceJob(DocumentConfigurationIntentService.class);
            default:
                Timber.w(tag + " is not declared in RevealJobCreator Job Creator");
                return null;
        }
    }
}
