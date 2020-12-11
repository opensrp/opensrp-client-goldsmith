package org.smartregister.goldsmith.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.chw.core.contract.FamilyOtherMemberProfileExtendedContract;
import org.smartregister.chw.core.fragment.CoreFamilyOtherMemberProfileFragment;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Client;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.activity.BaseFamilyOtherMemberProfileActivity;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.fragment.BaseFamilyOtherMemberProfileFragment;
import org.smartregister.family.model.BaseFamilyOtherMemberProfileActivityModel;
import org.smartregister.family.model.BaseFamilyProfileModel;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.FamilyOtherMemberProfileFragment;
import org.smartregister.goldsmith.presenter.FamilyOtherMemberActivityPresenter;
import org.smartregister.goldsmith.util.Constants.IntentKeys;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;
import org.smartregister.util.FormUtils;
import org.smartregister.view.activity.DrishtiApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.Properties.DETAILS;
import static org.opensrp.api.constants.Gender.FEMALE;
import static org.smartregister.AllConstants.PLAN_IDENTIFIER;
import static org.smartregister.chw.anc.util.Constants.TABLES.EC_CHILD;
import static org.smartregister.goldsmith.util.Constants.Client.FIRST_NAME;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.FIELDS;
import static org.smartregister.util.JsonFormUtils.STEP1;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getFieldJSONObject;
import static org.smartregister.util.JsonFormUtils.getJSONObject;

public class FamilyOtherMemberProfileActivity extends BaseFamilyOtherMemberProfileActivity implements FamilyOtherMemberProfileExtendedContract.View {

    private String baseEntityId;
    private CommonPersonObjectClient client;
    private String familyHead;
    private String gender;
    private int age;

