package org.smartregister.goldsmith.configuration;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseMemberProfileRowsDataProvider;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class AncMemberDataProvider implements BaseMemberProfileRowsDataProvider {

    @Override
    public Date getLastVisitDate(CommonPersonObjectClient client) {
        return null;
    }

    @Override
    public AlertStatus getFamilyAlertStatus(CommonPersonObjectClient client) {
        return null;
    }

    @Override
    public Alert getUpcomingServicesAlert(CommonPersonObjectClient client) {
        return null;
    }
}
