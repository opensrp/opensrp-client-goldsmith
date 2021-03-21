package org.smartregister.goldsmith.configuration.chw_practitioner;

import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.configuration.ToolbarOptions;
import org.smartregister.view.dialog.DialogOptionModel;

public class CHWToolbarOptions extends ToolbarOptions {

    @Override
    public int getLogoResourceId() {
        return R.drawable.ic_action_goldsmith_gold_placeholder_back;
    }

    @Override
    public boolean isFabEnabled() {
        return false;
    }

    @Override
    public boolean isNewToolbarEnabled() {
        return true; // TODO -> Disable some new toolbar options
    }

    @Override
    public DialogOptionModel getDialogOptionModel() {
        return null; // TODO -> Handle this
    }

}
