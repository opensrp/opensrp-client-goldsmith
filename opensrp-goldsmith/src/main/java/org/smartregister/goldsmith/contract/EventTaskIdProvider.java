package org.smartregister.goldsmith.contract;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 15-02-2021.
 */
public interface EventTaskIdProvider {

    void setLastStartedTask(String lastStartedTask);

    String getLastStartedTask();

    void clearLastStartedTask();
}
