package org.smartregister.goldsmith.activity;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsResult;

import org.smartregister.tasking.activity.TaskRegisterActivity;
import org.smartregister.tasking.model.BaseTaskDetails;

import java.util.Map;

import io.ona.kujaku.utils.Permissions;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 02-10-2020.
 */
public class GoldsmithTaskRegisterActivity extends TaskRegisterActivity implements ResultCallback<LocationSettingsResult> {

    @Override
    public void startFamilyRegistration(BaseTaskDetails baseTaskDetails) {
        // Do nothing
    }

    @Override
    public void startFormActivity(String s, String s1, Map<String, String> map) {

    }

    @Override
    public void onResume() {
        /*checkAndRequestLocationPermissions();
        LocationSettingsHelper.checkLocationEnabled(this, this);*/

        super.onResume();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent data) {
        super.onActivityResultExtended(requestCode, resultCode, data);
        
        if (requestCode == LoginActivity.LOCATION_PERMISSION_REQUEST_CODE) {
            checkAndRequestLocationPermissions();
        }
    }

    private void checkAndRequestLocationPermissions() {
        if (!Permissions.check(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permissions.request(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LoginActivity.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
