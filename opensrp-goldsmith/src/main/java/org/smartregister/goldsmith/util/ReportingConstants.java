package org.smartregister.goldsmith.util;

public class ReportingConstants {

    public static class ThirtyDayIndicatorKeysConstants {
        public static String COUNT_TASKS_READY = "total_tasks_completed_last_30_days_READY";
        public static String COUNT_TASKS_COMPLETED = "total_tasks_completed_last_30_days_COMPLETED";
        public static String COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS = "total_preg_last_30_days";
        public static String COUNT_TOTAL_NEW_BORN_VISITS_LAST_30_DAYS = "total_newborn_visits_last_30_days";
    }


    // Todo -> Implement Sync settings and get this prior to indicator calculations
    public static class ProgressTargetsConstants {
        public static String INDICATOR_KEY = "indicator";
        public static String TARGET_KEY = "target";
        public static String INDICATOR_TARGETS_KEY = "indicator_targets";
        public static String THIRTY_DAY_INDICATOR_TARGETS_KEY = "Thirty Day Indicator Targets";
        public static String PREGNANCY_REGISTRATION_30_DAY_TARGET = "total_preg_last_30_days";
        public static String NEW_BORN_VISITS_30_DAY_TARGET = "total_newborn_visits_last_30_days";
    }
}