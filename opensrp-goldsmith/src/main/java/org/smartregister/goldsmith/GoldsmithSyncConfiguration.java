package org.smartregister.goldsmith;

import com.google.common.collect.ImmutableList;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.goldsmith.activity.LoginActivity;
import org.smartregister.view.activity.BaseLoginActivity;

import java.util.List;

/**
 * Created by samuelgithengi on 10/19/18.
 */
public class GoldsmithSyncConfiguration extends SyncConfiguration {

    @Override
    public int getSyncMaxRetries() {
        return BuildConfig.MAX_SYNC_RETRIES;
    }

    @Override
    public SyncFilter getSyncFilterParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public String getSyncFilterValue() {

        return Utils.getSyncFilterValue();
    }

    @Override
    public int getUniqueIdSource() {
        return BuildConfig.OPENMRS_UNIQUE_ID_SOURCE;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_BATCH_SIZE;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE;
    }

    @Override
    public boolean isSyncSettings() {
        return BuildConfig.IS_SYNC_SETTINGS;
    }

    @Override
    public SyncFilter getSettingsSyncFilterParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return false;
    }

    @Override
    public boolean isSyncUsingPost() {
        return !BuildConfig.DEBUG;
    }

    @Override
    public List<String> getSynchronizedLocationTags() {
        return ImmutableList.of("MOH Jhpiego Facility Name", "Health Facility", "Facility");
    }

    @Override
    public String getTopAllowedLocationLevel() {
        return "District";
    }

    @Override
    public boolean runPlanEvaluationOnClientProcessing() {
        return true;
    }

    @Override
    public String getOauthClientId() {
        return BuildConfig.OAUTH_CLIENT_ID;
    }

    @Override
    public String getOauthClientSecret() {
        return BuildConfig.OAUTH_CLIENT_SECRET;
    }

    @Override
    public Class<? extends BaseLoginActivity> getAuthenticationActivity() {
        return LoginActivity.class;
    }
}
