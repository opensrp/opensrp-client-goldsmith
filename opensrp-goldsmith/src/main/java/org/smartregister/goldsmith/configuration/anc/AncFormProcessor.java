package org.smartregister.goldsmith.configuration.anc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.EventClient;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.goldsmith.util.SampleAppJsonFormUtils.populateInjectedFields;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;

public class AncFormProcessor implements ModuleFormProcessor {

    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String jsonString, @Nullable Intent data, @Nullable FormTag formTag) throws JSONException {

        JSONObject form = new JSONObject(jsonString);
        JSONArray fields = org.smartregister.util.JsonFormUtils.fields(form);
        JSONObject lmp = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.LAST_MENSTRUAL_PERIOD);
        boolean hasLmp = StringUtils.isNotBlank(lmp.optString(JsonFormUtils.VALUE));

        if (!hasLmp) {
            JSONObject eddJson = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.EDD);
            DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("dd-MM-yyyy");

            LocalDate lmpDate = dateTimeFormat.parseLocalDate(eddJson.optString(JsonFormUtils.VALUE)).plusDays(-280);
            lmp.put(JsonFormUtils.VALUE, dateTimeFormat.print(lmpDate));
        }

        AllSharedPreferences allSharedPreferences = ChwApplication.getInstance().getContext().allSharedPreferences();
        EventClient registrationEventClient = JsonFormUtils.processRegistrationForm(allSharedPreferences, jsonString, org.smartregister.chw.anc.util.Constants.TABLES.ANC_MEMBERS);

        // TODO: Implement a merge client that prioritises non-null values over empty or null values in other processors. !NOT THIS ONE
        // Some empty values might be the actual client updates
        if (registrationEventClient.getClient() != null) {
            Client originalClient = retrieveOriginalClient(registrationEventClient.getClient().getBaseEntityId());
            if (originalClient != null) {
                registrationEventClient.setClient(originalClient);
            }
        }

        HashMap<Client, List<Event>> clientEventHashMap = new HashMap<>();
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(registrationEventClient.getEvent());
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
        if (injectedFieldValues != null && injectedFieldValues.size() > 0) {
            populateInjectedFields(form, injectedFieldValues);
        }
        return form;
    }

    @Override
    public HashMap<String, String> getInjectableFields() {
        // FORM KEY, CLIENT_OBJECT KEY
        HashMap<String, String> injectableFieldsMap = new HashMap<>();
        injectableFieldsMap.put(org.smartregister.goldsmith.util.Constants.Client.PHONE_NUMBER, org.smartregister.goldsmith.util.Constants.Client.PHONE_NUMBER);
        injectableFieldsMap.put(org.smartregister.goldsmith.util.Constants.Client.LAST_NAME, org.smartregister.goldsmith.util.Constants.Client.LAST_NAME);
        injectableFieldsMap.put(DBConstants.KEY.LAST_MENSTRUAL_PERIOD, DBConstants.KEY.LAST_MENSTRUAL_PERIOD);
        injectableFieldsMap.put(org.smartregister.family.util.DBConstants.KEY.RELATIONAL_ID, BASE_ENTITY_ID);
        return injectableFieldsMap;
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> list, String s) {
        return false;
    }

    public Client retrieveOriginalClient(String baseEntityId) {
        ECSyncHelper ecSyncHelper = ECSyncHelper.getInstance(CoreLibrary.getInstance().context().applicationContext());
        JSONObject originalClientJsonObject = ecSyncHelper.getClient(baseEntityId);

        return org.smartregister.util.JsonFormUtils.gson.fromJson(originalClientJsonObject.toString(), Client.class);
    }
}
