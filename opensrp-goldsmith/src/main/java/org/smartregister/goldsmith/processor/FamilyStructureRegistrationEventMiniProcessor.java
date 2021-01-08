package org.smartregister.goldsmith.processor;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cocoahero.android.geojson.Feature;

import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.Event;
import org.smartregister.domain.Geometry;
import org.smartregister.domain.Location;
import org.smartregister.domain.LocationProperty;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.jsonmapping.ClientClassification;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.goldsmith.util.JsonFormUtils;
import org.smartregister.repository.StructureRepository;
import org.smartregister.sync.MiniClientProcessorForJava;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 08-01-2021.
 */
public class FamilyStructureRegistrationEventMiniProcessor implements MiniClientProcessorForJava {

    public HashSet<String> eventTypes = new HashSet<>();

    public FamilyStructureRegistrationEventMiniProcessor() {
        eventTypes.add(Constants.GoldsmithEventTypes.REGISTER_FAMILY_STRUCTURE_EVENT);
    }

    @NonNull
    @Override
    public HashSet<String> getEventTypes() {
        return eventTypes;
    }

    @Override
    public boolean canProcess(@NonNull String s) {
        return eventTypes.contains(s);
    }

    @Override
    public void processEventClient(@NonNull EventClient eventClient, @NonNull List<Event> unsyncEventsList, @Nullable ClientClassification clientClassification) throws Exception {
        Event structureRegistrationEvent = eventClient.getEvent();

        StructureRepository structureRepository = CoreLibrary.getInstance().context().getStructureRepository();

        Location location = new Location();
        location.setJurisdiction(false);
        Feature feature = null;

        for (Obs obs: structureRegistrationEvent.getObs()) {
            if ("geojson".equals(obs.getFormSubmissionField()) && obs.getValues() != null && obs.getValues().size() > 0) {
                Object geojson = obs.getValues().get(0);
                if (geojson instanceof String) {
                    feature = new Feature(new JSONObject((String) geojson));
                    break;
                }
            }
        }

        if (feature != null) {
            location.setGeometry(JsonFormUtils.gson.fromJson(feature.getGeometry().toJSON().toString(), Geometry.class));
            JSONObject properties = feature.getProperties();

            LocationProperty locationProperties = new LocationProperty();
            HashMap<String, String> customProperties = new HashMap<>();
            String locationUUID = null;

            Iterator<String> keysIterator = properties.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                if (key.equals("uuid")) {
                    locationUUID = properties.getString("uuid");
                    locationProperties.setUid(locationUUID);
                } else {
                    Object value = properties.get(key);
                    customProperties.put(key, JsonFormUtils.gson.toJson(value));
                }
            }

            locationProperties.setCustomProperties(customProperties);
            location.setProperties(locationProperties);

            structureRepository.addOrUpdate(location);

            ContentValues contentValues = new ContentValues();
            contentValues.put("structure_uuid", locationUUID);

            CoreLibrary.getInstance().context().commonrepository("ec_family")
                    .updateColumn("ec_family", contentValues, eventClient.getClient().getBaseEntityId());
        }
    }

    @Override
    public boolean unSync(@Nullable List<Event> list) {
        return false;
    }
}
