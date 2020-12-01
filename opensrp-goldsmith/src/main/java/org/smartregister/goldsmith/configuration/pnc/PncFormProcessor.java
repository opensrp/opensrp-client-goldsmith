package org.smartregister.goldsmith.configuration.pnc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.EventClient;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.repository.AllSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.Properties.DETAILS;
import static org.smartregister.AllConstants.PLAN_IDENTIFIER;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;

public class PncFormProcessor implements ModuleFormProcessor {
    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String jsonString, @Nullable Intent data, @Nullable FormTag formTag) throws JSONException {

        AllSharedPreferences allSharedPreferences = ChwApplication.getInstance().getContext().allSharedPreferences();
        EventClient registrationEventClient = JsonFormUtils.processRegistrationForm(allSharedPreferences, jsonString, Constants.TABLES.PREGNANCY_OUTCOME);
        HashMap<Client, List<Event>> clientEventHashMap = new HashMap<>();
        ArrayList<Event> eventList = new ArrayList<>();
        Event pregnancyOutcomeEvent = registrationEventClient.getEvent();
        pregnancyOutcomeEvent.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        eventList.add(pregnancyOutcomeEvent);
        clientEventHashMap.put(registrationEventClient.getClient(), eventList);

        return clientEventHashMap;
    }

    @Override
    public JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String entityId, @NonNull String currentLocationId, @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        form.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        form.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);

        JSONObject details = new JSONObject();
        details.put(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        form.put(DETAILS, details);

        return form;
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> events, String formJsonString) {
        return false;
    }
}
