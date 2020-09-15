package org.smartregister.opensrp.configurable.register.and.profiles.model;

import org.json.JSONArray;
import org.smartregister.configurableviews.model.Field;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.domain.Response;
import org.smartregister.opensrp.configurable.register.and.profiles.contract.ConfigurableRegisterFragmentContract;

import java.util.List;
import java.util.Set;

public class ConfigurableRegisterFragmentModel implements ConfigurableRegisterFragmentContract.Model {

    @Override
    public ViewConfiguration getViewConfiguration(String viewConfigurationIdentifier) {
        return null;
    }

    @Override
    public Set<View> getRegisterActiveColumns(String viewConfigurationIdentifier) {
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
    public String getFilterText(List<Field> filterList, String filter) {
        return null;
    }

    @Override
    public String getSortText(Field sortField) {
        return null;
    }

    @Override
    public JSONArray getJsonArray(Response<String> response) {
        return null;
    }
}
