package org.smartregister.goldsmith.contract;

public interface LoginJobScheduler {
    void scheduleJobsPeriodically();

    void scheduleJobsImmediately();

    long getFlexValue(int value);
}
