package org.smartregister.goldsmith.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.PncHomeVisitActivity;
import org.smartregister.tasking.activity.TaskingHomeActivity;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.tasking.configuration.TaskRegisterV2Configuration;
import org.smartregister.tasking.contract.BaseContract;
import org.smartregister.tasking.contract.BaseDrawerContract;
import org.smartregister.tasking.contract.BaseFormFragmentContract;
import org.smartregister.tasking.contract.TaskingHomeActivityContract;
import org.smartregister.tasking.layer.DigitalGlobeLayer;
import org.smartregister.tasking.model.BaseTaskDetails;
import org.smartregister.tasking.model.CardDetails;
import org.smartregister.tasking.model.TaskDetails;
import org.smartregister.tasking.model.TaskFilterParams;
import org.smartregister.tasking.repository.TaskingMappingHelper;
import org.smartregister.tasking.util.ActivityConfiguration;
import org.smartregister.tasking.util.Constants;
import org.smartregister.tasking.util.GeoJsonUtils;
import org.smartregister.tasking.util.TaskingJsonFormUtils;
import org.smartregister.tasking.util.TaskingLibraryConfiguration;
import org.smartregister.tasking.util.TaskingMapHelper;
import org.smartregister.tasking.viewholder.PrioritizedTaskRegisterViewHolder;
import org.smartregister.util.AppExecutors;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 02-10-2020.
 */
public class GoldsmithTaskingLibraryConfiguration extends TaskingLibraryConfiguration {

    private AppExecutors appExecutors = new AppExecutors();
    private TaskRegisterConfiguration taskRegisterConfiguration;

    public GoldsmithTaskingLibraryConfiguration() {
        taskRegisterConfiguration = new TaskRegisterV2Configuration();
    }

    @NonNull
    @Override
    public Pair<Drawable, String> getActionDrawable(Context context, TaskDetails taskDetails) {
        return null;
    }

    @Override
    public int getInterventionLabel() {
        return 0;
    }

    @NonNull
    @Override
    public Float getLocationBuffer() {
        return 1f;
    }

    @Override
    public void startImmediateSync() {

    }

    @Override
    public boolean validateFarStructures() {
        return false;
    }

    @Override
    public int getResolveLocationTimeoutInSeconds() {
        return 3;
    }

    @Override
    public String getAdminPasswordNotNearStructures() {
        return "admin-password";
    }

    @Override
    public boolean isFocusInvestigation() {
        return false;
    }

    @Override
    public boolean isMDA() {
        return false;
    }

    @Override
    public String getCurrentLocationId() {
        return CoreLibrary.getInstance()
                .context()
                .allSharedPreferences()
                .fetchUserLocalityId(
                        CoreLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM());
    }

    @Override
    public String getCurrentOperationalAreaId() {
        return CoreLibrary.getInstance()
                .context()
                .allSharedPreferences()
                .fetchUserLocalityId(
                        CoreLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM());
    }

    @Override
    public Integer getDatabaseVersion() {
        return BuildConfig.DATABASE_VERSION;
    }

