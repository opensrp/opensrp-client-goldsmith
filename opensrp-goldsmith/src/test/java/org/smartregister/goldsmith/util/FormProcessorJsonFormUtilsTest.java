package org.smartregister.goldsmith.util;

import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.goldsmith.TestApplication;
import org.smartregister.goldsmith.contract.EventTaskIdProviderImpl;

import java.util.HashMap;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 15-02-2021.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, sdk = Build.VERSION_CODES.O_MR1)
public class FormProcessorJsonFormUtilsTest {

    @Test
    public void testAddTaskIdToEventShouldAddTaskIdWhenDetailsIsNull() {
        String taskId = "task-id";
        EventTaskIdProviderImpl eventTaskIdProvider = new EventTaskIdProviderImpl();
        eventTaskIdProvider.setLastStartedTask(taskId);

        ((TestApplication) TestApplication.getInstance())
                .setEventTaskIdProvider(eventTaskIdProvider);

        Event event = new Event();

        FormProcessorJsonFormUtils.addTaskIdToEvent(event);

        Assert.assertEquals(taskId, event.getDetails().get(Constants.EventDetails.TASK_ID));
    }

    @Test
    public void testAddTaskIdToEventShouldAddTaskIdWhenDetailsIsNotNull() {
        String taskId = "task-id";
        EventTaskIdProviderImpl eventTaskIdProvider = new EventTaskIdProviderImpl();
        eventTaskIdProvider.setLastStartedTask(taskId);

        ((TestApplication) TestApplication.getInstance())
                .setEventTaskIdProvider(eventTaskIdProvider);

        Event event = new Event();
        HashMap<String, String> details = new HashMap<>();
        details.put("db-version", "3");
        event.setDetails(details);

        FormProcessorJsonFormUtils.addTaskIdToEvent(event);

        Assert.assertEquals(taskId, event.getDetails().get(Constants.EventDetails.TASK_ID));
    }
}