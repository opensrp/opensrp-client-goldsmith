package org.smartregister.goldsmith.interactor;

import org.smartregister.family.util.AppExecutors;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;

import java.util.List;
import java.util.Map;

public class ReportingInteractor implements GoldsmithReportingContract.Interactor {

    private ReportContract.Model model;
    protected AppExecutors appExecutors;

    public ReportingInteractor(ReportContract.Model model) {
        this.model = model;
        appExecutors = new AppExecutors();
    }

    @Override
    public void fetchIndicatorDailyTallies(GoldsmithReportingContract.InteractorCallback callBack) {
        Runnable runnable = () -> {
            List<Map<String, IndicatorTally>> dailyTallies = model.getIndicatorsDailyTallies();
            appExecutors.mainThread().execute(() -> {
                if (callBack != null) {
                    callBack.onTalliesFetched(dailyTallies);
                }
            });
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void scheduleDailyTallyJob() {
        // Ignored implementation because this is already handled on Login
    }
}
