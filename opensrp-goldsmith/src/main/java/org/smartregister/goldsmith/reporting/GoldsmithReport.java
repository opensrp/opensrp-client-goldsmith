package org.smartregister.goldsmith.reporting;

import android.view.View;
import android.view.ViewGroup;

import org.smartregister.goldsmith.R;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;
import org.smartregister.reporting.view.ProgressIndicatorView;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.goldsmith.util.ReportingConstants.ProgressTargets.PREGNANCY_REGISTRATION_TARGET;
import static org.smartregister.goldsmith.util.ReportingConstants.ThirtyDayIndicatorKeys.COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS;

public class GoldsmithReport {
    static int defaultForegroundColor;
    static int defaultBackgroundColor;

    public static void showIndicatorVisualisations(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        defaultForegroundColor = mainLayout.getResources().getColor(R.color.progressbar_green);
        defaultBackgroundColor = mainLayout.getResources().getColor(R.color.progressbar_red);
        showTotalPregnanciesIndicator(mainLayout, indicatorTallies);
    }

    public static void showTotalPregnanciesIndicator(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        String title = mainLayout.getContext().getString(R.string.pregnancies_registered_last_30_label);
        int percentage = (int) ReportingUtil.getProgressPercentage(ReportContract.IndicatorView.CountType.TOTAL_COUNT,
                PREGNANCY_REGISTRATION_TARGET, COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS, indicatorTallies);
        ProgressIndicatorDisplayOptions displayOptions = new ProgressIndicatorDisplayOptions.ProgressIndicatorBuilder()
                .withTitle(title)
                .withProgressValue(percentage)
                .withSubtitle("")
                .withBackgroundColor(defaultBackgroundColor)
                .withForegroundColor(defaultForegroundColor)
                .build();

        appendView(mainLayout, new ProgressIndicatorView(mainLayout.getContext(), displayOptions));

    }

    /**
     * Generating a pie chart is memory intensive in lower end devices.
     * Allow @java.lang.OutOfMemoryError is recorded in some devices
     *
     * @return view
     */
    public static void appendView(ViewGroup parentView, ReportContract.IndicatorView indicatorView) {
        try {
            View view = indicatorView.createView();
            if (view != null)
                parentView.addView(view);
        } catch (OutOfMemoryError e) {
            Timber.e(e);
        }
    }

}
