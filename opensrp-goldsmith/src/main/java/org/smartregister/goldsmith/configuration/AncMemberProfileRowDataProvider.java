package org.smartregister.goldsmith.configuration;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.Context;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.ConfigurableMemberProfileRowDataProvider;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.ConfigurableMemberProfileRowData;
import org.smartregister.goldsmith.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AncMemberProfileRowDataProvider implements ConfigurableMemberProfileRowDataProvider {

    private Context context = Utils.context();

    @Override
    public List<ConfigurableMemberProfileRowData> getRowData(CommonPersonObjectClient client) {
        List<ConfigurableMemberProfileRowData> rowDataList = new ArrayList<>();
        rowDataList.add(getMedicalHistoryData(client));
        return rowDataList;
    }

    public ConfigurableMemberProfileRowData getMedicalHistoryData(CommonPersonObjectClient client) {
        ConfigurableMemberProfileRowData medicalHistoryData = new ConfigurableMemberProfileRowData();

        medicalHistoryData.setRowIconId(R.drawable.ic_medical_history);
        medicalHistoryData.setRowTitle(context.getStringResource(R.string.view_medical_history));

        Date lastVisitDate = getLastVisitDate(client);
        if (lastVisitDate != null) {
            int numOfDays = Days.daysBetween(new DateTime(lastVisitDate).toLocalDate(), new DateTime().toLocalDate()).getDays();
            String lastVisitText = String.format(context.getStringResource(R.string.last_visit_days_ago),
                    (numOfDays <= 1) ? context.getStringResource(R.string.less_than_twenty_four) : numOfDays + " " + context.getStringResource(R.string.days));
            medicalHistoryData.setRowDetail(lastVisitText);
        }

        return medicalHistoryData;
    }

    private Date getLastVisitDate(CommonPersonObjectClient client) {
        return new Date();
    }

    public AlertStatus getFamilyAlertStatus(CommonPersonObjectClient client) {
        return null;
    }

    public Alert getUpcomingServicesAlert(CommonPersonObjectClient client) {
        return null;
    }


}
