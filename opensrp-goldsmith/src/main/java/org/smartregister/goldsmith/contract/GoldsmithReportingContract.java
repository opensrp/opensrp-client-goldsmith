package org.smartregister.goldsmith.contract;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;

import java.util.List;
import java.util.Map;

public interface GoldsmithReportingContract {

    interface View extends ReportContract.View {
        Presenter getPresenter();

        void showProgressBar(boolean show);
    }

    interface Presenter {
        void fetchIndicatorDailyTallies();
    }

    interface Interactor extends ReportContract.Interactor {
        void fetchIndicatorDailyTallies(GoldsmithReportingContract.InteractorCallback callback);
    }

    interface InteractorCallback {
        void onTalliesFetched(List<Map<String, IndicatorTally>> dailyTallies);
    }
}
