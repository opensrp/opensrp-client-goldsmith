package org.smartregister.goldsmith.activity;


import androidx.fragment.app.Fragment;

import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.family.model.BaseFamilyRegisterModel;
import org.smartregister.family.presenter.BaseFamilyRegisterPresenter;
import org.smartregister.goldsmith.fragment.ClientRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Map;

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

    @Override
    public void startFormActivity(String s, String s1, Map<String, String> map) {

    }
}
