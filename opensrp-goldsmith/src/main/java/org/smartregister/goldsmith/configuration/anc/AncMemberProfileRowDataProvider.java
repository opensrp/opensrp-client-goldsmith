package org.smartregister.goldsmith.configuration.anc;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.smartregister.AllConstants;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.model.BaseUpcomingService;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.ConfigurableMemberProfileRowDataProvider;
import org.smartregister.dao.AbstractDao;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.ConfigurableMemberProfileRowData;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.dao.ChwANCDao;
import org.smartregister.goldsmith.dao.FamilyDao;
import org.smartregister.goldsmith.interactor.AncUpcomingServicesInteractorFlv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class AncMemberProfileRowDataProvider implements ConfigurableMemberProfileRowDataProvider {

    private final Context context = ChwApplication.getInstance().getApplicationContext();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private CommonPersonObjectClient commonPersonClient;
    private ConfigurableMemberProfileRowData rowData;

    @Override
    public List<ConfigurableMemberProfileRowData> getRowData(CommonPersonObjectClient client) {
        commonPersonClient = client;
        List<ConfigurableMemberProfileRowData> rowDataList = new ArrayList<>();
        addMedicalHistoryData(rowDataList);
        addUpcomingServicesData(rowDataList);
        addFamilyAlertStatusData(rowDataList);
        return rowDataList;
    }

    public void addMedicalHistoryData(List<ConfigurableMemberProfileRowData> rowDataList) {
        Date lastVisitDate = getLastVisitDate(commonPersonClient);
        if (lastVisitDate == null)
            return;

        rowData = new ConfigurableMemberProfileRowData();
        rowData.setRowIconId(R.drawable.ic_medical_history);
        rowData.setRowTitle(context.getString(R.string.view_medical_history));
        rowData.setRowDetail(getMedicalHistoryDetailString(lastVisitDate));

        rowDataList.add(rowData);
    }

    public void addUpcomingServicesData(List<ConfigurableMemberProfileRowData> rowDataList) {
        Alert upcomingServices = getUpcomingServicesAlert(commonPersonClient);
        if (upcomingServices == null || upcomingServices.status() == AlertStatus.complete)
            return;

        rowData = new ConfigurableMemberProfileRowData();
        rowData.setRowIconId(R.drawable.ic_upcoming_services);
        rowData.setRowTitle(context.getString(R.string.upcoming_services));
        rowData.setRowDetail(getUpcomingServicesDetailString(upcomingServices));

        rowDataList.add(rowData);
    }

    public void addFamilyAlertStatusData(List<ConfigurableMemberProfileRowData> rowDataList) {
        AlertStatus familyAlertStatus = getFamilyAlertStatus(commonPersonClient);
        if (familyAlertStatus == null)
            return;

        rowData = new ConfigurableMemberProfileRowData();
        rowData.setRowIconId(R.drawable.row_family);
        rowData.setRowTitle(context.getString(R.string.family_profile));
        rowData.setRowDetail(getFamilyStatusString(familyAlertStatus));

        rowDataList.add(rowData);
    }

    private Date getLastVisitDate(CommonPersonObjectClient client) {
        return new Date(); // TODO -> Plug in actual method call
        // return ChwANCDao.getLatestVisitDate(client.getDetails().get(AllConstants.Client.BASE_ENTITY_ID));
    }

    private AlertStatus getFamilyAlertStatus(CommonPersonObjectClient client) {
        return AlertStatus.normal; // TODO -> Plug in actual method call
        // return FamilyDao.getFamilyAlertStatus(client.getDetails().get(AllConstants.Client.BASE_ENTITY_ID));
    }

    public Alert getUpcomingServicesAlert(CommonPersonObjectClient client) {
        AncUpcomingServicesInteractorFlv upcomingServicesInteractor = new AncUpcomingServicesInteractorFlv();
        String baseEntityId = client.getDetails().get(AllConstants.Client.BASE_ENTITY_ID);
        try {
            List<BaseUpcomingService> baseUpcomingServices = upcomingServicesInteractor.getMemberServices(context, new MemberObject(commonPersonClient));
            if (baseUpcomingServices.size() > 0) {
                Comparator<BaseUpcomingService> comparator = (o1, o2) -> o1.getServiceDate().compareTo(o2.getServiceDate());
                Collections.sort(baseUpcomingServices, comparator);

                BaseUpcomingService baseUpcomingService = baseUpcomingServices.get(0);
                return new Alert(
                        baseEntityId,
                        baseUpcomingService.getServiceName(),
                        baseUpcomingService.getServiceName(),
                        baseUpcomingService.getServiceDate().before(new LocalDate().toDate()) ? AlertStatus.urgent : AlertStatus.normal,
                        AbstractDao.getDobDateFormat().format(baseUpcomingService.getServiceDate()),
                        "",
                        true
                );
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        return null;
    }

    private String getMedicalHistoryDetailString(Date lastVisitDate) {
        int numOfDays = Days.daysBetween(new DateTime(lastVisitDate).toLocalDate(), new DateTime().toLocalDate()).getDays();
        return String.format(context.getString(R.string.last_visit_days_ago),
                (numOfDays <= 1) ? context.getString(R.string.less_than_twenty_four) : numOfDays + " " + context.getString(R.string.days));
    }

    private String getUpcomingServicesDetailString(Alert upcomingServices) {
        AlertStatus alertStatus = upcomingServices.status();
        int statusStringResource;
        if (alertStatus == AlertStatus.upcoming) {
            statusStringResource = R.string.service_upcoming;
        } else if (alertStatus == AlertStatus.urgent) {
            statusStringResource = R.string.service_overdue;
        } else {
            statusStringResource = R.string.service_due;
        }
        return context.getString(statusStringResource, upcomingServices.scheduleName(), dateFormat.format(new LocalDate(upcomingServices.startDate()).toDate()));
    }

    private String getFamilyStatusString(AlertStatus alertStatus) {
        if (alertStatus == AlertStatus.complete) {
            return context.getString(R.string.family_has_nothing_due);
        } else if (alertStatus == AlertStatus.normal) {
            return context.getString(R.string.family_has_services_due);
        } else if (alertStatus == AlertStatus.urgent) {
            return context.getString(R.string.family_has_service_overdue);
        } else {
            return "";
        }
    }
}
