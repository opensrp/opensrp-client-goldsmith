package org.smartregister.goldsmith.activity;


import androidx.fragment.app.Fragment;

import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.family.model.BaseFamilyRegisterModel;
import org.smartregister.family.presenter.BaseFamilyRegisterPresenter;
import org.smartregister.goldsmith.fragment.ClientRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;

public class ClientRegisterActivity extends BaseFamilyRegisterActivity {

    @Override
    protected void registerBottomNavigation() {
        // TODO :: Add new layout
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseFamilyRegisterPresenter(this, new BaseFamilyRegisterModel());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new ClientRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }
}
