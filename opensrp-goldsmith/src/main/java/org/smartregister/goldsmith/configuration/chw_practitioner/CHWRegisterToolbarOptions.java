package org.smartregister.goldsmith.configuration.chw_practitioner;

import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.configuration.ToolbarOptions;
import org.smartregister.view.dialog.DialogOptionModel;

import java.util.Arrays;
import java.util.List;

public class CHWRegisterToolbarOptions extends ToolbarOptions {

    @Override
    public int getLogoResourceId() {
        return R.drawable.ic_action_goldsmith_gold_placeholder_back;
    }

    @Override
    public int getToolBarColor() {
        return R.color.supervisor_scheme_blue;
    }

    @Override
    public List<Integer> getHiddenToolOptions() {
        return Arrays.asList(R.id.select_register_layout);
    }

    @Override
    public boolean isFabEnabled() {
        return false;
    }

    @Override
    public boolean isNewToolbarEnabled() {
        return true;
    }

    @Override
    public DialogOptionModel getDialogOptionModel() {
        return null;
    }

}
