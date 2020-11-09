package org.smartregister.goldsmith.configuration;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.goldsmith.util.SampleAppJsonFormUtils;

import java.util.HashMap;
import java.util.List;

public class AncFormProcessor implements ModuleFormProcessor {
    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String s, @Nullable Intent intent, @Nullable FormTag formTag) throws JSONException {
        return null;
    }

    @Override
    public JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String entityId,
                                    @NonNull String currentLocationId, @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        return SampleAppJsonFormUtils.getFormAsJson(form, formName, entityId, currentLocationId, injectedFieldValues);
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> list, String s) {
        return false;
    }
}
