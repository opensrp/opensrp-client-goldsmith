package org.smartregister.goldsmith.reporting;

import android.view.View;
import android.view.ViewGroup;

import org.smartregister.goldsmith.R;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;
import org.smartregister.reporting.view.ProgressIndicatorView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.goldsmith.util.ReportingConstants.ProgressTargets.PREGNANCY_REGISTRATION_TARGET;
import static org.smartregister.goldsmith.util.ReportingConstants.ThirtyDayIndicatorKeys.COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS;

public class GoldsmithReport {
    static int defaultForegroundColor;
    static int defaultBackgroundColor;
    static int positiveColor;
    static int negativeColor;

    public static void showIndicatorVisualisations(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        defaultForegroundColor = mainLayout.getResources().getColor(R.color.progressbar_green);
        defaultBackgroundColor = mainLayout.getResources().getColor(R.color.progressbar_red);
        positiveColor = mainLayout.getResources().getColor(R.color.progressbar_green);
        negativeColor = mainLayout.getResources().getColor(R.color.progressbar_red);
        showTotalPregnanciesIndicator(mainLayout, indicatorTallies);
    }

    public static void showTotalPregnanciesIndicator(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        String indicatorLabel = mainLayout.getContext().getString(R.string.pregnancies_registered_last_30_label);
        int count = (int) ReportingUtil.getCount(ReportContract.IndicatorView.CountType.LATEST_COUNT, COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS, indicatorTallies);
        int percentage = getPercentage(count, PREGNANCY_REGISTRATION_TARGET);
        ProgressIndicatorDisplayOptions displayOptions = new ProgressIndicatorDisplayOptions.ProgressIndicatorBuilder()
                .withIndicatorLabel(indicatorLabel)
                .withProgressIndicatorTitle(getProgressIndicatorTitle(count, percentage))
                .withProgressIndicatorTitleColor(getTitleColor(percentage))
                .withProgressValue(percentage)
                .withProgressIndicatorSubtitle("")
                .withBackgroundColor(defaultBackgroundColor)
                .withForegroundColor(defaultForegroundColor)
                .build();

        appendView(mainLayout, new ProgressIndicatorView(mainLayout.getContext(), displayOptions));

    }

    public static int getPercentage(int count, float target) {
        return  (int) ((count / target) * 100) - 100;
    }

    public static String getProgressIndicatorTitle(int count, int percentage) {
        String signedPercent = percentage < 0 ? String.valueOf(percentage) : "+"+percentage;
        return MessageFormat.format("{0} ({1}%)", count, signedPercent);
    }

    public static int getTitleColor(int percentage) {
        return percentage >= 50 ? positiveColor : negativeColor;
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
