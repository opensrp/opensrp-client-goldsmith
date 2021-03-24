package org.smartregister.goldsmith.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.presenter.MyPerformanceFragmentPresenter;
import org.smartregister.goldsmith.reporting.GoldsmithReportUtils;
import org.smartregister.reporting.domain.IndicatorTally;

import java.util.List;
import java.util.Map;

public class PerformanceDashboardFragment extends Fragment implements GoldsmithReportingContract.View {

    protected static GoldsmithReportingContract.Presenter presenter;
    protected View rootView;
    protected ViewGroup visualizationsViewGroup;
    protected ProgressBar progressBar;
    protected List<Map<String, IndicatorTally>> indicatorTallies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.performance_indicators_tasks_dashboard, container, false);
        progressBar = rootView.findViewById(R.id.progress_bar);
        visualizationsViewGroup = rootView.findViewById(R.id.health_indicators_layout);
        getPresenter().fetchIndicatorDailyTallies();
        return rootView;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (progressBar != null)
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public GoldsmithReportingContract.Presenter getPresenter() {
        if (presenter == null) {
            presenter = new MyPerformanceFragmentPresenter(this);
        }
        return presenter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter = null;
    }

    @Override
    public void refreshUI() {
        buildVisualization(visualizationsViewGroup);
    }

    @Override
    public void buildVisualization(ViewGroup viewGroup) {
        GoldsmithReportUtils.showIndicatorVisualisations(viewGroup, indicatorTallies, true);
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorTallies() {
        return this.indicatorTallies;
    }

    @Override
    public void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies) {
        this.indicatorTallies = indicatorTallies;
    }
}
