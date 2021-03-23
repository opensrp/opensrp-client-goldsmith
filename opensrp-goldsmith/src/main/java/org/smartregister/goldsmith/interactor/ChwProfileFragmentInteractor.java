package org.smartregister.goldsmith.interactor;

import org.smartregister.family.util.AppExecutors;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.dao.ChwPractitionerDao;
import org.smartregister.reporting.domain.IndicatorTally;

import java.util.List;
import java.util.Map;

public class ChwProfileFragmentInteractor implements GoldsmithReportingContract.Interactor {

    private AppExecutors appExecutors;
    private String identifier;

    public ChwProfileFragmentInteractor(String identifier) {
        this.identifier = identifier;
        this.appExecutors = new AppExecutors();
    }

    @Override
    public void fetchIndicatorDailyTallies(GoldsmithReportingContract.InteractorCallback callback) {
        Runnable runnable = () -> {
            List<Map<String, IndicatorTally>> dailyTallies = ChwPractitionerDao.getPregnanciesRegisteredLast30Days(identifier);
            List<Map<String, IndicatorTally>> newBornTallies = ChwPractitionerDao.getNewBornVisitsLast30Days(identifier);
            if (dailyTallies != null && newBornTallies != null) {
                dailyTallies.addAll(newBornTallies);
            }

            appExecutors.mainThread().execute(() -> {
                if (callback != null) {
                    callback.onTalliesFetched(dailyTallies);
                }
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void scheduleDailyTallyJob() {
        // Do nothing
    }
}
