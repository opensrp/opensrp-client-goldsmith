package org.smartregister.opensrp.configurable.register.and.profiles.presenter;

import org.smartregister.configurableviews.model.Field;
import org.smartregister.view.contract.BaseRegisterFragmentContract;
import org.smartregister.view.contract.ConfigurableRegisterFragmentContract;
import org.smartregister.view.contract.IField;

import java.util.List;

public class ConfigurableRegisterFragmentPresenter implements BaseRegisterFragmentContract.Presenter {

    @Override
    public void updateSortAndFilter(List<IField> list, IField iField) {

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
