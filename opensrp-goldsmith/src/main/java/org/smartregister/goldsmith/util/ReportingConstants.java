package org.smartregister.goldsmith.util;

public class ReportingConstants {

    public static class ThirtyDayIndicatorKeys {
        public static String COUNT_TASKS_READY = "total_tasks_completed_last_30_days_READY";
        public static String COUNT_TASKS_COMPLETED = "total_tasks_completed_last_30_days_COMPLETED";
        public static String COUNT_TOTAL_PREGNANCIES_LAST_30_DAYS = "total_preg_last_30_days";
    }

    public static class ProgressTargets {
        public static float PREGNANCY_REGISTRATION_TARGET = 4; // Todo -> How can the supervisor set this?
    }
}
