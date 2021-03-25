package org.smartregister.goldsmith.presenter;

import org.smartregister.domain.Practitioner;
import org.smartregister.goldsmith.contract.ChwProfileContract;
import org.smartregister.goldsmith.interactor.ChwProfileInteractor;

import java.lang.ref.WeakReference;

public class ChwProfilePresenter implements ChwProfileContract.Presenter, ChwProfileContract.InteractorCallBack {
    private WeakReference<ChwProfileContract.View> view;
    private ChwProfileContract.Interactor interactor;
    private String identifier;

    public ChwProfilePresenter(ChwProfileContract.View view, String identifier) {
        this.view = new WeakReference<>(view);
        this.identifier = identifier;
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
        interactor.refreshProfileView(identifier, this);
    }

    @Override
    public void refreshProfileTopSection(Practitioner chw) {
        if (chw != null) {
            getView().setProfileName(chw.getName());
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
