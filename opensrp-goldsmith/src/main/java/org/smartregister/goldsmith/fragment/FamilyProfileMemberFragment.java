package org.smartregister.goldsmith.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.AllConstants;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.model.BaseFamilyProfileMemberModel;
import org.smartregister.family.presenter.BaseFamilyProfileMemberPresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.FamilyOtherMemberProfileActivity;

import java.util.HashMap;

public class FamilyProfileMemberFragment extends BaseFamilyProfileMemberFragment {

    private String familyBaseEntityId;
    private String familyHead;
    private String primaryCareGiver;
    private String villageTown;
    private String familyName;

    public static BaseFamilyProfileMemberFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileMemberFragment fragment = new FamilyProfileMemberFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        CommonPersonObjectClient client = (CommonPersonObjectClient) getArguments().getSerializable(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        familyBaseEntityId = client.getCaseId();
        familyName = Utils.getValue(client.getColumnmaps(), AllConstants.Client.FIRST_NAME, false);
        familyHead = Utils.getValue(client.getColumnmaps(), Constants.INTENT_KEY.FAMILY_HEAD, false);
        primaryCareGiver = Utils.getValue(client.getColumnmaps(), Constants.INTENT_KEY.PRIMARY_CAREGIVER, false);
        villageTown = Utils.getValue(client.getColumnmaps(), Constants.INTENT_KEY.VILLAGE_TOWN, false);
        presenter = new BaseFamilyProfileMemberPresenter(this, new BaseFamilyProfileMemberModel(), null, familyBaseEntityId, familyHead, primaryCareGiver);
    }

    @Override
    protected void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.patient_column && view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            goToOtherMemberProfileActivity((CommonPersonObjectClient) view.getTag());
        }
    }

    public void goToOtherMemberProfileActivity(CommonPersonObjectClient patient) {
        Intent intent = new Intent(getActivity(), FamilyOtherMemberProfileActivity.class);
        intent.putExtra(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID, familyBaseEntityId);
        intent.putExtra(Constants.INTENT_KEY.VILLAGE_TOWN, villageTown);
        intent.putExtra(Constants.INTENT_KEY.FAMILY_HEAD, familyHead);
        intent.putExtra(Constants.INTENT_KEY.PRIMARY_CAREGIVER, primaryCareGiver);
        intent.putExtra(Constants.INTENT_KEY.FAMILY_NAME, familyName);
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, patient);
        startActivity(intent);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {
        //do nothing
    }

}
