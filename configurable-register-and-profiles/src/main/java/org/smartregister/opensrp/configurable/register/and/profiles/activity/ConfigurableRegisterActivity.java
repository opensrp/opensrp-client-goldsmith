package org.smartregister.opensrp.configurable.register.and.profiles.activity;


import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.contract.ConfigurableRegisterActivityContract;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.List;

public class ConfigurableRegisterActivity extends BaseRegisterActivity implements ConfigurableRegisterActivityContract.View {


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

    @Override
    public void startFormActivity(String s, String s1, String s2) {

    }

    @Override
    public void startFormActivity(JSONObject jsonObject) {

    }

    @Override
    protected void onActivityResultExtended(int i, int i1, Intent intent) {

    }

    @Override
    public List<String> getViewIdentifiers() {
        return null;
    }

    @Override
    public void startRegistration() {

    }

    @Override
    public ConfigurableRegisterActivityContract.Presenter presenter() {
        return null;
    }
}