    @Override
    public void tagEventTaskDetails(List<Event> list, SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public Boolean displayDistanceScale() {
        return false;
    }

    @Override
    public String getFormName(@NonNull String s, @Nullable String s1) {
        return null;
    }

    @Override
    public boolean resetTaskInfo(@NonNull SQLiteDatabase sqLiteDatabase, @NonNull BaseTaskDetails baseTaskDetails) {
        return false;
    }

    @Override
    public boolean archiveClient(String s, boolean b) {
        return false;
    }

    @Override
    public String getTranslatedIRSVerificationStatus(String s) {
        return null;
    }

    @Override
    public String getTranslatedBusinessStatus(String s) {
        return null;
    }

    @Override
    public void formatCardDetails(CardDetails cardDetails) {

    }

    @Override
    public void processServerConfigs() {

    }

    @Override
    public Map<String, Integer> populateLabels() {
        return null;
    }

    @Override
    public void showBasicForm(BaseFormFragmentContract.View view, Context context, String s) {

    }

    @Override
    public void onLocationValidated(@NonNull Context context, @NonNull BaseFormFragmentContract.View view, @NonNull BaseFormFragmentContract.Interactor interactor, @NonNull BaseTaskDetails baseTaskDetails, @NonNull Location location) {

    }

    @Override
    public String generateTaskRegisterSelectQuery(String mainCondition) {
        return String.format("SELECT * FROM %s INNER JOIN %s ON %s.for = %s.baseEntityId WHERE %s", Constants.DatabaseKeys.TASK_TABLE, "client", Constants.DatabaseKeys.TASK_TABLE, "client", mainCondition);
    }

    @Override
    public String nonRegisteredStructureTasksSelect(String s) {
        return null;
    }

    @Override
    public String groupedRegisteredStructureTasksSelect(String s) {
        return null;
    }

    @Override
    public String[] taskRegisterMainColumns(String s) {
        return new String[0];
    }

    @Override
    public String familyRegisterTableName() {
        return null;
    }

    @Override
    public void saveCaseConfirmation(BaseContract.BaseInteractor baseInteractor, BaseContract.BasePresenter basePresenter, JSONObject jsonObject, String s) {

    }

    @Override
    public String calculateBusinessStatus(@NonNull org.smartregister.domain.Event event) {
        return null;
    }

    @Override
    public String getCurrentPlanId() {
        return BuildConfig.PNC_PLAN_ID;
    }

    @Override
    public boolean getSynced() {
        return false;
    }

    @Override
    public void setSynced(boolean b) {

    }

    @Override
    public boolean isMyLocationComponentEnabled() {
        return false;
    }

    @Override
    public void setMyLocationComponentEnabled(boolean b) {

    }

    @Override
    public Task generateTaskFromStructureType(@NonNull Context context, @NonNull String s, @NonNull String s1) {
        return null;
    }

    @Override
    public void saveLocationInterventionForm(BaseContract.BaseInteractor baseInteractor, BaseContract.BasePresenter basePresenter, JSONObject jsonObject) {

    }

    @Override
    public void saveJsonForm(BaseContract.BaseInteractor baseInteractor, String s) {

    }

    @Override
    public void openFilterActivity(Activity activity, TaskFilterParams taskFilterParams) {

    }

    @Override
    public void openFamilyProfile(Activity activity, CommonPersonObjectClient commonPersonObjectClient, BaseTaskDetails baseTaskDetails) {

    }

    @Override
    public void setTaskDetails(Activity activity, TaskRegisterAdapter taskRegisterAdapter, List<TaskDetails> list) {

    }

    @Override
    public void showNotFoundPopup(Activity activity, String s) {

    }

    @Override
    public void startMapActivity(Activity activity, String s, TaskFilterParams taskFilterParams) {
        activity.startActivity(new Intent(activity, TaskingHomeActivity.class));
    }

    @Override
    public void onTaskRegisterBindViewHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder taskRegisterViewHolder, @NonNull View.OnClickListener onClickListener, @NonNull TaskDetails taskDetails, int i) {
        if (taskRegisterViewHolder instanceof PrioritizedTaskRegisterViewHolder) {
            PrioritizedTaskRegisterViewHolder taskViewHolder = (PrioritizedTaskRegisterViewHolder) taskRegisterViewHolder;

            String taskTitle = "";

            taskTitle = taskDetails.getTaskCode();
            if (taskTitle != null && taskTitle.toLowerCase().startsWith("pnc day")) {

                int iconResource = -1;
                switch (taskDetails.getPriority()) {
                    case 0:
                        iconResource = R.drawable.pnc_04;
                        break;

                    case 1:
                        iconResource = R.drawable.pnc_03;
                        break;

                    case 2:
                        iconResource = R.drawable.pnc_02_offset;
                        break;

                    case 3:
                        iconResource = R.drawable.pnc_01_offset;
                }

                if (iconResource != -1) {
                    taskViewHolder.setTaskIcon(iconResource);
                }

            }

            String dob = taskDetails.getClient().getDetails().get("birthdate");
            /*if (TextUtils.isEmpty(dob)) {
                dob = taskDetails.getClient().getDetails().get("dob");
            }*/

            int entityAgeInYrs = Utils.getAgeFromDate(dob);
            /*String entityName = taskDetails.getClient().getDetails().get("first_name")+ " "
                    + taskDetails.getClient().getDetails().get("last_name") + ", " + entityAgeInYrs;*/
            String firstName = StringUtils.capitalize(taskDetails.getClient().getDetails().get("firstName"));
            String lastName = StringUtils.capitalize(taskDetails.getClient().getDetails().get("lastName"));
            String entityName = String.format("%s %s, %d", firstName, lastName, entityAgeInYrs);
            taskViewHolder.setTaskEntityName(entityName);
            taskViewHolder.setTaskTitle(taskTitle);

            Calendar authoredOn = Calendar.getInstance();
            authoredOn.setTimeInMillis(taskDetails.getAuthoredOn());
            taskViewHolder.setTaskRelativeTimeAssigned("Assigned " + org.smartregister.tasking.util.Utils.getRelativeDateTimeString(authoredOn));

            // TODO: Show the calculated distance in metres or KM
            // TODO: Switch between the call icon & the walk icon

            taskViewHolder.setAction(R.drawable.ic_directions_walk, "3 km", onClickListener);
            taskViewHolder.setTaskDetails(taskDetails);
        } else {
            Timber.i("The RecyclerView.ViewHolder is not an instance of PrioritizedTaskRegisterViewHolder");
        }
    }

