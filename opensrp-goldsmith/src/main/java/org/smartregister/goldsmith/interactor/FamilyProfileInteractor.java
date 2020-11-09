package org.smartregister.goldsmith.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.core.interactor.CoreFamilyProfileInteractor;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.goldsmith.BuildConfig;

import java.util.HashMap;
import java.util.Map;

import static org.smartregister.AllConstants.PLAN_IDENTIFIER;

public class FamilyProfileInteractor extends CoreFamilyProfileInteractor {

    @VisibleForTesting
    FamilyProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public FamilyProfileInteractor() {
        super();
    }

    public void saveRegistration(final FamilyEventClient familyEventClient, final String jsonString, final boolean isEditMode, final FamilyProfileContract.InteractorCallBack callBack) {
        Map<String, String> details = new HashMap<>();
        details.put(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        familyEventClient.getEvent().setDetails(details);
        super.saveRegistration(familyEventClient, jsonString, isEditMode, callBack);
    }

}
