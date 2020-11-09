package org.smartregister.goldsmith.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.viewpager.widget.ViewPager;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.family.activity.BaseFamilyOtherMemberProfileActivity;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.fragment.BaseFamilyOtherMemberProfileFragment;
import org.smartregister.family.model.BaseFamilyOtherMemberProfileActivityModel;
import org.smartregister.family.model.BaseFamilyProfileModel;
import org.smartregister.family.presenter.BaseFamilyOtherMemberProfileActivityPresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.FamilyOtherMemberProfileFragment;
import org.smartregister.goldsmith.util.Constants.IntentKeys;

import timber.log.Timber;

import static org.opensrp.api.constants.Gender.FEMALE;

public class FamilyOtherMemberProfileActivity extends BaseFamilyOtherMemberProfileActivity {

    private String baseEntityId;

    private String familyHead;

    private String gender;

    private int age;

    @Override
    protected void initializePresenter() {
        baseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        familyHead = getIntent().getStringExtra(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCaregiver = getIntent().getStringExtra(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        String villageTown = getIntent().getStringExtra(Constants.INTENT_KEY.VILLAGE_TOWN);
        gender = getIntent().getStringExtra(IntentKeys.GENDER);
        age = Years.yearsBetween(new DateTime(getIntent().getStringExtra(IntentKeys.DOB)), DateTime.now()).getYears();
        presenter = new BaseFamilyOtherMemberProfileActivityPresenter(this, new BaseFamilyOtherMemberProfileActivityModel(), null, baseEntityId, familyHead, primaryCaregiver, villageTown);
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_pregnancy_out_come:
                BaseFamilyProfileModel model = new BaseFamilyProfileModel(familyHead);
                try {
                    JSONObject jsonForm = model.getFormAsJson("pregnancy_outcome", baseEntityId, Utils.context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID));

                    Intent intent = new Intent(this, Utils.metadata().familyMemberFormActivity);
                    intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

                    Form form = new Form();
                    form.setActionBarBackground(R.color.family_actionbar);
                    form.setWizard(false);
                    intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);

                    startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
                } catch (Exception e) {
                    Timber.e(e);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
