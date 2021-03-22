package org.smartregister.goldsmith.fragment;

import android.os.Bundle;

import org.smartregister.AllConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.presenter.ChwProfileFragmentPresenter;

public class ChwProfileFragment extends PerformanceDashboardFragment {

    public static ChwProfileFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        ChwProfileFragment fragment = new ChwProfileFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public GoldsmithReportingContract.Presenter getPresenter() {
        if (presenter == null) {
            CommonPersonObjectClient client = (CommonPersonObjectClient) getArguments().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
            presenter = new ChwProfileFragmentPresenter(this, client.getDetails().get("identifier"));
        }
        return presenter;
    }
}
