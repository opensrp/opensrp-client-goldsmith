package org.smartregister.goldsmith.presenter;

import org.smartregister.goldsmith.contract.ChwProfileContract;
import org.smartregister.goldsmith.domain.ChwPerson;
import org.smartregister.goldsmith.interactor.ChwProfileInteractor;

import java.lang.ref.WeakReference;

public class ChwProfilePresenter implements ChwProfileContract.Presenter, ChwProfileContract.InteractorCallBack {
    private WeakReference<ChwProfileContract.View> view;
    private ChwProfileContract.Interactor interactor;
    private String baseEntityId;

    public ChwProfilePresenter(ChwProfileContract.View view, String baseEntityId) {
        this.view = new WeakReference<>(view);
        this.baseEntityId = baseEntityId;
        this.interactor = new ChwProfileInteractor();
    }

    @Override
    public ChwProfileContract.View getView() {
        if (view != null) {
            return view.get();
        } else {
            return null;
        }
    }

    @Override
    public void fetchProfileData() {
        interactor.refreshProfileView(baseEntityId, this);
    }

    @Override
    public void refreshProfileTopSection(ChwPerson chw) {
        if (chw != null) {
            getView().setProfileName(chw.getFullName());
        }
    }


    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        view = null;//set to null on destroy

        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
        }
    }
}
