package org.smartregister.goldsmith.contract;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 15-02-2021.
 */
public class EventTaskIdProviderImpl implements EventTaskIdProvider {

    private String lastStartedTaskId;

    @Override
    public void setLastStartedTask(String lastStartedTask) {
        this.lastStartedTaskId = lastStartedTask;
    }

    @Override
    public String getLastStartedTask() {
        return lastStartedTaskId;
    }

    @Override
    public void clearLastStartedTask() {
        lastStartedTaskId = null;
    }
}
