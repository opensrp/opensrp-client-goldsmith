package org.smartregister.goldsmith.fragment;

import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.MyPerformanceActivity;
import org.smartregister.goldsmith.util.ReportingConstants;
import org.smartregister.reporting.util.ReportingUtil;

import java.text.MessageFormat;

public class ThirtyDayPerformanceFragment extends PerformanceDashboardFragment {

    public static ThirtyDayPerformanceFragment newInstance() {
        return new ThirtyDayPerformanceFragment();
    }

    @Override
    public void buildVisualization(ViewGroup viewGroup) {
        updateTasksComplete();
        super.buildVisualization(viewGroup);
    }

    public void updateTasksComplete() {
        MyPerformanceActivity myPerformanceActivity = (MyPerformanceActivity) getActivity();
        TextView tvPercentComplete = myPerformanceActivity.findViewById(R.id.tv_percentage);
        TextView tvTasksCompleted = myPerformanceActivity.findViewById(R.id.tv_tasks_completed);

        float tasksCompletedCount = ReportingUtil.getLatestCountBasedOnDate(getIndicatorTallies(),
                ReportingConstants.ThirtyDayIndicatorKeysConstants.COUNT_TASKS_COMPLETED);
        float pendingTasksCount = ReportingUtil.getLatestCountBasedOnDate(getIndicatorTallies(),
                ReportingConstants.ThirtyDayIndicatorKeysConstants.COUNT_TASKS_READY);
        float totalTaskCount = tasksCompletedCount + pendingTasksCount;
        int percentage = (int) ((tasksCompletedCount / totalTaskCount) * 100);

        tvPercentComplete.setText(MessageFormat.format(rootView.getContext().getString(R.string.performance_completed_percentage), percentage));
        tvTasksCompleted.setText(MessageFormat.format(rootView.getContext().getString(R.string.performance_completed_fraction), tasksCompletedCount, totalTaskCount));
    }
}