    @Override
    protected void initializePresenter() {
        client = (CommonPersonObjectClient) getIntent().getExtras().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        String familyBaseEntityId = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        String familyName = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_NAME);
        familyHead = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCaregiver = getIntent().getExtras().getString(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        String villageTown = getIntent().getExtras().getString(Constants.INTENT_KEY.VILLAGE_TOWN);
        baseEntityId = client.getColumnmaps().get(Constants.INTENT_KEY.BASE_ENTITY_ID);
        gender = client.getColumnmaps().get(IntentKeys.GENDER);
        age = Years.yearsBetween(new DateTime(client.getColumnmaps().get(IntentKeys.DOB)), DateTime.now()).getYears();
        presenter = new FamilyOtherMemberActivityPresenter( this, new BaseFamilyOtherMemberProfileActivityModel(),
                null, familyBaseEntityId, baseEntityId, familyHead, primaryCaregiver, villageTown, familyName);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        BaseFamilyOtherMemberProfileFragment profileOtherMemberFragment = FamilyOtherMemberProfileFragment.newInstance(this.getIntent().getExtras());
        adapter.addFragment(profileOtherMemberFragment, "");

        viewPager.setAdapter(adapter);

        return viewPager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem addMember = menu.findItem(R.id.add_member);
        if (addMember != null) {
            addMember.setVisible(false);
        }
        if (FEMALE.name().equalsIgnoreCase(gender) && age > 13) {
            menu.add(Menu.NONE, R.id.action_pregnancy_out_come, Menu.NONE, R.string.anc_pregnancy_out_come);
        }

        getMenuInflater().inflate(org.smartregister.chw.core.R.menu.other_member_menu, menu);
        if (showAncRegistration()) {
            menu.findItem(R.id.action_anc_registration).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_anc_registration:
                startAncRegister();
                return true;

            case R.id.action_pregnancy_out_come:
                BaseFamilyProfileModel model = new BaseFamilyProfileModel(familyHead);
                try {
                    JSONObject jsonForm = model.getFormAsJson("pregnancy_outcome", baseEntityId, Utils.context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID));
                    jsonForm.put("entity_id", baseEntityId);
                    JSONObject details = new JSONObject();
                    details.put(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
                    jsonForm.put(DETAILS, details);
                    jsonForm.put(DBConstants.KEY.RELATIONAL_ID, client.getCaseId());


                    Map<String, String> values = new HashMap<>();

                    values.put(CoreConstants.JsonAssets.FAM_NAME, client.getDetails().get(FIRST_NAME));
                    values.put(org.smartregister.family.util.DBConstants.KEY.RELATIONAL_ID, client.getCaseId());

                    org.smartregister.chw.core.utils.FormUtils.updateFormField(jsonForm.getJSONObject(STEP1).getJSONArray(FIELDS), values);

                    Intent intent = new Intent(this, Utils.metadata().familyMemberFormActivity);
                    intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

                    Form form = new Form();
                    form.setActionBarBackground(R.color.family_actionbar);
                    form.setWizard(false);
                    intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);

                    startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
                    return true;
                } catch (Exception e) {
                    Timber.e(e);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAncRegister() {
        String ancModuleName = org.smartregister.goldsmith.util.Constants.RegisterViewConstants.ModuleOptions.ANC;
        CoreLibrary.getInstance().setCurrentModule(ancModuleName);
        Context context = this;
        Intent intent = new Intent(context, BaseConfigurableRegisterActivity.class);
        intent.putExtra(AllConstants.IntentExtra.MODULE_NAME, ancModuleName);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.ACTION, AllConstants.IntentExtra.JsonForm.ACTION_REGISTRATION);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.ENTITY_TABLE, CoreConstants.TABLE_NAME.ANC_MEMBER);
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, client);
        context.startActivity(intent);
    }

    private boolean showAncRegistration() {
       /* String gender = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.GENDER, false);
        return (!presenter().isWomanAlreadyRegisteredOnAnc(client)) && isOfReproductiveAge(client, gender) && "Female".equalsIgnoreCase(gender);*/
        return true;
    }

    public boolean isOfReproductiveAge(CommonPersonObjectClient commonPersonObject, String gender) {
        if (gender.equalsIgnoreCase("Female")) {
            return org.smartregister.chw.core.utils.Utils.isMemberOfReproductiveAge(commonPersonObject, 10, 49);
        } else if (gender.equalsIgnoreCase("Male")) {
            return org.smartregister.chw.core.utils.Utils.isMemberOfReproductiveAge(commonPersonObject, 15, 49);
        } else {
            return false;
        }
    }


    public FamilyOtherMemberActivityPresenter presenter() {
        return (FamilyOtherMemberActivityPresenter) presenter;
    }

    @Override
    public void refreshList() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            for (int i = 0; i < adapter.getCount(); i++) {
                refreshList(adapter.getItem(i));
            }
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                for (int i = 0; i < adapter.getCount(); i++) {
                    refreshList(adapter.getItem(i));
                }
            });
        }
    }

    protected void refreshList(Fragment fragment) {
        if (fragment instanceof CoreFamilyOtherMemberProfileFragment) {
            CoreFamilyOtherMemberProfileFragment familyOtherMemberProfileFragment = ((CoreFamilyOtherMemberProfileFragment) fragment);
            if (familyOtherMemberProfileFragment.presenter() != null) {
                familyOtherMemberProfileFragment.refreshListView();
            }
        }
    }

    @Override
    public void updateHasPhone(boolean hasPhone) {

    }

    @Override
    public void setFamilyServiceStatus(String status) {
        // TODO
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == org.smartregister.chw.core.R.id.family_has_row) {
            // TODO
        } else {
            super.onClick(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (JsonFormUtils.REQUEST_CODE_GET_JSON == requestCode && Activity.RESULT_OK == resultCode) {
            String json = data.getStringExtra(JsonFormConstants.JSON_FORM_KEY.JSON);
            try {
                JSONObject jsonForm = new JSONObject(json);
                String encounterType = jsonForm.optString(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.ENCOUNTER_TYPE);

                if (encounterType.equalsIgnoreCase(org.smartregister.chw.anc.util.Constants.EVENT_TYPE.PREGNANCY_OUTCOME)) {

                    String table = "ec_pregnancy_outcome";
                    boolean hasChildren = false;
                    saveRegistration(jsonForm.toString(), table);

                    String motherBaseId = jsonForm.optString(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.ENTITY_TYPE);
                    JSONArray fields = org.smartregister.util.JsonFormUtils.fields(jsonForm);
                    JSONObject deliveryDate = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.DELIVERY_DATE);
                    JSONObject famNameObject = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.FAM_NAME);

                    String familyName = famNameObject != null ? famNameObject.optString(org.smartregister.chw.anc.util.JsonFormUtils.VALUE) : "";
                    String dob = deliveryDate.optString(org.smartregister.chw.anc.util.JsonFormUtils.VALUE);
                    hasChildren = StringUtils.isNotBlank(deliveryDate.optString(org.smartregister.chw.anc.util.JsonFormUtils.VALUE));

                    JSONObject familyIdObject = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.RELATIONAL_ID);
                    String familyBaseEntityId = familyIdObject.getString(org.smartregister.chw.anc.util.JsonFormUtils.VALUE);

                    Map<String, List<JSONObject>> jsonObjectMap = getChildFieldMaps(fields);

                    generateAndSaveFormsForEachChild(jsonObjectMap, motherBaseId, familyBaseEntityId, dob, familyName);
                    return;
                }

                String entityId = JsonFormUtils.getString(jsonForm, ENTITY_ID);

                //TODO: Fix this missing base-entity-id when starting the form
                if (TextUtils.isEmpty(entityId)) {
                    entityId = UUID.randomUUID().toString();
                }

                JSONArray fields = org.smartregister.util.JsonFormUtils.fields(jsonForm);
                JSONObject metadata = getJSONObject(jsonForm, JsonFormUtils.METADATA);
                FormTag formTag = new FormTag();
                formTag.providerId = org.smartregister.chw.core.utils.Utils.context().allSharedPreferences().fetchRegisteredANM();
                formTag.appVersion = FamilyLibrary.getInstance().getApplicationVersion();
                formTag.databaseVersion = FamilyLibrary.getInstance().getDatabaseVersion();
                Event event = org.smartregister.util.JsonFormUtils.createEvent(fields, metadata, formTag, entityId, jsonForm.getString(JsonFormConstants.ENCOUNTER_TYPE), "PNC");
                org.smartregister.goldsmith.util.JsonFormUtils.tagSyncMetadata(CoreLibrary.getInstance().context().allSharedPreferences(), event);
                event.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
                JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(event));
                CoreLibrary.getInstance().context().getEventClientRepository().addEvent(event.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unsynced);
                DrishtiApplication.getInstance().getClientProcessor().processClient(Collections.singletonList(
                        new EventClient(JsonFormUtils.gson.fromJson(eventJson.toString(),
                                org.smartregister.domain.Event.class), new Client(event.getBaseEntityId()))), true);
            } catch (Exception e) {
                Timber.e(e);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveRegistration(final String jsonString, String table) throws Exception {
        AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = org.smartregister.chw.anc.util.JsonFormUtils.processJsonForm(allSharedPreferences, jsonString, table);
        baseEvent.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);

        NCUtils.addEvent(allSharedPreferences, baseEvent);
        NCUtils.startClientProcessing();
    }

    private Map<String, List<JSONObject>> getChildFieldMaps(JSONArray fields) {
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

    protected void generateAndSaveFormsForEachChild(Map<String, List<JSONObject>> jsonObjectMap, String motherBaseId, String familyBaseEntityId, String dob, String familyName) {
        AllSharedPreferences allSharedPreferences = ImmunizationLibrary.getInstance().context().allSharedPreferences();

        JSONArray childFields;
        for (Map.Entry<String, List<JSONObject>> entry : jsonObjectMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                childFields = new JSONArray();
                for (JSONObject jsonObject : entry.getValue()) {
                    try {
                        String replaceString = jsonObject.getString(org.smartregister.chw.anc.util.JsonFormUtils.KEY);

                        JSONObject childField = new JSONObject(jsonObject.toString().replaceAll(replaceString, replaceString.substring(0, replaceString.lastIndexOf("_"))));

                        childFields.put(childField);
                    } catch (JSONException e) {
                        Timber.e(e);
                    }
                }
                saveChild(childFields, motherBaseId, allSharedPreferences, familyBaseEntityId, dob, familyName);
            }
        }
    }

    private void saveChild(JSONArray childFields, String motherBaseId, AllSharedPreferences
            allSharedPreferences, String familyBaseEntityId, String dob, String familyName) {
        String uniqueChildID = AncLibrary.getInstance().getUniqueIdRepository().getNextUniqueId().getOpenmrsId();

        if (StringUtils.isNotBlank(uniqueChildID)) {
            String childBaseEntityId = org.smartregister.chw.anc.util.JsonFormUtils.generateRandomUUIDString();
            try {

                JSONObject surNameObject = org.smartregister.util.JsonFormUtils.getFieldJSONObject(childFields, DBConstants.KEY.SUR_NAME);
                String surName = surNameObject != null ? surNameObject.optString(org.smartregister.chw.anc.util.JsonFormUtils.VALUE) : null;
                String lastName = sameASFamilyNameCheck(childFields) ? familyName : surName;

                JSONObject pncForm = FormUtils
                        .getInstance(AncLibrary.getInstance().context().applicationContext())
                        .getFormJson(org.smartregister.chw.anc.util.Constants.FORMS.PNC_CHILD_REGISTRATION);
                org.smartregister.chw.anc.util.JsonFormUtils.getRegistrationForm(pncForm, childBaseEntityId, getLocationID());

                pncForm = org.smartregister.chw.anc.util.JsonFormUtils.populatePNCForm(pncForm, childFields, familyBaseEntityId, motherBaseId, uniqueChildID, dob, lastName);
                processPncChild(childFields, allSharedPreferences, childBaseEntityId, familyBaseEntityId, motherBaseId, uniqueChildID, lastName, dob);
                if (pncForm != null) {
                    saveRegistration(pncForm.toString(), EC_CHILD);
                    saveVaccineEvents(childFields, childBaseEntityId, dob);
                }

            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    public static void saveVaccineEvents(JSONArray fields, String baseID, String dob) {

        JSONObject vaccinesAtBirthObject = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, DBConstants.KEY.VACCINES_AT_BIRTH);
        JSONArray vaccinesAtBirthArray = vaccinesAtBirthObject != null ? vaccinesAtBirthObject.optJSONArray(DBConstants.KEY.OPTIONS) : null;

        if (vaccinesAtBirthArray != null) {
            for (int i = 0; i < vaccinesAtBirthArray.length(); i++) {
                JSONObject currentVaccine = vaccinesAtBirthArray.optJSONObject(i);
                if (currentVaccine != null && currentVaccine.optBoolean(org.smartregister.chw.anc.util.JsonFormUtils.VALUE) && !currentVaccine.optString(org.smartregister.chw.anc.util.JsonFormUtils.KEY).equals("chk_none")) {
                    String VaccineName = currentVaccine.optString(org.smartregister.chw.anc.util.JsonFormUtils.KEY).equals("chk_bcg") ? "bcg" : "opv_0";
                    VisitUtils.savePncChildVaccines(VaccineName, baseID, vaccinationDate(dob));
                }
            }

        } else {
            saveVaccines(fields, baseID);
        }
    }

    private static void saveVaccines(JSONArray fields, String baseID) {
        String[] vaccines = {"bcg_date", "opv0_date"};
        for (int i = 0; i < vaccines.length; i++) {
            try {
                String vaccineDate = getFieldJSONObject(fields, vaccines[i]).optString(VALUE);
                if (StringUtils.isNotBlank(vaccineDate)) {
                    Date dateVaccinated = vaccinationDate(vaccineDate);
                    String VaccineName = vaccines[i] == "bcg_date" ? "bcg" : "opv_0";
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

    private boolean sameASFamilyNameCheck(JSONArray childFields) {
        if (childFields.length() > 0) {
            JSONObject sameAsFamNameCheck = org.smartregister.util.JsonFormUtils.getFieldJSONObject(childFields, DBConstants.KEY.SAME_AS_FAM_NAME_CHK);
            sameAsFamNameCheck = sameAsFamNameCheck != null ? sameAsFamNameCheck : org.smartregister.util.JsonFormUtils.getFieldJSONObject(childFields, DBConstants.KEY.SAME_AS_FAM_NAME);
            JSONObject sameAsFamNameObject = sameAsFamNameCheck.optJSONArray(DBConstants.KEY.OPTIONS).optJSONObject(0);
            if (sameAsFamNameCheck != null) {
                return sameAsFamNameObject.optBoolean(org.smartregister.chw.anc.util.JsonFormUtils.VALUE);
            }
        }
        return false;
    }

    public void processPncChild(JSONArray fields, AllSharedPreferences allSharedPreferences, String entityId, String familyBaseEntityId, String motherBaseId, String uniqueChildID, String lastName, String dob) {
        try {
            org.smartregister.clientandeventmodel.Client pncChild = org.smartregister.util.JsonFormUtils.createBaseClient(fields, org.smartregister.chw.anc.util.JsonFormUtils.formTag(allSharedPreferences), entityId);
            Map<String, String> identifiers = new HashMap<>();
            identifiers.put(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.OPENSPR_ID, uniqueChildID.replace("-", ""));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(dob);
            pncChild.setLastName(lastName);
            pncChild.setBirthdate(date);
            pncChild.setIdentifiers(identifiers);
            pncChild.addRelationship(org.smartregister.chw.anc.util.Constants.RELATIONSHIP.FAMILY, familyBaseEntityId);
            pncChild.addRelationship(org.smartregister.chw.anc.util.Constants.RELATIONSHIP.MOTHER, motherBaseId);

            JSONObject eventJson = new JSONObject(org.smartregister.chw.anc.util.JsonFormUtils.gson.toJson(pncChild));
            AncLibrary.getInstance().getUniqueIdRepository().close(pncChild.getIdentifier(org.smartregister.chw.anc.util.Constants.JSON_FORM_EXTRA.OPENSPR_ID));

            NCUtils.getSyncHelper().addClient(pncChild.getBaseEntityId(), eventJson);

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    protected String getLocationID() {
        return org.smartregister.Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }
}
