package org.smartregister.goldsmith.presenter;

import android.util.Pair;

import org.smartregister.chw.core.contract.CoreChildRegisterContract;
import org.smartregister.chw.core.contract.FamilyProfileExtendedContract;
import org.smartregister.chw.core.model.CoreChildRegisterModel;
import org.smartregister.chw.core.presenter.CoreFamilyProfilePresenter;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.interactor.FamilyProfileInteractor;
import org.smartregister.goldsmith.model.ChildRegisterModel;

import static org.smartregister.AllConstants.PLAN_IDENTIFIER;

public class FamilyProfilePresenter extends CoreFamilyProfilePresenter {


    public FamilyProfilePresenter(FamilyProfileExtendedContract.View view, FamilyProfileContract.Model model, String familyBaseEntityId, String familyHead, String primaryCaregiver, String familyName) {
        super(view, model, familyBaseEntityId, familyHead, primaryCaregiver, familyName);
        interactor = new FamilyProfileInteractor();
        verifyHasPhone();
    }

    @Override
    public void refreshProfileTopSection(CommonPersonObjectClient client) {

        if (client == null || client.getColumnmaps() == null) {
            return;
        }

        String firstName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
        String famName;

        if (Utils.getBooleanProperty(Constants.Properties.FAMILY_HEAD_FIRSTNAME_ENABLED)) {
            String familyHeadFirstName = Utils.getValue(client.getColumnmaps(), Constants.KEY.FAMILY_HEAD_NAME, true);
            famName = getView().getApplicationContext().getString(R.string.family_profile_title_with_firstname, familyHeadFirstName, firstName);
        } else {
            famName = getView().getApplicationContext().getString(R.string.family_profile_title, firstName);
        }

        getView().setProfileName(famName);

        getView().setProfileDetailOne("0 min"); // TODO -> Placeholder : Set actual duration

        String villageTown = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.VILLAGE_TOWN, false);
        getView().setProfileDetailTwo(villageTown);


        getView().setProfileImage(client.getCaseId());

    }

    @Override
    protected CoreChildRegisterModel getChildRegisterModel() {
        return new ChildRegisterModel();
    }

    @Override
    public void saveChildRegistration(Pair<Client, Event> pair, String jsonString, boolean isEditMode, CoreChildRegisterContract.InteractorCallBack callBack) {
        pair.second.addDetails(PLAN_IDENTIFIER, BuildConfig.PNC_PLAN_ID);
        super.saveChildRegistration(pair, jsonString, isEditMode, this);
    }

}
