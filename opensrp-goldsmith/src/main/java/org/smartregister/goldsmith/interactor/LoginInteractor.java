package org.smartregister.goldsmith.interactor;

import org.smartregister.domain.LoginResponse;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.contract.LoginJobScheduler;
import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.view.contract.BaseLoginContract;

import java.lang.ref.WeakReference;

import timber.log.Timber;


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

    @Override
    public void loginWithLocalFlag(WeakReference<BaseLoginContract.View> view, boolean localLogin, String userName, char[] password) {
        // TODO: Remove this. This fixes a crash on the device. This error should be resolve once the CHW work is ready
        try {
            super.loginWithLocalFlag(view, localLogin, userName, password);
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}