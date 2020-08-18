package org.smartregister.goldsmith.activity;


import androidx.fragment.app.Fragment;

import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

public class ClientRegisterActivity extends BaseFamilyRegisterActivity {

    @Override
    protected void initializePresenter() {

    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return null;
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }
}
