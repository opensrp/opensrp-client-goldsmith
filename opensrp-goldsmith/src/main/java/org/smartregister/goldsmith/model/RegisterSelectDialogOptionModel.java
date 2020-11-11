package org.smartregister.goldsmith.model;

import org.smartregister.goldsmith.domain.RegisterSelectDialogOption;
import org.smartregister.goldsmith.util.Constants;
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

    }
}
