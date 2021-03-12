package org.smartregister.goldsmith.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.pinlogin.PinLogger;
import org.smartregister.goldsmith.pinlogin.PinLoginUtil;
import org.smartregister.goldsmith.presenter.LoginPresenter;
import org.smartregister.task.SaveTeamLocationsTask;
import org.smartregister.util.Utils;
import org.smartregister.view.activity.BaseLoginActivity;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.kujaku.utils.Permissions;


public class LoginActivity extends BaseLoginActivity implements BaseLoginContract.View {
    /*public static final String TAG = BaseLoginActivity.class.getCanonicalName();
    private static final String WFH_CSV_PARSED = "WEIGHT_FOR_HEIGHT_CSV_PARSED";*/
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 983;

    private PinLogger pinLogger = PinLoginUtil.getPinLogger();
    private boolean startHomeRemotely;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.processViewCustomizations();

        if (hasPinLogin()) {
            pinLoginAttempt();
            return;
        }

        if (!mLoginPresenter.isUserLoggedOut()) {
            goToHome(false);
        }
    }

    private void pinLoginAttempt() {
        // if the user has pin
        /*if (mLoginPresenter.isUserLoggedOut()) {
            if (pinLogger.isPinSet()) {
                Intent intent = new Intent(this, PinLoginActivity.class);
                intent.putExtra(PinLoginActivity.DESTINATION_FRAGMENT, PinLoginFragment.TAG);
                startActivity(intent);
                finish();
            }
        } else {
            goToHome(false);
        }*/
    }

    private boolean hasPinLogin() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (hasPinLogin() && !pinLogger.isFirstAuthentication()) {
            menu.add("Reset Pin Login");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase("Reset Pin Login")) {
            pinLogger.resetPinLogin();
            this.recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initializePresenter() {
        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void goToHome(boolean remote) {
        ChwApplication.getInstance().processServerConfigs();
        if (remote) {
            Utils.startAsyncTask(new SaveTeamLocationsTask(), null);
            // processWeightForHeightZscoreCSV();
        }
        if (hasPinLogin()) {
            startPinHome(remote);
        } else {
            startHome(remote);
        }
    }

    private void startHome(boolean remote) {
        if (!Permissions.check(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startHomeRemotely = remote;
            Permissions.request(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, LaunchpadActivity.class);
            intent.putExtra(Constants.INTENT_KEY.IS_REMOTE_LOGIN, remote);
            startActivity(intent);
            finish();
        }
    }

    private void startPinHome(boolean remote) {
        if (remote)
            pinLogger.resetPinLogin();
        // TODO -> Complete this implementation
        /*if (pinLogger.isFirstAuthentication()) {
            EditText passwordEditText = findViewById(org.smartregister.R.id.login_password_edit_text);
            pinLogger.savePassword(passwordEditText.getText().toString());
        }

        if (pinLogger.isFirstAuthentication()) {
            Intent intent = new Intent(this, PinLoginActivity.class);
            intent.putExtra(PinLoginActivity.DESTINATION_FRAGMENT, ChooseLoginMethodFragment.TAG);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ChwApplication.getApplicationFlavor().launchChildClientsAtLogin() ?
                    ChildRegisterActivity.class : FamilyRegisterActivity.class);
            intent.putExtra(Constants.INTENT_KEY.IS_REMOTE_LOGIN, remote);
            startActivity(intent);
        }*/
    }

    /*private void processWeightForHeightZscoreCSV() {
        // To implement later
        AllSharedPreferences allSharedPreferences = ChwApplication.getInstance().getContext().allSharedPreferences();
        if (ChwApplication.getApplicationFlavor().hasChildSickForm() && !allSharedPreferences.getPreference(WFH_CSV_PARSED).equals("true")) {
            WeightForHeightIntentService.startParseWFHZScores(this);
            allSharedPreferences.savePreference(WFH_CSV_PARSED, "true");
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            startHome(startHomeRemotely);
        }
    }
}