    @Override
    public void onTaskRegisterItemClicked(@NonNull Activity activity, @NonNull TaskDetails taskDetails) {
        CommonPersonObjectClient client = taskDetails.getClient();
        client.setColumnmaps(client.getDetails());

        // Fetch the mother details here from ec_family_member & pass this below
        // to enable showing the names on the home visit task page & child tasks also
        String relationships = client.getDetails().get("relationships");
        int motherIdPos = relationships.indexOf("mother=[") + "mother=[".length();
        String motherId = relationships.substring(motherIdPos, motherIdPos + 36);
        /*CommonPersonObjectClient motherClient = CoreLibrary.getInstance().context().getEventClientRepository().fetchCommonPersonObjectClientByBaseEntityId(motherId);
        motherClient.setColumnmaps(motherClient.getDetails());*/

        CommonPersonObjectClient guardianClient = null;
        CommonPersonObjectClient motherClientMember = CoreLibrary.getInstance().context().getEventClientRepository()
                .fetchCommonPersonObjectClientByBaseEntityId("ec_family_member", motherId, null);
        guardianClient = motherClientMember;

        if (guardianClient == null || guardianClient.getColumnmaps() == null) {
            CommonPersonObjectClient family = CoreLibrary.getInstance().context().getEventClientRepository()
                    .fetchCommonPersonObjectClientByBaseEntityId("ec_family", motherId, null);
            guardianClient = family;
        }

        if (guardianClient != null && guardianClient.getColumnmaps() != null) {
            PncHomeVisitActivity.startMe(activity, new MemberObject(guardianClient), false);
        } else {
            Toast.makeText(activity, "The guardian client for this child could not be found", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @NonNull
    @Override
    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    @Override
    public BaseDrawerContract.View getDrawerMenuView(BaseDrawerContract.DrawerActivity drawerActivity) {
        // TODO: Fix this
        return new BaseDrawerContract.View() {
            @Override
            public Activity getContext() {
                return null;
            }

            @Override
            public void initializeDrawerLayout() {

            }

            @Override
            public void setUpViews(NavigationView navigationView) {

            }

            @Override
            public void setPlan(String campaign) {

            }

            @Override
            public void setOperationalArea(String operationalArea) {

            }

            @Override
            public String getPlan() {
                return null;
            }

            @Override
            public String getOperationalArea() {
                return null;
            }

            @Override
            public void setDistrict(String district) {

            }

            @Override
            public void setFacility(String facility, String facilityLevel) {

            }

            @Override
            public void setOperator() {

            }

            @Override
            public void lockNavigationDrawerForSelection(int title, int message) {

            }

            @Override
            public void unlockNavigationDrawer() {

            }

            @Override
            public void lockNavigationDrawerForSelection() {

            }

            @Override
            public void showOperationalAreaSelector(Pair<String, ArrayList<String>> locationHierarchy) {

            }

            @Override
            public void showPlanSelector(List<String> campaigns, String entireTreeString) {

            }

            @Override
            public void displayNotification(int title, int message, Object... formatArgs) {

            }

            @Override
            public void openDrawerLayout() {

            }

            @Override
            public void closeDrawerLayout() {

            }

            @Override
            public BaseDrawerContract.Presenter getPresenter() {
                // TODO: Fix this
                return new BaseDrawerContract.Presenter() {
                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onShowOperationalAreaSelector() {

                    }

                    @Override
                    public void onOperationalAreaSelectorClicked(ArrayList<String> name) {

                    }

                    @Override
                    public void onShowPlanSelector() {

                    }

                    @Override
                    public void onPlanSelectorClicked(ArrayList<String> value, ArrayList<String> name) {

                    }

                    @Override
                    public void onPlansFetched(Set<PlanDefinition> planDefinitionSet) {

                    }

                    @Override
                    public void unlockDrawerLayout() {

                    }

                    @Override
                    public boolean isChangedCurrentSelection() {
                        return false;
                    }

                    @Override
                    public void setChangedCurrentSelection(boolean changedCurrentSelection) {

                    }

                    @Override
                    public BaseDrawerContract.View getView() {
                        return null;
                    }

                    @Override
                    public void onViewResumed() {

                    }

                    @Override
                    public void onShowOfflineMaps() {

                    }

                    @Override
                    public boolean isPlanAndOperationalAreaSelected() {
                        return false;
                    }

                    @Override
                    public void onPlanValidated(boolean isValid) {

                    }

                    @Override
                    public void updateSyncStatusDisplay(boolean synced) {

                    }

                    @Override
                    public void startOtherFormsActivity() {

                    }

                    @Override
                    public void onShowFilledForms() {

                    }

                    @Override
                    public void checkSynced() {

                    }
                };
            }

            @Override
            public void onResume() {

            }

            @Override
            public void openOfflineMapsView() {

            }

            @Override
            public void checkSynced() {

            }

            @Override
            public void toggleProgressBarView(boolean syncing) {

            }

            @Nullable
            @Override
            public String getManifestVersion() {
                return null;
            }

            @Override
            public BaseDrawerContract.DrawerActivity getActivity() {
                return null;
            }
        };
    }

    @Override
    public void showTasksCompleteActionView(TextView textView) {

    }

    @Override
    public Map<String, Object> getServerConfigs() {
        return null;
    }

    @Override
    public TaskingJsonFormUtils getJsonFormUtils() {
        return null;
    }

    @Override
    public TaskingMappingHelper getMappingHelper() {
        return null;
    }

    @Override
    public TaskingMapHelper getMapHelper() {
        return new TaskingMapHelper();
    }

    @Override
    public boolean isRefreshMapOnEventSaved() {
        return false;
    }

    @Override
    public void setRefreshMapOnEventSaved(boolean isRefreshMapOnEventSaved) {

    }

    @Override
    public void setFeatureCollection(FeatureCollection featureCollection) {

    }

    @Override
    public DigitalGlobeLayer getDigitalGlobeLayer() {
        return new DigitalGlobeLayer();
    }

    @Override
    public List<String> getFacilityLevels() {
        return null;
    }

    @Override
    public List<String> getLocationLevels() {
        return null;
    }

    @Override
    public ActivityConfiguration getActivityConfiguration() {
        return null;
    }

    @Override
    public void registerFamily(Feature selectedFeature) {

    }

    @Override
    public void openTaskRegister(TaskFilterParams filterParams, TaskingHomeActivity taskingHomeActivity) {

    }

    @Override
    public boolean isCompassEnabled() {
        return false;
    }

    @Override
    public boolean showCurrentLocationButton() {
        return false;
    }

    @Override
    public boolean disableMyLocationOnMapMove() {
        return false;
    }

    @Override
    public boolean getDrawOperationalAreaBoundaryAndLabel() {
        return false;
    }

    @Override
    public GeoJsonUtils getGeoJsonUtils() {
        return new GeoJsonUtils();
    }

    @Override
    public String getProvinceFromTreeDialogValue(List<String> name) {
        return null;
    }

    @Override
    public String getDistrictFromTreeDialogValue(List<String> name) {
        return null;
    }

    @Override
    public void onShowFilledForms() {

    }

    @Override
    public void onFeatureSelectedByLongClick(Feature feature, TaskingHomeActivityContract.Presenter taskingHomePresenter) {

    }

    @Override
    public void onFeatureSelectedByClick(Feature feature, TaskingHomeActivityContract.Presenter taskingHomePresenter) {

    }

    @Override
    public double getOnClickMaxZoomLevel() {
        return 0;
    }

    @Override
    public void fetchPlans(String jurisdictionName, BaseDrawerContract.Presenter presenter) {

    }

    @Override
    public void validateCurrentPlan(String selectedOperationalArea, String currentPlanId, BaseDrawerContract.Presenter presenter) {

    }

    @Override
    public void setFacility(List<String> defaultLocation, BaseDrawerContract.View view) {

    }

    @Override
    public void openFilterTaskActivity(TaskFilterParams filterParams, TaskingHomeActivity activity) {

    }

    @Override
    public TaskRegisterConfiguration getTasksRegisterConfiguration() {
        return taskRegisterConfiguration;
    }

}
