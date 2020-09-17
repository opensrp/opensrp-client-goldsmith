package org.smartregister.opensrp.configurable.register.and.profiles.model;

import org.json.JSONArray;
import org.smartregister.domain.Response;
import org.smartregister.view.contract.BaseRegisterFragmentContract;
import org.smartregister.view.contract.IField;
import org.smartregister.view.contract.IView;
import org.smartregister.view.contract.IViewConfiguration;

import java.util.List;
import java.util.Set;

public class ConfigurableRegisterFragmentModel implements BaseRegisterFragmentContract.Model {

    @Override
    public IViewConfiguration getViewConfiguration(String viewConfigurationIdentifier) {
        return null;
    }

    @Override
    public Set<IView> getRegisterActiveColumns(String viewConfigurationIdentifier) {
        return null;
    }

    @Override
    public String countSelect(String tableName, String mainCondition) {
        return null;
    }

    @Override
    public String mainSelect(String tableName, String mainCondition) {
        return null;
    }

    @Override
    public String getFilterText(List<IField> filterList, String filter) {
        return null;
    }

    @Override
    public String getSortText(IField sortField) {
        return null;
    }

    @Override
    public JSONArray getJsonArray(Response<String> response) {
        return null;
    }
}
