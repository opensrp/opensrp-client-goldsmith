package org.smartregister.goldsmith.configuration;

import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.model.RegisterSelectDialogOptionModel;
import org.smartregister.view.dialog.DialogOptionModel;

public class ToolbarOptions implements org.smartregister.configuration.ToolbarOptions {

    private DialogOptionModel dialogOptionModel;

    @Override
    public int getLogoResourceId() {
        return R.drawable.ic_action_goldsmith_gold_placeholder_back;
    }

    @Override
    public int getFabTextStringResource() {
        return 0;
    }

    @Override
    public boolean isFabEnabled() {
        return true;
    }

    @Override
    public boolean isNewToolbarEnabled() {
        return true;
    }

    @Override
    public DialogOptionModel getDialogOptionModel() {
        if (dialogOptionModel == null) {
            dialogOptionModel = new RegisterSelectDialogOptionModel();
        }
        return dialogOptionModel;
    }
}
