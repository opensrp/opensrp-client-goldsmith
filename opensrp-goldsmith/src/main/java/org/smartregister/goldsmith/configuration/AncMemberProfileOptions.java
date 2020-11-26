package org.smartregister.goldsmith.configuration;

import org.smartregister.configuration.BaseMemberProfileOptions;
import org.smartregister.configuration.ConfigurableMemberProfileRowDataProvider;

public class AncMemberProfileOptions implements BaseMemberProfileOptions {

    @Override
    public Class<? extends ConfigurableMemberProfileRowDataProvider> getMemberProfileDataProvider() {
        return AncMemberProfileRowDataProvider.class;
    }
}
