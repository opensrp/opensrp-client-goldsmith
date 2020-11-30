package org.smartregister.goldsmith.configuration;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseMemberProfileOptions;
import org.smartregister.configuration.ConfigurableMemberProfileRowDataProvider;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;

public class AncMemberProfileOptions implements BaseMemberProfileOptions {


    @Override
    public int getMenuLayoutId() {
        return R.menu.anc_member_profile_menu;
    }

    @Override
    public boolean onMenuOptionsItemSelected(MenuItem menuItem, Context context, CommonPersonObjectClient client) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_pregnancy_out_come) {
            startPncRegistration(context, client);
        }
        return true;
    }

    @Override
    public Class<? extends ConfigurableMemberProfileRowDataProvider> getMemberProfileDataProvider() {
        return AncMemberProfileRowDataProvider.class;
    }

    private void startPncRegistration(Context context, CommonPersonObjectClient client) {
        String ancModuleName = Constants.RegisterViewConstants.ModuleOptions.PNC;
        CoreLibrary.getInstance().setCurrentModule(ancModuleName);
        Intent intent = new Intent(context, BaseConfigurableRegisterActivity.class);
        intent.putExtra(AllConstants.IntentExtra.MODULE_NAME, ancModuleName);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.BASE_ENTITY_ID, client.getDetails().get(AllConstants.Client.BASE_ENTITY_ID));
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, client);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.ACTION, AllConstants.IntentExtra.JsonForm.ACTION_REGISTRATION);
        intent.putExtra(AllConstants.IntentExtra.JsonForm.ENTITY_TABLE, CoreConstants.TABLE_NAME.PNC_MEMBER);
        context.startActivity(intent);
    }
}
