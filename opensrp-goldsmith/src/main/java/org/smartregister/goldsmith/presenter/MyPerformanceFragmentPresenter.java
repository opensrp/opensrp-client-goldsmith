package org.smartregister.goldsmith.presenter;

import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.interactor.ReportingInteractor;
import org.smartregister.reporting.domain.BaseReportIndicatorsModel;
import org.smartregister.reporting.domain.IndicatorTally;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MyPerformanceFragmentPresenter implements GoldsmithReportingContract.Presenter, GoldsmithReportingContract.InteractorCallback {

    private WeakReference<GoldsmithReportingContract.View> viewWeakReference;
    private GoldsmithReportingContract.Interactor interactor;

    public MyPerformanceFragmentPresenter(GoldsmithReportingContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        interactor = new ReportingInteractor(new BaseReportIndicatorsModel());
    }

    @Override
    public void fetchIndicatorDailyTallies() {
        if (getView() != null)
            getView().showProgressBar(true);
        interactor.fetchIndicatorDailyTallies(this);
    }

    public GoldsmithReportingContract.View getView() {
        if (viewWeakReference != null) {
            return viewWeakReference.get();
        }
        return null;
    }

    @Override
    public void onTalliesFetched(List<Map<String, IndicatorTally>> dailyTallies) {
        if (getView() != null) {
            getView().setIndicatorTallies(dailyTallies);
            getView().refreshUI();
            getView().showProgressBar(false);
        }
    }
}
