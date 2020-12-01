package org.smartregister.goldsmith.configuration.pnc;

import android.content.Context;
import android.view.MenuItem;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseMemberProfileOptions;
import org.smartregister.configuration.ConfigurableMemberProfileRowDataProvider;
import org.smartregister.goldsmith.configuration.anc.AncMemberProfileRowDataProvider;

public class PncMemberProfileOption implements BaseMemberProfileOptions {
    @Override
    public int getMenuLayoutId() {
        return 0;
    }

    @Override
    public boolean onMenuOptionsItemSelected(MenuItem menuItem, Context context, CommonPersonObjectClient client) {
        return false;
    }

    @Override
    public Class<? extends ConfigurableMemberProfileRowDataProvider> getMemberProfileDataProvider() {
        return AncMemberProfileRowDataProvider.class;
    }
}
