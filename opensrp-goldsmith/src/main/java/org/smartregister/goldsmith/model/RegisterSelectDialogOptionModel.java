package org.smartregister.goldsmith.model;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.goldsmith.domain.RegisterSelectDialogOption;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;
import org.smartregister.view.dialog.DialogOption;
import org.smartregister.view.dialog.DialogOptionModel;

public class RegisterSelectDialogOptionModel implements DialogOptionModel {

    @Override
    public DialogOption[] getDialogOptions() {
        // Should we retrieve these from the number of configurations?
        return new DialogOption[]{new RegisterSelectDialogOption(
                Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES),
                new RegisterSelectDialogOption(Constants.RegisterViewConstants.ModuleOptions.ANC),
                new RegisterSelectDialogOption(Constants.RegisterViewConstants.ModuleOptions.PNC)
        };
    }

    @Override
    public void onDialogOptionSelection(Activity activity, DialogOption option, Object tag) {
        String optionName = option.name();
        switch (optionName) {
            case Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES, activity);
                break;
            case Constants.RegisterViewConstants.ModuleOptions.ANC:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.ANC, activity);
                break;
            case Constants.RegisterViewConstants.ModuleOptions.PNC:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.PNC, activity);
                break;
            default:
                // Do nothing
                break;
        }
    }

    private void loadRegister(String moduleName, Activity activity) {
        CoreLibrary.getInstance().setCurrentModule(moduleName);
        Intent intent = new Intent(activity, BaseConfigurableRegisterActivity.class);
        intent.putExtra(AllConstants.IntentExtra.MODULE_NAME, moduleName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
        activity.finish();
    }
}
