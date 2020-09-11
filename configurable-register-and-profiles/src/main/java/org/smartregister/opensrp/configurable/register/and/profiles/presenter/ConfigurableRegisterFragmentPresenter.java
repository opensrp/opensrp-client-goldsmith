package org.smartregister.opensrp.configurable.register.and.profiles.presenter;

import org.smartregister.configurableviews.model.Field;
import org.smartregister.opensrp.configurable.register.and.profiles.contract.ConfigurableRegisterFragmentContract;

import java.util.List;

public class ConfigurableRegisterFragmentPresenter implements ConfigurableRegisterFragmentContract.Presenter {
    @Override
    public void updateSortAndFilter(List<Field> filterList, Field sortField) {

    }

    @Override
    public String getMainCondition() {
        return null;
    }

    @Override
    public String getDefaultSortQuery() {
        return null;
    }

    @Override
    public String getQueryTable() {
        return null;
    }

    @Override
    public void processViewConfigurations() {

    }

    @Override
    public void initializeQueries(String s) {

    }

    @Override
    public void startSync() {

    }

    @Override
    public void searchGlobally(String s) {

    }
}
