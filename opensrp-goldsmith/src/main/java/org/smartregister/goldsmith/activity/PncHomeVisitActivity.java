package org.smartregister.goldsmith.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitPresenter;
import org.smartregister.chw.core.task.RunnableTask;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.pnc.activity.BasePncHomeVisitActivity;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.interactor.PncHomeVisitInteractor;
import org.smartregister.goldsmith.schedulers.ChwScheduleTaskExecutor;

import java.util.Date;

import timber.log.Timber;

public class PncHomeVisitActivity extends BasePncHomeVisitActivity {

    protected String fhirTaskId;

    public static void startMe(Context activity, MemberObject memberObject, String fhirTaskId, Boolean isEditMode) {
        Intent intent = new Intent(activity, PncHomeVisitActivity.class);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT, memberObject);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.EDIT_MODE, isEditMode);
        intent.putExtra(org.smartregister.goldsmith.util.Constants.ANC_MEMBER_OBJECTS.FHIR_TASK_ID, fhirTaskId);
        //activity.startActivityForResult(intent, org.smartregister.chw.anc.util.Constants.REQUEST_CODE_HOME_VISIT);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().hasExtra(org.smartregister.goldsmith.util.Constants.ANC_MEMBER_OBJECTS.FHIR_TASK_ID)) {
            fhirTaskId = getIntent().getStringExtra(org.smartregister.goldsmith.util.Constants.ANC_MEMBER_OBJECTS.FHIR_TASK_ID);
        }
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(memberObject, this, new PncHomeVisitInteractor());
    }

    @Override
    public void submittedAndClose() {
        // recompute schedule
        Runnable runnable = () -> ChwScheduleTaskExecutor.getInstance().execute(memberObject.getBaseEntityId(), CoreConstants.EventType.PNC_HOME_VISIT, new Date());
        Utils.startAsyncTask(new RunnableTask(runnable), null);
        super.submittedAndClose();

    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Form form = new Form();
        form.setActionBarBackground(R.color.family_actionbar);
        form.setWizard(false);

        Intent intent = new Intent(this, Utils.metadata().familyMemberFormActivity);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        intent.putExtra(Constants.WizardFormActivity.EnableOnCloseDialog, false);
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);
        startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
    }

    public String getFhirTaskId() {
        return fhirTaskId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getStringExtra("json") != null) {
            String jsonString = data.getStringExtra("json");

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has(CoreConstants.JsonAssets.DETAILS)) {
                    JSONObject details = jsonObject.getJSONObject(CoreConstants.JsonAssets.DETAILS);
                    details.put(org.smartregister.goldsmith.util.Constants.EventDetails.TASK_ID, getFhirTaskId());

                    data.putExtra("json", jsonString.toString());
                }
            } catch (JSONException e) {
                Timber.e(e);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
