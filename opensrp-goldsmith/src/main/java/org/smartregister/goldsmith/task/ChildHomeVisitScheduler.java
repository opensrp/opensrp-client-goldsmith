package org.smartregister.goldsmith.task;

import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.contract.ScheduleTask;
import org.smartregister.chw.core.domain.BaseScheduleTask;
import org.smartregister.chw.core.rule.HomeAlertRule;
import org.smartregister.chw.core.utils.ChildHomeVisit;
import org.smartregister.chw.core.utils.CoreChildUtils;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.dao.PersonDao;
import org.smartregister.goldsmith.util.Constants;

import java.util.Date;
import java.util.List;

public class ChildHomeVisitScheduler extends BaseTaskExecutor {
    @Override
    public void resetSchedule(String baseEntityID, String scheduleName) {
        super.resetSchedule(baseEntityID, scheduleName);
        GoldsmithApplication.getInstance().getScheduleRepository().deleteScheduleByGroup(getScheduleGroup(), baseEntityID);
    }

    @Override
    public List<ScheduleTask> generateTasks(String baseEntityID, String eventName, Date eventDate) {
        // recompute the home visit task for this child
        BaseScheduleTask baseScheduleTask = prepareNewTaskObject(baseEntityID);

        ChildHomeVisit childHomeVisit = CoreChildUtils.getLastHomeVisit(Constants.TABLE_NAME.CHILD, baseEntityID);
        String yearOfBirth = PersonDao.getDob(baseEntityID);

        HomeAlertRule alertRule = new HomeAlertRule(
                GoldsmithApplication.getInstance().getApplicationContext(), yearOfBirth, childHomeVisit.getLastHomeVisitDate(), childHomeVisit.getVisitNotDoneDate(), childHomeVisit.getDateCreated());
        CoreChwApplication.getInstance().getRulesEngineHelper().getButtonAlertStatus(alertRule, CoreConstants.RULE_FILE.HOME_VISIT);


        baseScheduleTask.setScheduleDueDate(alertRule.getDueDate());
        baseScheduleTask.setScheduleNotDoneDate(alertRule.getNotDoneDate());
        baseScheduleTask.setScheduleExpiryDate(alertRule.getExpiryDate());
        baseScheduleTask.setScheduleCompletionDate(alertRule.getCompletionDate());
        baseScheduleTask.setScheduleOverDueDate(alertRule.getOverDueDate());

        return toScheduleList(baseScheduleTask);
    }

    @Override
    public String getScheduleName() {
        return CoreConstants.SCHEDULE_TYPES.CHILD_VISIT;
    }

    @Override
    public String getScheduleGroup() {
        return CoreConstants.SCHEDULE_GROUPS.HOME_VISIT;
    }

}
