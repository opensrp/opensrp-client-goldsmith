package org.smartregister.goldsmith.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.EventClient;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.repository.AllSharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.AllConstants.PLAN_IDENTIFIER;
import static org.smartregister.chw.anc.util.Constants.TABLES.EC_CHILD;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getFieldJSONObject;

public class PncRegistrationUtils {

    public static EventClient processPncChild(JSONObject jsonForm, JSONArray fields, AllSharedPreferences allSharedPreferences,
                                              String entityId, String familyBaseEntityId,
                                              String motherBaseId, String uniqueChildID, String lastName,
                                              String dob) {
        try {
            Client pncChildClient = JsonFormUtils.createBaseClient(fields, org.smartregister.chw.anc.util.JsonFormUtils.formTag(allSharedPreferences), entityId);
            Map<String, String> identifiers = new HashMap<>();
            identifiers.put(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.OPENSPR_ID, uniqueChildID.replace("-", ""));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(dob);
            pncChildClient.setLastName(lastName);
            pncChildClient.setBirthdate(date);
            pncChildClient.setIdentifiers(identifiers);
            pncChildClient.addRelationship(Constants.RELATIONSHIP.FAMILY, familyBaseEntityId);
            pncChildClient.addRelationship(org.smartregister.chw.anc.util.Constants.RELATIONSHIP.MOTHER, motherBaseId);

            Event baseEvent = org.smartregister.chw.anc.util.JsonFormUtils.processJsonForm(allSharedPreferences, jsonForm.toString(), EC_CHILD);
            baseEvent.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
            JsonFormUtils.tagSyncMetadata(allSharedPreferences, baseEvent);

            AncLibrary.getInstance().getUniqueIdRepository().close(pncChildClient.getIdentifier(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.OPENSPR_ID));

            return new EventClient(baseEvent, pncChildClient);
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    public static Map<String, List<JSONObject>> getChildFieldMaps(JSONArray fields) {
        Map<String, List<JSONObject>> jsonObjectMap = new HashMap();

        for (int i = 0; i < fields.length(); i++) {
            try {
                JSONObject jsonObject = fields.getJSONObject(i);
                String key = jsonObject.getString(org.smartregister.chw.anc.util.JsonFormUtils.KEY);
                String keySplit = key.substring(key.lastIndexOf("_"));
                if (keySplit.matches(".*\\d.*")) {

                    String formattedKey = keySplit.replaceAll("[^\\d.]", "");
                    if (formattedKey.length() < 10)
                        continue;
                    List<JSONObject> jsonObjectList = jsonObjectMap.get(formattedKey);

                    if (jsonObjectList == null)
                        jsonObjectList = new ArrayList<>();

                    jsonObjectList.add(jsonObject);
                    jsonObjectMap.put(formattedKey, jsonObjectList);
                }
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
        return jsonObjectMap;
    }

    public static void saveVaccineEvents(JSONArray fields, String baseID, String dob) {

        JSONObject vaccinesAtBirthObject = getFieldJSONObject(fields, DBConstants.KEY.VACCINES_AT_BIRTH);
        JSONArray vaccinesAtBirthArray = vaccinesAtBirthObject != null ? vaccinesAtBirthObject.optJSONArray(DBConstants.KEY.OPTIONS) : null;

        if (vaccinesAtBirthArray != null) {
            for (int i = 0; i < vaccinesAtBirthArray.length(); i++) {
                JSONObject currentVaccine = vaccinesAtBirthArray.optJSONObject(i);
                if (currentVaccine != null && currentVaccine.optBoolean(VALUE)
                        && !currentVaccine.optString(JsonFormUtils.KEY).equals("chk_none")) {
                    String VaccineName = currentVaccine.optString(JsonFormUtils.KEY).equals("chk_bcg") ? "bcg" : "opv_0";
                    VisitUtils.savePncChildVaccines(VaccineName, baseID, vaccinationDate(dob));
                }
            }

        } else {
            saveVaccines(fields, baseID);
        }
    }

    public static void saveVaccines(JSONArray fields, String baseID) {
        String[] vaccines = {"bcg_date", "opv0_date"};
        for (int i = 0; i < vaccines.length; i++) {
            try {
                String vaccineDate = getFieldJSONObject(fields, vaccines[i]).optString(VALUE);
                if (StringUtils.isNotBlank(vaccineDate)) {
                    Date dateVaccinated = vaccinationDate(vaccineDate);
                    String VaccineName = vaccines[i].equals("bcg_date") ? "bcg" : "opv_0";
                    VisitUtils.savePncChildVaccines(VaccineName, baseID, dateVaccinated);
                }
            } catch (NullPointerException e) {
                Timber.d(e);
            }
        }
    }

    public static Date vaccinationDate(String vaccineDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            return formatter.parse(vaccineDate);
        } catch (ParseException e) {
            Timber.e(e);
        }
        return null;
    }


    public static boolean sameASFamilyNameCheck(JSONArray childFields) {
        if (childFields.length() > 0) {
            JSONObject sameAsFamNameCheck = getFieldJSONObject(childFields, DBConstants.KEY.SAME_AS_FAM_NAME_CHK);
            sameAsFamNameCheck = sameAsFamNameCheck != null ? sameAsFamNameCheck : getFieldJSONObject(childFields, DBConstants.KEY.SAME_AS_FAM_NAME);
            JSONObject sameAsFamNameObject = sameAsFamNameCheck.optJSONArray(DBConstants.KEY.OPTIONS).optJSONObject(0);
            if (sameAsFamNameCheck != null) {
                return sameAsFamNameObject.optBoolean(VALUE);
            }
        }
        return false;
    }

    public static String getLocationID() {
        return org.smartregister.Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }
}
