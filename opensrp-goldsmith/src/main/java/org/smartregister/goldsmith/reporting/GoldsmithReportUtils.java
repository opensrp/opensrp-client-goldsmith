package org.smartregister.goldsmith.reporting;

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.util.ReportingConstants;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;
import org.smartregister.reporting.view.ProgressIndicatorView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.goldsmith.util.ReportingConstants.ThirtyDayIndicatorKeysConstants.COUNT_TOTAL_NEW_BORN_VISITS_LAST_30_DAYS;
import static org.smartregister.goldsmith.util.ReportingConstants.ThirtyDayIndicatorKeysConstants.COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS;

public class GoldsmithReportUtils {
    private static int defaultBackgroundColor;
    private static int positiveColor;
    private static int negativeColor;
    private static int inProgressColor;
    private static int pregRegistration30DayTarget = 1;
    private static int newBornVisits30DayTarget = 1;

    public static void showIndicatorVisualisations(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        defaultBackgroundColor = mainLayout.getResources().getColor(R.color.progressbar_grey);
        positiveColor = mainLayout.getResources().getColor(R.color.progressbar_green);
        negativeColor = mainLayout.getResources().getColor(R.color.progressbar_red);
        inProgressColor = mainLayout.getResources().getColor(R.color.progressbar_amber);
        initTargets();

        show30DayTotalPregnanciesIndicator(mainLayout, indicatorTallies);
        show30DayTotalNewBornVisits(mainLayout, indicatorTallies);

    }

    public static void show30DayTotalPregnanciesIndicator(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        String indicatorLabel = mainLayout.getContext().getString(R.string.pregnancies_registered_last_30_label);
        int count = (int) ReportingUtil.getCount(ReportContract.IndicatorView.CountType.LATEST_COUNT, COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS, indicatorTallies);
        int percentage = getPercentage(count, pregRegistration30DayTarget);
        int progressColor = getBarColor(percentage);
        ProgressIndicatorDisplayOptions displayOptions = getProgressIndicatorDisplayOptions(indicatorLabel, count, percentage, progressColor);

        appendView(mainLayout, new ProgressIndicatorView(mainLayout.getContext(), displayOptions));
    }

    public static void show30DayTotalNewBornVisits(ViewGroup mainLayout, List<Map<String, IndicatorTally>> indicatorTallies) {
        String indicatorLabel = mainLayout.getContext().getString(R.string.new_born_visits_last_30_label);
        int count = (int) ReportingUtil.getCount(ReportContract.IndicatorView.CountType.LATEST_COUNT, COUNT_TOTAL_NEW_BORN_VISITS_LAST_30_DAYS, indicatorTallies);
        int percentage = getPercentage(count, newBornVisits30DayTarget);
        int progressColor = getBarColor(percentage);
        ProgressIndicatorDisplayOptions displayOptions = getProgressIndicatorDisplayOptions(indicatorLabel, count, percentage, progressColor);

        appendView(mainLayout, new ProgressIndicatorView(mainLayout.getContext(), displayOptions));
    }

    public static ProgressIndicatorDisplayOptions getProgressIndicatorDisplayOptions(String label, int count, int percentage, int progressColor) {
        return new ProgressIndicatorDisplayOptions.ProgressIndicatorBuilder()
                .withIndicatorLabel(label)
                .withProgressIndicatorTitle(getProgressIndicatorTitle(count, percentage))
                .withProgressIndicatorTitleColor(progressColor)
                .withProgressValue(percentage)
                .withProgressIndicatorSubtitle("")
                .withBackgroundColor(defaultBackgroundColor)
                .withForegroundColor(progressColor)
                .build();
    }

    public static int getPercentage(int count, float target) {
        return (int) ((count / target) * 100);
    }

    public static String getProgressIndicatorTitle(int count, int percentage) {
        int progressDifference = percentage - 100;
        String signedValue = percentage <= 100 ? String.valueOf(progressDifference) : "+" + progressDifference;
        String format = count > 0 ? "{0} ({1}%)" : "{0}";
        return MessageFormat.format(format, count, signedValue);
    }

    public static int getBarColor(int percentage) {
        if (percentage >= 50) {
            return percentage >= 100 ? positiveColor : inProgressColor;
        }
        return negativeColor;
    }

    // TODO -> Call this only once when server settings are updated
    public static void initTargets() {
        try {
            JSONArray targetsArray = (JSONArray) GoldsmithApplication.getInstance().getServerConfigs().get(ReportingConstants.ProgressTargetsConstants.INDICATOR_TARGETS_KEY);
            if (targetsArray != null && targetsArray.length() > 0) {
                JSONObject thirtyDayTargetsObject;
                JSONArray thirtyDayTargetsArray;
                JSONObject targetObject;
                String key;
                int value;
                for (int i = 0; i < targetsArray.length(); i++) {
                    thirtyDayTargetsObject = ((JSONObject) targetsArray.get(i));
                    thirtyDayTargetsArray = thirtyDayTargetsObject.getJSONArray(ReportingConstants.ProgressTargetsConstants.THIRTY_DAY_INDICATOR_TARGETS_KEY);

                    for (int j = 0; j < thirtyDayTargetsArray.length(); j++) {
                        targetObject = (JSONObject) thirtyDayTargetsArray.get(j);
                        key = targetObject.getString(ReportingConstants.ProgressTargetsConstants.INDICATOR_KEY);
                        value = Integer.parseInt(targetObject.getString(ReportingConstants.ProgressTargetsConstants.TARGET_KEY));
                        if (ReportingConstants.ProgressTargetsConstants.PREGNANCY_REGISTRATION_30_DAY_TARGET.equals(key)) {
                            pregRegistration30DayTarget = value;
                        } else if (ReportingConstants.ProgressTargetsConstants.NEW_BORN_VISITS_30_DAY_TARGET.equals(key)) {
                            newBornVisits30DayTarget = value;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Timber.e(ex, "Problem initialising indicator targets");
        }
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
