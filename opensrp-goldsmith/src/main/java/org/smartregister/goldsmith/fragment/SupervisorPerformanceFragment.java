package org.smartregister.goldsmith.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.goldsmith.R;

public class SupervisorPerformanceFragment extends PerformanceDashboardFragment {

    public static SupervisorPerformanceFragment newInstance() {
        return new SupervisorPerformanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.supervisor_performance_dashboard, container, false);
        progressBar = rootView.findViewById(R.id.progress_bar);
        visualizationsViewGroup = rootView.findViewById(R.id.tasks_completion_layout);
        // getPresenter().fetchIndicatorDailyTallies(); // TODO -> Enable this
        return rootView;
    }
}
