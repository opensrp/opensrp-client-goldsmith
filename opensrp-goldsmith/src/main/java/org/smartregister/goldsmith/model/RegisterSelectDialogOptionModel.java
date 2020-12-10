package org.smartregister.goldsmith.model;

import android.content.Context;
import android.content.Intent;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.goldsmith.ChwApplication;
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
    public void onDialogOptionSelection(DialogOption option, Object tag) {
        String optionName = option.name();
        switch (optionName) {
            case Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES);
                break;
            case Constants.RegisterViewConstants.ModuleOptions.ANC:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.ANC);
                break;
            case Constants.RegisterViewConstants.ModuleOptions.PNC:
                loadRegister(Constants.RegisterViewConstants.ModuleOptions.PNC);
                break;
            default:
                // Do nothing
                break;
        }
    }

    private void loadRegister(String moduleName) {
        Context context = ChwApplication.getInstance().getApplicationContext();
        CoreLibrary.getInstance().setCurrentModule(moduleName);
        Intent intent = new Intent(context, BaseConfigurableRegisterActivity.class);
        intent.putExtra(AllConstants.IntentExtra.MODULE_NAME, moduleName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }
}
