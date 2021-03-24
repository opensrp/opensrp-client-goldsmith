package org.smartregister.goldsmith.fragment;

import android.os.Bundle;
import android.view.ViewGroup;

import org.smartregister.AllConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.presenter.ChwProfileFragmentPresenter;
import org.smartregister.goldsmith.reporting.GoldsmithReportUtils;

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
            String identifier = client.getDetails().get("identifier");
            presenter = new ChwProfileFragmentPresenter(this, identifier);
        }
        return presenter;
    }

    @Override
    public void buildVisualization(ViewGroup viewGroup) {
        GoldsmithReportUtils.showIndicatorVisualisations(viewGroup, indicatorTallies, false);
    }
}
