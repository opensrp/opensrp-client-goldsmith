package org.smartregister.goldsmith.configuration;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.util.SampleAppJsonFormUtils;

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
        return getEventClientHashMap(familyEventClientList);
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
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(familyEventClient.getEvent());
            clientEventHashMap.put(familyEventClient.getClient(), eventList);
        }

        return clientEventHashMap;
    }

}
