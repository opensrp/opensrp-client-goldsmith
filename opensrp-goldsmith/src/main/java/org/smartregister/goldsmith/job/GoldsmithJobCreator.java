package org.smartregister.goldsmith.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import org.smartregister.job.DocumentConfigurationServiceJob;
import org.smartregister.job.ExtendedSyncServiceJob;
import org.smartregister.job.PlanPeriodicEvaluationJob;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.job.SyncServiceJob;
import org.smartregister.job.ValidateSyncDataServiceJob;
import org.smartregister.reporting.job.RecurringIndicatorGeneratingJob;
import org.smartregister.sync.intent.DocumentConfigurationIntentService;
import org.smartregister.sync.intent.SyncIntentService;
import org.smartregister.tasking.job.TaskingSyncSettingsServiceJob;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 05-10-2020.
 */
public class GoldsmithJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncServiceJob.TAG:
                return new SyncServiceJob(SyncIntentService.class);
            case GoldsmithSyncSettingsServiceJob.TAG:
                return new GoldsmithSyncSettingsServiceJob();
            case LocationTaskServiceJob.TAG:
                return new LocationTaskServiceJob();
            case TaskingSyncSettingsServiceJob.TAG:
                return new TaskingSyncSettingsServiceJob();
            case ExtendedSyncServiceJob.TAG:
                return new ExtendedSyncServiceJob();
            case PullUniqueIdsServiceJob.TAG:
                return new PullUniqueIdsServiceJob();
            case ValidateSyncDataServiceJob.TAG:
                return new ValidateSyncDataServiceJob();
            case DocumentConfigurationServiceJob.TAG:
                return new DocumentConfigurationServiceJob(DocumentConfigurationIntentService.class);
            case RecurringIndicatorGeneratingJob.TAG:
                return new RecurringIndicatorGeneratingJob();
            default:
                if (PlanPeriodicEvaluationJob.isPlanPeriodEvaluationJob(tag)) {
                    return new PlanPeriodicEvaluationJob();
                }

                Timber.w(tag + " is not declared in Goldsmith Job Creator");
                return null;
        }
    }
}
