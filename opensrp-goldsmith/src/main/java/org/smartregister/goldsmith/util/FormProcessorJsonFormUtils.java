package org.smartregister.goldsmith.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.client.utils.constants.JsonFormConstants;
import org.smartregister.configuration.ModuleMetadata;
import org.smartregister.domain.form.FormLocation;
import org.smartregister.family.util.Constants;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.FormUtils;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class FormProcessorJsonFormUtils extends JsonFormUtils {

    public static final String METADATA = "metadata";
    public static final String STEP2 = "step2";
    public static final String OPENSRP_ID = "OPENSRP_ID";

    public static JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String id, @NonNull String currentLocationId) throws JSONException {
        return getFormAsJson(form, formName, id, currentLocationId, null);
    }

    public static JSONObject getFormAsJson(@NonNull JSONObject form, @NonNull String formName, @NonNull String id, @NonNull String currentLocationId, @Nullable HashMap<String, String> injectedFieldValues) throws JSONException {
        String entityId = id;
        form.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);

        // Inject the field values
        if (injectedFieldValues != null && injectedFieldValues.size() > 0) {
            populateInjectedFields(form, injectedFieldValues);
        }


        if (org.smartregister.family.util.Utils.metadata().familyRegister.formName.equals(formName) || org.smartregister.family.util.Utils.metadata().familyMemberRegister.formName.equals(formName)) {
            if (StringUtils.isNotBlank(entityId)) {
                entityId = entityId.replace("-", "");
            }

            JSONArray field = fields(form, STEP1);
            JSONObject uniqueId = getFieldJSONObject(field, Constants.JSON_FORM_KEY.UNIQUE_ID);

            updateUniqueId(formName, form, entityId, uniqueId);
        }

        if (CoreLibrary.getInstance().getModuleConfiguration(CoreLibrary.getInstance().getCurrentModuleName()).getModuleMetadata().getRegistrationFormName().equals(formName)) {
            if (StringUtils.isBlank(entityId)) {
                UniqueIdRepository uniqueIdRepo = CoreLibrary.getInstance().context().getUniqueIdRepository();
                entityId = uniqueIdRepo.getNextUniqueId() != null ? uniqueIdRepo.getNextUniqueId().getOpenmrsId() : "";
                if (entityId.isEmpty()) {
                    Timber.e("SampleAppJsonFormUtils --> UniqueIds are empty");
                    return null;
                }
            }

            if (StringUtils.isNotBlank(entityId)) {
                entityId = entityId.replace("-", "");
            }

            addRegLocHierarchyQuestions(form);

            injectOpenSRPId(entityId, form);

        } else {
            Timber.w("SampleAppJsonFormUtils --> Unsupported form requested for launch %s", formName);
        }

        Timber.d("SampleAppJsonFormUtils --> form is %s", form.toString());
        return form;
    }

    public static void populateInjectedFields(@NonNull JSONObject form, @NotNull HashMap<String, String> injectedFieldValues) throws JSONException {
        if (form.has(AllConstants.JSON.COUNT)) {
            int stepCount = Integer.parseInt(form.optString(AllConstants.JSON.COUNT));
            for (int index = 0; index < stepCount; index++) {
                String stepName = AllConstants.JSON.STEP + (index + 1);
                JSONObject step = form.optJSONObject(stepName);
                if (step != null) {
                    JSONArray stepFields = step.optJSONArray(AllConstants.JSON.FIELDS);
                    for (int k = 0; k < stepFields.length(); k++) {
                        JSONObject jsonObject = stepFields.optJSONObject(k);
                        String fieldKey = jsonObject.optString(AllConstants.JSON.KEY);
                        String fieldValue = injectedFieldValues.get(fieldKey);
                        if (!TextUtils.isEmpty(fieldValue)) {
                            jsonObject.put(AllConstants.JSON.VALUE, fieldValue);
                        }
                    }
                }
            }
        }
    }

    public static void addRegLocHierarchyQuestions(@NonNull JSONObject form) {
        try {
            JSONArray questions = FormUtils.getMultiStepFormFields(form);
            ArrayList<String> allLevels = CoreLibrary.getInstance().getCurrentModuleConfiguration().getModuleMetadata().getLocationLevels();
            ArrayList<String> healthFacilities = CoreLibrary.getInstance().getCurrentModuleConfiguration().getModuleMetadata().getHealthFacilityLevels();

            List<String> defaultLocation = LocationHelper.getInstance().generateDefaultLocationHierarchy(allLevels);
            List<String> defaultFacility = LocationHelper.getInstance().generateDefaultLocationHierarchy(healthFacilities);
            List<FormLocation> entireTree = LocationHelper.getInstance().generateLocationHierarchyTree(true, allLevels);

            String defaultLocationString = AssetHandler.javaToJsonString(defaultLocation, new TypeToken<List<String>>() {
            }.getType());

            String defaultFacilityString = AssetHandler.javaToJsonString(defaultFacility, new TypeToken<List<String>>() {
            }.getType());

            String entireTreeString = AssetHandler.javaToJsonString(entireTree, new TypeToken<List<FormLocation>>() {
            }.getType());

            updateLocationTree(questions, defaultLocationString, defaultFacilityString, entireTreeString);
        } catch (Exception e) {
            Timber.e(e, "JsonFormUtils --> addChildRegLocHierarchyQuestions");
        }

    }

    private static void updateLocationTree(JSONArray questions,
                                           String defaultLocationString, String defaultFacilityString,
                                           String entireTreeString) throws JSONException {
        ModuleMetadata opdMetadata = CoreLibrary.getInstance().getCurrentModuleConfiguration().getModuleMetadata();
        if (opdMetadata != null && opdMetadata.getFieldsWithLocationHierarchy() != null && !opdMetadata.getFieldsWithLocationHierarchy().isEmpty()) {

            for (int i = 0; i < questions.length(); i++) {
                JSONObject widget = questions.getJSONObject(i);
                String key = widget.optString(AllConstants.JSON.KEY);
                if (StringUtils.isNotBlank(key) && opdMetadata.getFieldsWithLocationHierarchy().contains(widget.optString(AllConstants.JSON.KEY))) {
                    if (StringUtils.isNotBlank(entireTreeString)) {
                        addLocationTree(key, widget, entireTreeString, JsonFormConstants.TREE);
                    }
                    if (StringUtils.isNotBlank(defaultFacilityString)) {
                        addLocationTreeDefault(key, widget, defaultLocationString);
                    }
                }
            }
        }
    }

    private static void addLocationTree(@NonNull String widgetKey, @NonNull JSONObject
            widget, @NonNull String updateString, @NonNull String treeType) {
        try {
            if (widgetKey.equals(widget.optString(AllConstants.JSON.KEY))) {
                widget.put(treeType, new JSONArray(updateString));
            }
        } catch (JSONException e) {
            Timber.e(e, "JsonFormUtils --> addLocationTree");
        }
    }

    private static void addLocationTreeDefault(@NonNull String widgetKey, @NonNull JSONObject
            widget, @NonNull String updateString) {
        addLocationTree(widgetKey, widget, updateString, JsonFormConstants.DEFAULT);
    }

    public static JSONArray fields(@NonNull JSONObject jsonForm, @NonNull String step) {
        try {

            JSONObject step1 = jsonForm.has(step) ? jsonForm.getJSONObject(step) : null;
            if (step1 == null) {
                return null;
            }

            return step1.has(FIELDS) ? step1.getJSONArray(FIELDS) : null;

        } catch (JSONException e) {
            Timber.e(e, "SampleAppJsonFormUtils --> fields");
        }
        return null;
    }

    protected static void updateUniqueId(String formName, JSONObject form, String entityId, JSONObject uniqueId) throws JSONException {
        if (formName.equals(org.smartregister.family.util.Utils.metadata().familyRegister.formName)) {
            // Inject OpenSRP id into the form
            if (uniqueId == null) {
                JSONArray fields = fields(form, STEP2);
                uniqueId = getFieldJSONObject(fields, Constants.JSON_FORM_KEY.UNIQUE_ID);
            } else {
                uniqueId.remove(org.smartregister.family.util.JsonFormUtils.VALUE);
                uniqueId.put(org.smartregister.family.util.JsonFormUtils.VALUE, entityId + "_Family");
            }
        }
        if (uniqueId != null) {
            uniqueId.remove(org.smartregister.family.util.JsonFormUtils.VALUE);
            uniqueId.put(org.smartregister.family.util.JsonFormUtils.VALUE, entityId);
        }
    }

    protected static void injectOpenSRPId(String entityId, JSONObject form) throws JSONException {
        JSONObject stepOne = form.getJSONObject(FormProcessorJsonFormUtils.STEP1);
        JSONArray jsonArray = stepOne.getJSONArray(FormProcessorJsonFormUtils.FIELDS);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString(FormProcessorJsonFormUtils.KEY).equalsIgnoreCase(FormProcessorJsonFormUtils.OPENSRP_ID)) {
                jsonObject.remove(FormProcessorJsonFormUtils.VALUE);
                jsonObject.put(FormProcessorJsonFormUtils.VALUE, entityId);
            }
        }
    }
}