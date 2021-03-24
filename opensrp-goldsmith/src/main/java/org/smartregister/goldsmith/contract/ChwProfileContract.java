package org.smartregister.goldsmith.contract;

import org.smartregister.domain.Practitioner;
import org.smartregister.view.contract.BaseProfileContract;

public interface ChwProfileContract {

    interface View extends BaseProfileContract.View {

        void setProfileName(String fullName);

        void setChwDistance(String distance);

        void setChwAddress(String address);

        void setHouseholdNumber(String households);

        void setLastSyncDay(String lastSyncDay);

        ChwProfileContract.Presenter presenter();
    }

    interface Presenter extends BaseProfileContract.Presenter {

        ChwProfileContract.View getView();

        void fetchProfileData();

    }

    interface Interactor {

        void refreshProfileView(String baseEntityId, ChwProfileContract.InteractorCallBack callback);
    }

    interface InteractorCallBack {
        void refreshProfileTopSection(Practitioner chw);
    }

}
