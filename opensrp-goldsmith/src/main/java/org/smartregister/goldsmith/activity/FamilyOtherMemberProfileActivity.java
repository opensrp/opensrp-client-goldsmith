package org.smartregister.goldsmith.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Client;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.family.FamilyLibrary;

import org.smartregister.chw.core.contract.FamilyOtherMemberProfileExtendedContract;
import org.smartregister.chw.core.fragment.CoreFamilyOtherMemberProfileFragment;

import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
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
import org.smartregister.goldsmith.util.Constants.IntentKeys;
import org.smartregister.repository.BaseRepository;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.Collections;

import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.Properties.DETAILS;
import static org.opensrp.api.constants.Gender.FEMALE;
import static org.smartregister.AllConstants.PLAN_IDENTIFIER;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import org.smartregister.goldsmith.presenter.FamilyOtherMemberActivityPresenter;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;

import static org.smartregister.goldsmith.util.Constants.Client.FIRST_NAME;

public class FamilyOtherMemberProfileActivity extends BaseFamilyOtherMemberProfileActivity implements FamilyOtherMemberProfileExtendedContract.View {

    private String baseEntityId;
    private CommonPersonObjectClient client;
    private String familyHead;
    private String gender;
    private int age;

    @Override
    protected void initializePresenter() {
        client = (CommonPersonObjectClient) getIntent().getExtras().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        String familyBaseEntityId = client.getCaseId();
        String familyName = client.getDetails().get(FIRST_NAME);
        // TODO -> Decouple from CHW-CORE and use CommonPersonObjectClient as-is instead
        baseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        familyHead = client.getDetails().get(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCaregiver = client.getDetails().get(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        String villageTown = client.getDetails().get(Constants.INTENT_KEY.VILLAGE_TOWN);
        // TODO => Confirm gender and age is okay
        gender = client.getDetails().get(IntentKeys.GENDER);
        age = Years.yearsBetween(new DateTime( client.getDetails().get(IntentKeys.DOB)), DateTime.now()).getYears();
        presenter = new FamilyOtherMemberActivityPresenter((FamilyOtherMemberProfileExtendedContract.View) this, new BaseFamilyOtherMemberProfileActivityModel(),
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
                    JSONObject details = new JSONObject();
                    details.put(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
                    jsonForm.put(DETAILS, details);
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

    protected void startAncRegister() {
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
                String entityId = JsonFormUtils.getString(jsonForm, ENTITY_ID);
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
}
