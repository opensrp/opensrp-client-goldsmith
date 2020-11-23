package org.smartregister.goldsmith;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.db.EventClient;
import org.smartregister.sync.ClientProcessorForJava;

import java.util.ArrayList;
import java.util.List;

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
        super.processClient(local, true);
        super.processClient(remote);

    }
}
