package org.smartregister.goldsmith.activity;

import android.app.Activity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.smartregister.AllConstants;
import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.core.activity.CoreAboveFiveChildProfileActivity;
import org.smartregister.chw.core.activity.CoreChildProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileMenuActivity;
import org.smartregister.chw.core.activity.CoreFamilyRemoveMemberActivity;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.model.BaseFamilyProfileModel;
import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.FamilyProfileMemberFragment;
import org.smartregister.goldsmith.presenter.FamilyProfilePresenter;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FamilyProfileActivity extends CoreFamilyProfileActivity {

    @Override
    protected void initializePresenter() {

        CommonPersonObjectClient client = (CommonPersonObjectClient) getIntent().getExtras().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        familyBaseEntityId = client.getCaseId();
        familyHead = Utils.getValue(client.getColumnmaps(), Constants.INTENT_KEY.FAMILY_HEAD, false);
        primaryCaregiver = Utils.getValue(client.getColumnmaps(), Constants.INTENT_KEY.PRIMARY_CAREGIVER, false);
        familyName = Utils.getValue(client.getColumnmaps(), AllConstants.Client.FIRST_NAME, false);
        presenter = new FamilyProfilePresenter(this, new BaseFamilyProfileModel(familyName), familyBaseEntityId, familyHead, primaryCaregiver, familyName);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        int gsBlueColor = context().getColorResource(R.color.goldsmithToolbarColor);

        AppBarLayout appBarLayout = findViewById(R.id.toolbar_appbarlayout);
        appBarLayout.setBackgroundColor(gsBlueColor);

        Toolbar familyToolbar = findViewById(R.id.family_toolbar);
        familyToolbar.setBackgroundColor(gsBlueColor);
        TextView toolBarTitle = findViewById(R.id.toolbar_title);
        toolBarTitle.setText("");
        Toolbar familyTwoToolbar = findViewById(R.id.family_two_toolbar);
        familyTwoToolbar.setBackgroundColor(gsBlueColor);

        ViewGroup profileNameLayout = findViewById(R.id.profile_name_layout);
        profileNameLayout.setBackgroundColor(gsBlueColor);

        CircleImageView profileView = findViewById(R.id.imageview_profile);
        profileView.setBorderWidth(0);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setSelectedTabIndicatorColor(context().getColorResource(R.color.colorAccent));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        BaseFamilyProfileMemberFragment profileMemberFragment = FamilyProfileMemberFragment.newInstance(this.getIntent().getExtras());
        adapter.addFragment(profileMemberFragment, this.getString(R.string.member));
        // TODO -> Add Tasks fragment
        viewPager.setAdapter(adapter);
        return viewPager;
    }

    @Override
    protected Class<?> getFamilyOtherMemberProfileActivityClass() {
        return FamilyOtherMemberProfileActivity.class;
    }

    @Override
    protected Class<? extends CoreAboveFiveChildProfileActivity> getAboveFiveChildProfileActivityClass() {
        return null;
    }

    @Override
    protected Class<? extends CoreChildProfileActivity> getChildProfileActivityClass() {
        return null;
    }

    @Override
    protected Class<? extends BaseAncMemberProfileActivity> getAncMemberProfileActivityClass() {
        return null;
    }

    @Override
    protected Class<? extends BasePncMemberProfileActivity> getPncMemberProfileActivityClass() {
        return null;
    }

    @Override
    protected void goToFpProfile(String baseEntityId, Activity activity) {
        // To implement once profiles are present
    }

    @Override
    protected boolean isAncMember(String baseEntityId) {
        return false;
    }

    @Override
    protected HashMap<String, String> getAncFamilyHeadNameAndPhone(String baseEntityId) {
        return null;
    }

    @Override
    protected CommonPersonObject getAncCommonPersonObject(String baseEntityId) {
        return null;
    }

    @Override
    protected CommonPersonObject getPncCommonPersonObject(String baseEntityId) {
        return null;
    }

    @Override
    protected boolean isPncMember(String baseEntityId) {
        return false;
    }

    @Override
    protected void refreshPresenter() {
        this.presenter = new FamilyProfilePresenter(this, new BaseFamilyProfileModel(familyName),
                familyBaseEntityId, familyHead, primaryCaregiver, familyName);
    }

    @Override
    protected void refreshList(Fragment fragment) {
        if (fragment instanceof FamilyProfileMemberFragment) {
            FamilyProfileMemberFragment familyProfileMemberFragment = ((FamilyProfileMemberFragment) fragment);
            if (familyProfileMemberFragment.presenter() != null) {
                familyProfileMemberFragment.refreshListView();
            }
        }
    }

    @Override
    protected Class<? extends CoreFamilyRemoveMemberActivity> getFamilyRemoveMemberClass() {
        return null;
    }

    @Override
    protected Class<? extends CoreFamilyProfileMenuActivity> getFamilyProfileMenuClass() {
        return null;
    }

    @Override
    public void setEventDate(String s) {
        // Empty implementation
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        // Empty implementation
    }
}
