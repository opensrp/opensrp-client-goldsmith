package org.smartregister.goldsmith.configuration.pnc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.EventClient;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.util.PncRegistrationUtils;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.FormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.Properties.DETAILS;
import static org.smartregister.AllConstants.PLAN_IDENTIFIER;
import static org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.goldsmith.util.FormProcessorJsonFormUtils.populateInjectedFields;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;

public class PncFormProcessor implements ModuleFormProcessor {
    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String jsonString, @Nullable Intent data, @Nullable FormTag formTag) throws JSONException {

        AllSharedPreferences allSharedPreferences = GoldsmithApplication.getInstance().getContext().allSharedPreferences();
        EventClient pncRegistrationEventClient = JsonFormUtils.processRegistrationForm(allSharedPreferences, jsonString, Constants.TABLES.PREGNANCY_OUTCOME);

        ArrayList<Event> eventList = new ArrayList<>();
        Event pregnancyOutcomeEvent = pncRegistrationEventClient.getEvent();
        pregnancyOutcomeEvent.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        eventList.add(pregnancyOutcomeEvent);

        List<EventClient> childrenEventClientList = getChildrenEventClients(jsonString);

        HashMap<Client, List<Event>> clientEventHashMap = getEventClientHashMap(childrenEventClientList);
        clientEventHashMap.put(pncRegistrationEventClient.getClient(), eventList);

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


    private List<EventClient> getChildrenEventClients(String jsonString) throws JSONException {
        List<EventClient> childrenEventClientList = new ArrayList<>();

        if (jsonString != null) {
            JSONObject jsonForm = new JSONObject(jsonString);
            String motherBaseId = jsonForm.optString(Constants.JSON_FORM_EXTRA.ENTITY_TYPE);
            JSONArray fields = JsonFormUtils.fields(jsonForm);
            JSONObject deliveryDate = JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.DELIVERY_DATE);
            JSONObject famNameObject = JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.FAM_NAME);

            String familyName = famNameObject != null ? famNameObject.optString(JsonFormUtils.VALUE) : "";
            String dob = deliveryDate.optString(JsonFormUtils.VALUE);

            JSONObject familyIdObject = JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.RELATIONAL_ID);
            String familyBaseEntityId = familyIdObject.getString(JsonFormUtils.VALUE);

            Map<String, List<JSONObject>> jsonObjectMap = PncRegistrationUtils.getChildFieldMaps(fields);

            childrenEventClientList.addAll(generateEventClientForEachChild(jsonObjectMap, motherBaseId, familyBaseEntityId, dob, familyName));
        }
        return childrenEventClientList;
    }


    private List<EventClient> generateEventClientForEachChild(Map<String, List<JSONObject>> jsonObjectMap,
                                                              String motherBaseId, String familyBaseEntityId,
                                                              String dob, String familyName) {
        AllSharedPreferences allSharedPreferences = GoldsmithApplication.getInstance().getContext().allSharedPreferences();
        List<EventClient> childrenEventClientList = new ArrayList<>();
        JSONArray childFields;
        for (Map.Entry<String, List<JSONObject>> entry : jsonObjectMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                childFields = new JSONArray();
                for (JSONObject jsonObject : entry.getValue()) {
                    try {
                        String replaceString = jsonObject.getString(JsonFormUtils.KEY);
                        JSONObject childField = new JSONObject(jsonObject.toString().replaceAll(replaceString, replaceString.substring(0, replaceString.lastIndexOf("_"))));
                        childFields.put(childField);
                    } catch (JSONException e) {
                        Timber.e(e);
                    }
                }
                String uniqueChildID = GoldsmithApplication.getInstance().getUniqueIdRepository().getNextUniqueId().getOpenmrsId();

                if (StringUtils.isNotBlank(uniqueChildID)) {
                    String childBaseEntityId = JsonFormUtils.generateRandomUUIDString();
                    try {
                        JSONObject surNameObject = JsonFormUtils.getFieldJSONObject(childFields, DBConstants.KEY.SUR_NAME);
                        String surName = surNameObject != null ? surNameObject.optString(JsonFormUtils.VALUE) : null;
                        String lastName = PncRegistrationUtils.sameASFamilyNameCheck(childFields) ? familyName : surName;

                        JSONObject pncForm = FormUtils
                                .getInstance(AncLibrary.getInstance().context().applicationContext())
                                .getFormJson(org.smartregister.chw.anc.util.Constants.FORMS.PNC_CHILD_REGISTRATION);
                        JsonFormUtils.getRegistrationForm(pncForm, childBaseEntityId, PncRegistrationUtils.getLocationID());

                        pncForm = JsonFormUtils.populatePNCForm(pncForm, childFields, familyBaseEntityId, motherBaseId, uniqueChildID, dob, lastName);
                        String practitionerIdentifier = GoldsmithApplication.getInstance().getContext().allSharedPreferences().getUserPractitionerIdentifier();
                        EventClient childEventClient = PncRegistrationUtils.processPncChild(pncForm, childFields,
                                allSharedPreferences, childBaseEntityId, familyBaseEntityId, motherBaseId, uniqueChildID, lastName, dob, practitionerIdentifier);
                        childrenEventClientList.add(childEventClient);
                        if (pncForm != null) {
                            PncRegistrationUtils.saveVaccineEvents(childFields, childBaseEntityId, dob);
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
            }
        }
        return childrenEventClientList;
    }

    public HashMap<Client, List<Event>> getEventClientHashMap(@NonNull List<EventClient> eventClients) {
        HashMap<Client, List<Event>> clientEventHashMap = new HashMap<>();

        for (EventClient eventClient : eventClients) {
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(eventClient.getEvent());
            clientEventHashMap.put(eventClient.getClient(), eventList);
        }

        return clientEventHashMap;
    }
}
