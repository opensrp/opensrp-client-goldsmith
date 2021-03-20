package org.smartregister.goldsmith.interactor;

import org.smartregister.goldsmith.contract.ChwProfileContract;
import org.smartregister.goldsmith.dao.ChwDao;
import org.smartregister.goldsmith.domain.ChwPerson;
import org.smartregister.util.AppExecutors;

public class ChwProfileInteractor implements ChwProfileContract.Interactor {

    protected AppExecutors appExecutors;

    public ChwProfileInteractor(AppExecutors executors) {
        this.appExecutors = executors;
    }

    public ChwProfileInteractor() {
        new AppExecutors();
    }


    @Override
    public void refreshProfileView(String baseEntityId, ChwProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> {
            ChwPerson chwClient = ChwDao.getChw(baseEntityId);
            appExecutors.mainThread().execute(() -> callback.refreshProfileTopSection(chwClient));
        };
        appExecutors.diskIO().execute(runnable);
    }
}
