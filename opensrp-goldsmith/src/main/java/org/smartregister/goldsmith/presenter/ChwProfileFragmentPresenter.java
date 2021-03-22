package org.smartregister.goldsmith.presenter;

import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.interactor.ChwProfileFragmentInteractor;

public class ChwProfileFragmentPresenter extends MyPerformanceFragmentPresenter {

    public ChwProfileFragmentPresenter(GoldsmithReportingContract.View view, String identifier) {
        super(view);
        interactor = new ChwProfileFragmentInteractor(identifier);
    }

    @Override
    public void fetchIndicatorDailyTallies() {
        if (getView() != null)
            getView().showProgressBar(true);
        interactor.fetchIndicatorDailyTallies(this);
    }
}
