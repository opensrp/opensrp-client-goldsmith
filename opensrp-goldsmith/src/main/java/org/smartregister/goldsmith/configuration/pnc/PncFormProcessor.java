package org.smartregister.goldsmith.configuration.pnc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.core.utils.CoreConstants;
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
import static org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.goldsmith.util.SampleAppJsonFormUtils.populateInjectedFields;
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
    public JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName,
                                    @NonNull String entityId, @NonNull String currentLocationId,
                                    @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        form.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        form.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);

        // Inject the field values
        JSONObject details = new JSONObject();
        details.put(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        form.put(DETAILS, details);

        if (injectedFieldValues != null && injectedFieldValues.size() > 0) {
            populateInjectedFields(form, injectedFieldValues);
        }

        return form;
    }

    @Override
    public HashMap<String, String> getInjectableFields() {
        // FORM KEY, CLIENT_OBJECT KEY
        HashMap<String, String> injectableFieldsMap = new HashMap<>();
        injectableFieldsMap.put(org.smartregister.goldsmith.util.Constants.Client.LAST_NAME, org.smartregister.goldsmith.util.Constants.Client.LAST_NAME);
        injectableFieldsMap.put(CoreConstants.JsonAssets.FAM_NAME, org.smartregister.goldsmith.util.Constants.Client.FIRST_NAME);
        injectableFieldsMap.put(org.smartregister.family.util.DBConstants.KEY.RELATIONAL_ID, BASE_ENTITY_ID);
        return injectableFieldsMap;
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> events, String formJsonString) {
        return false;
    }
}
