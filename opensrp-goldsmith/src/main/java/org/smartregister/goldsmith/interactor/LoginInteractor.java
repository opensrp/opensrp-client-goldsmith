package org.smartregister.goldsmith.interactor;

import org.smartregister.goldsmith.contract.LoginJobScheduler;
import org.smartregister.goldsmith.contract.LoginJobScheduler;
import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.view.contract.BaseLoginContract;


/***
 * @author rkodev
 */
public class LoginInteractor extends BaseLoginInteractor implements BaseLoginContract.Interactor {

    /**
     * add all schedule jobs to the schedule instance to enable
     * job start at pin login
     */
    private LoginJobScheduler scheduler = new LoginJobSchedulerProvider();

    public LoginInteractor(BaseLoginContract.Presenter loginPresenter) {
        super(loginPresenter);
    }

    @Override
    protected void scheduleJobsPeriodically() {
        scheduler.scheduleJobsPeriodically();
    }

    @Override
    protected void scheduleJobsImmediately() {
        super.scheduleJobsImmediately();
        scheduler.scheduleJobsImmediately();
    }
}