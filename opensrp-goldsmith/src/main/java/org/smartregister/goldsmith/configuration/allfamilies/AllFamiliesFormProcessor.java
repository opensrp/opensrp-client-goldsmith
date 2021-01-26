package org.smartregister.goldsmith.configuration.allfamilies;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.Location;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.util.SampleAppJsonFormUtils;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.tasking.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 30-09-2020.
 */
public class AllFamiliesFormProcessor implements ModuleFormProcessor {

    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String jsonString, @Nullable Intent data, @Nullable FormTag formTag) throws JSONException {
        List<FamilyEventClient> familyEventClientList = new ArrayList<>();
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyUpdateForm(Utils.context().allSharedPreferences(), jsonString);
        if (familyEventClient == null) {
            return getEventClientHashMap(familyEventClientList);
        }

        FamilyEventClient headEventClient = JsonFormUtils.processFamilyHeadRegistrationForm(Utils.context().allSharedPreferences(), jsonString, familyEventClient.getClient().getBaseEntityId());
        if (headEventClient == null) {
            return getEventClientHashMap(familyEventClientList);
        }

        if (headEventClient.getClient() != null && familyEventClient.getClient() != null) {
            String headUniqueId = headEventClient.getClient().getIdentifier(Utils.metadata().uniqueIdentifierKey);
            if (StringUtils.isNotBlank(headUniqueId)) {
                String familyUniqueId = headUniqueId + Constants.IDENTIFIER.FAMILY_SUFFIX;
                familyEventClient.getClient().addIdentifier(Utils.metadata().uniqueIdentifierKey, familyUniqueId);
            }
        }

        // Update the family head and primary caregiver
        Client familyClient = familyEventClient.getClient();
        familyClient.addRelationship(Utils.metadata().familyRegister.familyHeadRelationKey, headEventClient.getClient().getBaseEntityId());
        familyClient.addRelationship(Utils.metadata().familyRegister.familyCareGiverRelationKey, headEventClient.getClient().getBaseEntityId());

        familyEventClientList.add(familyEventClient);
        familyEventClientList.add(headEventClient);


        // Add a Register Family Structure
        FamilyEventClient familyStructureRegistrationEventClient = generateStructureRegistrationEvent(jsonString, familyClient);
        if (familyStructureRegistrationEventClient != null) {
            familyEventClientList.add(familyStructureRegistrationEventClient);
        }


        return getEventClientHashMap(familyEventClientList);
    }

    protected FamilyEventClient generateStructureRegistrationEvent(@NonNull String jsonString, Client familyClient) throws JSONException {
        String fieldValue  = org.smartregister.util.JsonFormUtils.getFieldValue(jsonString, "gps");
        if (!TextUtils.isEmpty(fieldValue)) {
            String[] coordinates = fieldValue.split(" ");
            if (coordinates.length > 1 && !TextUtils.isEmpty(coordinates[0]) && !TextUtils.isEmpty(coordinates[1]) ) {
                AllSharedPreferences allSharedPreferences = CoreLibrary.getInstance().context().allSharedPreferences();
                FormTag formTag = JsonFormUtils.formTag(allSharedPreferences);
                formTag.locationId = allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM());
                formTag.team = allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM());
                formTag.teamId = allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM());

                Event registerFamilyStructure = JsonFormUtils.createEvent(new JSONArray(), new JSONObject(), formTag, familyClient.getBaseEntityId(), org.smartregister.goldsmith.util.Constants.GoldsmithEventTypes.REGISTER_FAMILY_STRUCTURE_EVENT, "structure");
                String structureId = JsonFormUtils.generateRandomUUIDString();

                // id obs
                Obs idObservation = generateObs("id", structureId);

                // uuid obs
                Obs uuidObservation = generateObs("uuid", structureId);

                // parent-id obs
                String operationalArea = PreferencesUtil.getInstance().getCurrentOperationalArea();

                Obs parentIdObservation = new Obs();
                if (!TextUtils.isEmpty(operationalArea)) {
                    Location location = org.smartregister.tasking.util.Utils.getOperationalAreaLocation(operationalArea);

                    if (location != null && !TextUtils.isEmpty(location.getId())) {
                        parentIdObservation.setFieldType("formSubmissionField");
                        parentIdObservation.setFieldDataType("text");
                        parentIdObservation.setFieldCode("");
                        parentIdObservation.setParentCode("");
                        parentIdObservation.setHumanReadableValues(new ArrayList<>());

                        parentIdObservation.setFormSubmissionField("parentId");
                        ArrayList<Object> parentIdValues = new ArrayList<>();
                        parentIdValues.add(location.getId());
                        parentIdObservation.setValues(parentIdValues);
                    }
                }

                /// latitude Obs
                Obs latitudeObservation = generateObs("latitude", coordinates[0]);

                // longitude Obs
                Obs longitudeObservation = generateObs("longitude", coordinates[1]);

                com.mapbox.geojson.Point structurePoint = Point.fromLngLat(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
                com.mapbox.geojson.Feature feature = Feature.fromGeometry(structurePoint);

                feature.addStringProperty("uuid", (String) uuidObservation.getValues().get(0));
                feature.addStringProperty("id", (String) idObservation.getValues().get(0));
                if (parentIdObservation.getValues() != null) {
                    feature.addStringProperty("parentId", (String) parentIdObservation.getValues().get(0));
                }

                // geojson Obs
                Obs geojsonObservation = generateObs("geojson", feature.toJson());

                registerFamilyStructure.addObs(idObservation);
                registerFamilyStructure.addObs(uuidObservation);
                if (parentIdObservation.getValues() != null) {
                    registerFamilyStructure.addObs(parentIdObservation);
                }
                registerFamilyStructure.addObs(latitudeObservation);
                registerFamilyStructure.addObs(longitudeObservation);
                registerFamilyStructure.addObs(geojsonObservation);

                return new FamilyEventClient(familyClient, registerFamilyStructure);
            }
        }

        return null;
    }

    @NonNull
    protected Obs generateObs(String formSubmissionField, String value) {
        Obs observation = new Obs();
        observation.setFieldType("formSubmissionField");
        observation.setFieldDataType("text");
        observation.setFieldCode("");
        observation.setParentCode("");
        observation.setHumanReadableValues(new ArrayList<>());
        observation.setFormSubmissionField(formSubmissionField);

        ArrayList<Object> values = new ArrayList<>();
        values.add(value);
        observation.setValues(values);

        return observation;
    }

    @Override
    public JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String entityId,
                                    @NonNull String currentLocationId, @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        return SampleAppJsonFormUtils.getFormAsJson(form, formName, entityId, currentLocationId, injectedFieldValues);
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> list, String jsonString) {

        if (client != null || (list != null && list.size() > 1 && list.get(0) != null)) {
            String providerId = list.get(0).getProviderId();
            String imageLocation = null;

            if (client.getIdentifier("opensrp_id").toLowerCase().contains("family")) {
                String familyStep = Utils.getCustomConfigs(Constants.CustomConfig.FAMILY_FORM_IMAGE_STEP);

                imageLocation = (StringUtils.isBlank(familyStep)) ?
                        JsonFormUtils.getFieldValue(jsonString, Constants.KEY.PHOTO) :
                        JsonFormUtils.getFieldValue(jsonString, familyStep, Constants.KEY.PHOTO);

            } else {
                String familyMemberStep = Utils.getCustomConfigs(Constants.CustomConfig.FAMILY_MEMBER_FORM_IMAGE_STEP);

                imageLocation = (StringUtils.isBlank(familyMemberStep)) ?
                        JsonFormUtils.getFieldValue(jsonString, JsonFormUtils.STEP2, Constants.KEY.PHOTO) :
                        JsonFormUtils.getFieldValue(jsonString, familyMemberStep, Constants.KEY.PHOTO);
            }

            if (StringUtils.isNotBlank(imageLocation)) {
                JsonFormUtils.saveImage(providerId, client.getBaseEntityId(), imageLocation);
            }

            return true;
        }

        return false;
    }

    public HashMap<Client, List<Event>> getEventClientHashMap(@NonNull List<FamilyEventClient> familyEventClients) {
        HashMap<Client, List<Event>> clientEventHashMap = new HashMap<>();

        for (FamilyEventClient familyEventClient: familyEventClients) {
            List<Event> eventList = null;
            if (clientEventHashMap.containsKey(familyEventClient.getClient())) {
                eventList = clientEventHashMap.get(familyEventClient.getClient());
            } else {
                eventList = new ArrayList<>();
            }

            eventList.add(familyEventClient.getEvent());

            clientEventHashMap.put(familyEventClient.getClient(), eventList);
        }

        return clientEventHashMap;
    }

}
