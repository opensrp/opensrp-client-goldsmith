package org.smartregister.goldsmith.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.contract.GoldsmithReportingContract;
import org.smartregister.goldsmith.presenter.MyPerformanceFragmentPresenter;
import org.smartregister.goldsmith.reporting.GoldsmithReport;
import org.smartregister.goldsmith.util.ReportingConstants;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.util.ReportingUtil;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class ThirtyDayDashboardFragment extends Fragment implements GoldsmithReportingContract.View {

    private static GoldsmithReportingContract.Presenter presenter;
    private ViewGroup visualizationsViewGroup;
    private ProgressBar progressBar;
    private List<Map<String, IndicatorTally>> indicatorTallies;

    public ThirtyDayDashboardFragment() {
        // Required empty public constructor
    }

    public static ThirtyDayDashboardFragment newInstance() {
        return new ThirtyDayDashboardFragment();
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
    public void showProgressBar(boolean show) {
        if (progressBar != null)
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_thirty_day_dashboard, container, false);
        progressBar = rootView.findViewById(R.id.progress_bar);
        visualizationsViewGroup = rootView.findViewById(R.id.indicator_dashboard);
        getPresenter().fetchIndicatorDailyTallies();
        return rootView;
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
        updateTasksComplete(viewGroup);
        GoldsmithReport.showIndicatorVisualisations(viewGroup, indicatorTallies);
    }

    public List<Map<String, IndicatorTally>> getIndicatorTallies() {
        return this.indicatorTallies;
    }

    public void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies) {
        this.indicatorTallies = indicatorTallies;
    }

    public void updateTasksComplete(ViewGroup viewGroup) {
        TextView tvPercentComplete = viewGroup.findViewById(R.id.tv_percentage);
        TextView tvTasksCompleted = viewGroup.findViewById(R.id.tv_tasks_completed);
        float tasksCompletedCount = ReportingUtil.getLatestCountBasedOnDate(getIndicatorTallies(),
                ReportingConstants.ThirtyDayIndicatorKeys.COUNT_TASKS_COMPLETED);
        float pendingTasksCount = ReportingUtil.getLatestCountBasedOnDate(getIndicatorTallies(),
                ReportingConstants.ThirtyDayIndicatorKeys.COUNT_TASKS_READY);
        float totalTaskCount = tasksCompletedCount + pendingTasksCount;
        int percentage = (int) ((tasksCompletedCount / totalTaskCount) * 100);

        tvPercentComplete.setText(MessageFormat.format(viewGroup.getContext().getString(R.string.performance_completed_percentage), percentage));
        tvTasksCompleted.setText(MessageFormat.format(viewGroup.getContext().getString(R.string.performance_completed_fraction), tasksCompletedCount, totalTaskCount));
    }
}