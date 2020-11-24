package org.smartregister.goldsmith.configuration;

import org.smartregister.configuration.BaseMemberProfileOptions;
import org.smartregister.configuration.BaseMemberProfileRowsDataProvider;
import org.smartregister.view.activity.SecuredActivity;

public class AncMemberProfileOptions implements BaseMemberProfileOptions {
    @Override
    public Class<? extends SecuredActivity> getModuleMedicalHistoryActivity() {
        return null; // TODO
    }

    @Override
    public Class<? extends BaseMemberProfileRowsDataProvider> getMemberProfileDataProvider() {
        return AncMemberDataProvider.class;
    }
}
