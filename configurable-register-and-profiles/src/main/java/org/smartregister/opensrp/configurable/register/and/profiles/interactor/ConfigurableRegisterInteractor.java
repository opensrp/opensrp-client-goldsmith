package org.smartregister.opensrp.configurable.register.and.profiles.interactor;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.opensrp.configurable.register.and.profiles.ConfigurableRegisterLibrary;
import org.smartregister.opensrp.configurable.register.and.profiles.contract.ConfigurableRegisterActivityContract;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.AppExecutors;

public abstract class ConfigurableRegisterInteractor implements ConfigurableRegisterActivityContract.Interactor {

    protected AppExecutors appExecutors;

    public ConfigurableRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public ConfigurableRegisterInteractor() {
        appExecutors = new AppExecutors();
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // TODO -> To be implemented in the extending class?
    }

    @Override
    public void getNextUniqueId(Triple<String, String, String> triple, ConfigurableRegisterActivityContract.InteractorCallBack callBack) {
        // TODO -> To be implemented in the extending class?
    }
}
