package org.smartregister.opensrp.configurable.register.and.profiles.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.db.EventClient;
import org.smartregister.opensrp.configurable.register.and.profiles.R;
import org.smartregister.opensrp.configurable.register.and.profiles.interactor.ConfigurableRegisterInteractor;
import org.smartregister.view.contract.RegisterParams;
import org.smartregister.view.contract.BaseRegisterContract;
import org.smartregister.view.contract.ConfigurableRegisterActivityContract;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public abstract class ConfigurableRegisterActivityPresenter implements BaseRegisterContract.Presenter, ConfigurableRegisterActivityContract.InteractorCallBack {

    protected WeakReference<BaseRegisterContract.View> viewWeakReference;
    protected ConfigurableRegisterActivityContract.Interactor interactor;
    protected ConfigurableRegisterActivityContract.Model model;

    public ConfigurableRegisterActivityPresenter(BaseRegisterContract.View view, ConfigurableRegisterActivityContract.Model model) {
        viewWeakReference = new WeakReference<>(view);
        interactor = createInteractor();
        this.model = model;
    }

    private BaseRegisterContract.View getView() {
        if (viewWeakReference != null) {
            return viewWeakReference.get();
        } else {
            return null;
        }
    }

    public void setModel(ConfigurableRegisterActivityContract.Model model) {
        this.model = model;
    }

    public void setInteractor(ConfigurableRegisterActivityContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void saveLanguage(String language) {
        model.saveLanguage(language);
        if (getView() != null)
            getView().displayToast(language + " selected");
    }

    @Override
    public void startForm(@NonNull String formName, @NonNull String entityId, String metadata, String currentLocationId) {
        if (StringUtils.isBlank(entityId)) {
            Triple<String, String, String> triple = Triple.of(formName, metadata, currentLocationId);
            interactor.getNextUniqueId(triple, this);
            return;
        }
        JSONObject form = null;
        try {
            form = model.getFormAsJson(formName, entityId, currentLocationId);
        } catch (JSONException je) {
            Timber.e(je);
        }
        if (getView() != null)
            getView().startFormActivity(form);
    }

    @Override
    public ConfigurableRegisterActivityContract.Interactor createInteractor() {
        //return new ConfigurableRegisterInteractor();
        return null;
    }

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        if (viewIdentifiers != null)
            model.registerViewConfigurations(viewIdentifiers);
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        if (viewIdentifiers != null)
            model.unregisterViewConfiguration(viewIdentifiers);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        viewWeakReference = null;
        if (!isChangingConfiguration) {
            model = null;
        }
    }

    @Override
    public void updateInitials() {
        String initials = model.getInitials();
        if (initials != null && getView() != null) {
            getView().updateInitialsText(initials);
        }
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
    }

    @Override
    public void onNoUniqueId() {
        if (getView() != null)
            getView().displayShortToast(R.string.no_unique_id);
    }

    @Override
    public void onRegistrationSaved(@NonNull RegisterParams registerParams, @Nullable List<EventClient> clientList) {

    }
}
