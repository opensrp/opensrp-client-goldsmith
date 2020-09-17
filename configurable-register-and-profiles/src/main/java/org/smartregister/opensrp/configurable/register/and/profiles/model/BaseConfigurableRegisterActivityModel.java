package org.smartregister.opensrp.configurable.register.and.profiles.model;

import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.opensrp.configurable.register.and.profiles.util.Utils;
import org.smartregister.util.FormUtils;
import org.smartregister.view.contract.ConfigurableRegisterActivityContract;

import java.util.List;

import timber.log.Timber;

public abstract class BaseConfigurableRegisterActivityModel implements ConfigurableRegisterActivityContract.Model {

    private static FormUtils formUtils;

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        if (viewIdentifiers != null) {
            ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().registerViewConfigurations(viewIdentifiers);
        }
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        if (viewIdentifiers != null) {
            ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().unregisterViewConfiguration(viewIdentifiers);
        }
    }

    @Override
    public void saveLanguage(String language) {
        // To be handled
    }

    @Override
    public String getLocationId(String locationName) {
        return LocationHelper.getInstance().getOpenMrsLocationId(locationName);
    }

    @Override
    public String getInitials() {
        return org.smartregister.util.Utils.getUserInitials();
    }

    public void setFormUtils(FormUtils formUtils) {
        this.formUtils = formUtils;
    }

    protected FormUtils getFormUtils() {
        if (formUtils == null) {
            try {
                formUtils = FormUtils.getInstance(Utils.context().applicationContext());
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return formUtils;
    }
}
