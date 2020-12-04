package org.smartregister.goldsmith;

import android.content.ContentValues;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.domain.Event;
import org.smartregister.domain.db.EventClient;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by samuelgithengi on 11/9/20.
 */
public class GoldsmithClientProcessor extends ClientProcessorForJava {
    public GoldsmithClientProcessor(Context context) {
        super(context);
    }

    public static ClientProcessorForJava getInstance(Context context) {
        if (instance == null) {
            instance = new GoldsmithClientProcessor(context);
        }
        return instance;
    }

    @Override
    public synchronized void processClient(List<EventClient> eventClientList) throws Exception {
        List<EventClient> local = new ArrayList<>();
        List<EventClient> remote = new ArrayList<>();
        for (EventClient eventClient : eventClientList) {
            if (StringUtils.isBlank(eventClient.getEvent().getEventId()) || eventClient.getEvent().getServerVersion() ==0L) {
                local.add(eventClient);
            } else {
                remote.add(eventClient);
            }
        }

        // TODO: Fix this issue at a better position
        // This is a pain for home visits which generate a lot of events
        for (EventClient eventClient: eventClientList) {
            Event event = eventClient.getEvent();
            Map<String, String> details = event.getDetails();

            if (!details.containsKey("planIdentifier")) {
                event.addDetails("planIdentifier", BuildConfig.PNC_PLAN_ID);
                JSONObject eventJson = DrishtiApplication.getInstance().getContext().getEventClientRepository().getEventsByFormSubmissionId(event.getFormSubmissionId());
                eventJson.getJSONObject("details").put("planIdentifier", BuildConfig.PNC_PLAN_ID);

                updateEvent(event.getFormSubmissionId(), eventJson);
            }
        }

        super.processClient(local, true);
        super.processClient(remote);

    }

    public int updateEvent(String formSubmissionId, JSONObject eventJson) {
        try {

            ContentValues values = new ContentValues();
            values.put(EventClientRepository.event_column.json.name(), eventJson.toString());

            return DrishtiApplication.getInstance().getRepository().getWritableDatabase()
                    .update(EventClientRepository.Table.event.name(),
                    values,
                    EventClientRepository.event_column.formSubmissionId.name() + " = ?",
                    new String[]{formSubmissionId});

        } catch (Exception e) {
            Timber.e(e);
        }

        return 0;
    }
}
