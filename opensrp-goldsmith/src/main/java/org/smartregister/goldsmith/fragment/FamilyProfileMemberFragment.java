package org.smartregister.goldsmith.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.AllConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.model.BaseFamilyProfileMemberModel;
import org.smartregister.family.presenter.BaseFamilyProfileMemberPresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.FamilyOtherMemberProfileActivity;
import org.smartregister.goldsmith.util.Constants.IntentKeys;

import java.util.HashMap;

public class FamilyProfileMemberFragment extends BaseFamilyProfileMemberFragment {

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
        // TODO -> Decouple from CHW-CORE and use CommonPersonObjectClient as-is instead
        String familyBaseEntityId = client.getCaseId();
        String familyHead = client.getDetails().get(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCareGiver = client.getDetails().get(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        presenter = new BaseFamilyProfileMemberPresenter(this, new BaseFamilyProfileMemberModel(), null, familyBaseEntityId, familyHead, primaryCareGiver);
    }

    @Override
    protected void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.patient_column) {
            if (view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
                goToOtherMemberProfileActivity((CommonPersonObjectClient) view.getTag());
            }
        }
    }

    public void goToOtherMemberProfileActivity(CommonPersonObjectClient patient) {
        Intent intent = new Intent(getActivity(), FamilyOtherMemberProfileActivity.class);
        intent.putExtras(getArguments());
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, patient);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {
        //do nothing
    }

}
