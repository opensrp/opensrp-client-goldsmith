package org.smartregister.goldsmith.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.CoreLibrary;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.dao.ChwPractitionerDao;

import java.util.Map;

public class SupervisorPerformanceFragment extends PerformanceDashboardFragment {

    public static SupervisorPerformanceFragment newInstance() {
        return new SupervisorPerformanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.supervisor_performance_dashboard, container, false);
        progressBar = rootView.findViewById(R.id.progress_bar);
        TextView tvPercentage = rootView.findViewById(R.id.tv_percentage);

        String identifier = CoreLibrary.getInstance().context().allSharedPreferences().getUserPractitionerIdentifier();

        fetchIndicatorDailyTallies(tvPercentage, identifier);
        return rootView;
    }

    private void fetchIndicatorDailyTallies(TextView textView, String identifier) {
        GoldsmithApplication application = (GoldsmithApplication) GoldsmithApplication.getInstance();
        Runnable runnable = () -> application.getAppExecutors().mainThread().execute(() -> {
            Map<String, String> taskCompletionDetails = ChwPractitionerDao.getPractitionerTaskCompletion(identifier);
            updatePerformanceView(taskCompletionDetails, textView);
        });
        application.getAppExecutors().diskIO().execute(runnable);
    }

    private void updatePerformanceView(Map<String, String> taskCompletionDetails, TextView textView) {
        if (taskCompletionDetails != null) {
            int totalTaskCount = Integer.parseInt(taskCompletionDetails.get(ChwPractitionerDao.TASKS));
            int tasksCompletedCount = Integer.parseInt(taskCompletionDetails.get(ChwPractitionerDao.COMPLETED));
            int percentage = totalTaskCount > 0 ? ((tasksCompletedCount / totalTaskCount) * 100) : 0;
            textView.setText(String.valueOf(percentage));
        }
    }
}
