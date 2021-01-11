package org.smartregister.goldsmith.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.core.contract.FamilyOtherMemberProfileExtendedContract;
import org.smartregister.chw.core.fragment.CoreFamilyOtherMemberProfileFragment;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.activity.BaseFamilyOtherMemberProfileActivity;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.fragment.BaseFamilyOtherMemberProfileFragment;
import org.smartregister.family.model.BaseFamilyOtherMemberProfileActivityModel;
import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.FamilyOtherMemberProfileFragment;
import org.smartregister.goldsmith.presenter.FamilyOtherMemberActivityPresenter;
import org.smartregister.goldsmith.util.Constants.IntentKeys;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;

public class FamilyOtherMemberProfileActivity extends BaseFamilyOtherMemberProfileActivity implements FamilyOtherMemberProfileExtendedContract.View {

    private String baseEntityId;
    private CommonPersonObjectClient client;
    private String gender;

    @Override
    protected void initializePresenter() {
        client = (CommonPersonObjectClient) getIntent().getExtras().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        String familyBaseEntityId = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        String familyName = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_NAME);
        String familyHead = getIntent().getExtras().getString(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCaregiver = getIntent().getExtras().getString(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        String villageTown = getIntent().getExtras().getString(Constants.INTENT_KEY.VILLAGE_TOWN);
        baseEntityId = client.getColumnmaps().get(Constants.INTENT_KEY.BASE_ENTITY_ID);
        gender = client.getColumnmaps().get(IntentKeys.GENDER);
        int age = Years.yearsBetween(new DateTime(client.getColumnmaps().get(IntentKeys.DOB)), DateTime.now()).getYears();
        presenter = new FamilyOtherMemberActivityPresenter(this, new BaseFamilyOtherMemberProfileActivityModel(),
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
        getMenuInflater().inflate(org.smartregister.chw.core.R.menu.other_member_menu, menu);
        if (shouldShowAncRegistration()) {
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

    private boolean shouldShowAncRegistration() {
        return (!presenter().isWomanAlreadyRegisteredOnAnc(client)) && isOfReproductiveAge(client, gender) && "Female".equalsIgnoreCase(gender);
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
}
