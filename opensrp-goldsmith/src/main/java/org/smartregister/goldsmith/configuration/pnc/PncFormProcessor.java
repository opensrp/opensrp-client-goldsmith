package org.smartregister.goldsmith.configuration.pnc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;

import java.util.HashMap;
import java.util.List;

import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.goldsmith.util.SampleAppJsonFormUtils.populateInjectedFields;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;

public class PncFormProcessor implements ModuleFormProcessor {
    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String jsonString, @Nullable Intent data, @Nullable FormTag formTag) throws JSONException {
        return null;
    }

    @Override
    public JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String entityId, @NonNull String currentLocationId, @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        form.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        form.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);

        // Inject the field values
        if (injectedFieldValues != null && injectedFieldValues.size() > 0) {
            populateInjectedFields(form, injectedFieldValues);
        }

        return form;
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> events, String formJsonString) {
        return false;
    }
}
