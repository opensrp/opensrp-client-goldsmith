package org.smartregister.goldsmith.util;


import org.smartregister.chw.core.rule.PNCHealthFacilityVisitRule;
import org.smartregister.goldsmith.GoldsmithApplication;

import java.util.Date;

public class PNCVisitUtil {

    public static PNCHealthFacilityVisitRule getNextPNCHealthFacilityVisit(Date deliveryDate, Date lastVisitDate) {

        PNCHealthFacilityVisitRule visitRule = new PNCHealthFacilityVisitRule(deliveryDate, lastVisitDate);
        visitRule = GoldsmithApplication.getInstance().getRulesEngineHelper().getPNCHealthFacilityRule(visitRule, Constants.RULE_FILE.PNC_HEALTH_FACILITY_VISIT);

        return visitRule;
    }

}
