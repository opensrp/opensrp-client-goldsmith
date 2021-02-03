package org.smartregister.goldsmith.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import org.smartregister.chw.anc.activity.BaseAncHomeVisitActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.AncHomeVisitActivity;
import org.smartregister.goldsmith.activity.PncHomeVisitActivity;
import org.smartregister.tasking.activity.TaskingHomeActivity;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.tasking.configuration.DefaultTaskingLibraryConfiguration;
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
import org.smartregister.tasking.util.TaskingConstants;
import org.smartregister.tasking.util.TaskingJsonFormUtils;
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
public class GoldsmithTaskingLibraryConfiguration extends DefaultTaskingLibraryConfiguration {

    private AppExecutors appExecutors = new AppExecutors();
    private TaskRegisterConfiguration taskRegisterConfiguration;
    private TaskingMapHelper taskingMapHelper;
    private MapConfiguration mapConfiguration;

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
        // Do nothing for now
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
        // Do nothing for now
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
        // Do nothing for now
    }

    @Override
    public void processServerConfigs() {
        // Do nothing for now
    }

    @Override
    public Map<String, Integer> populateLabels() {
        return null;
    }

    @Override
    public void showBasicForm(BaseFormFragmentContract.View view, Context context, String s) {
        // Do nothing for now
    }

    @Override
    public void onLocationValidated(@NonNull Context context, @NonNull BaseFormFragmentContract.View view, @NonNull BaseFormFragmentContract.Interactor interactor, @NonNull BaseTaskDetails baseTaskDetails, @NonNull Location location) {
        // Do nothing for now
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
        // Do nothing for now
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
        // Do nothing for now
    }

    @Override
    public boolean isMyLocationComponentEnabled() {
        return false;
    }

    @Override
    public void setMyLocationComponentEnabled(boolean b) {
        // Do nothing for now
    }

    @Override
    public Task generateTaskFromStructureType(@NonNull Context context, @NonNull String s, @NonNull String s1) {
        return null;
    }

    @Override
    public void saveLocationInterventionForm(BaseContract.BaseInteractor baseInteractor, BaseContract.BasePresenter basePresenter, JSONObject jsonObject) {
        // Do nothing for now
    }

    @Override
    public void saveJsonForm(BaseContract.BaseInteractor baseInteractor, String s) {
        // Do nothing for now
    }

    @Override
    public void openFilterActivity(Activity activity, TaskFilterParams taskFilterParams) {
        // Do nothing for now
    }

    @Override
    public void openFamilyProfile(Activity activity, CommonPersonObjectClient commonPersonObjectClient, BaseTaskDetails baseTaskDetails) {
        // Do nothing for now
    }

    @Override
    public void setTaskDetails(Activity activity, TaskRegisterAdapter taskRegisterAdapter, List<TaskDetails> list) {
        // Do nothing for now
    }

    @Override
    public void showNotFoundPopup(Activity activity, String s) {
        // Do nothing for now
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

                int iconResource;
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
                        break;
                    default:
                        iconResource = -1;
                        break;
                }

                if (iconResource != -1) {
                    taskViewHolder.setTaskIcon(iconResource);
                }

            }


            if (taskTitle != null && taskTitle.toLowerCase().startsWith("anc contact")) {

                int iconResource;
                switch (taskDetails.getPriority()) {
                    case 0:
                        iconResource = R.drawable.anc_04;
                        break;

                    case 1:
                        iconResource = R.drawable.anc_03;
                        break;

                    case 2:
                        iconResource = R.drawable.anc_02_offset;
                        break;

                    case 3:
                        iconResource = R.drawable.anc_01_offset;
                        break;

                    default:
                        iconResource = -1;
                        break;
                }

                if (iconResource != -1) {
                    taskViewHolder.setTaskIcon(iconResource);
                }
            }

            // TODO: Fix dob for birth approximations
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
        String taskCode = taskDetails.getTaskCode().toLowerCase();
        if (taskCode.startsWith("pnc day")) {
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
                PncHomeVisitActivity.startMe((Context) activity, new MemberObject(guardianClient), false);
            } else {
                Toast.makeText(activity, "The guardian client for this child could not be found", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (taskCode.startsWith("anc contact")) {
            CommonPersonObjectClient client = taskDetails.getClient();
            client.setColumnmaps(client.getDetails());

            String baseEntityId = client.getCaseId();
            if (TextUtils.isEmpty(baseEntityId)) {
                baseEntityId = client.getDetails().get("baseEntityId");
            }

            Intent intent = new Intent(activity, BaseAncHomeVisitActivity.class);
            intent.putExtra("MemberObject", new MemberObject(client));
            intent.putExtra("editMode", false);
            //activity.startActivity(intent);

            AncHomeVisitActivity.startMe((Context) activity, baseEntityId, false);
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
        return new TaskingLibBaseDrawerContractImpl();
    }

    @Override
    public void showTasksCompleteActionView(TextView textView) {
        // Do nothing for now
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
        if (taskingMapHelper == null) {
            taskingMapHelper = new TaskingMapHelper();
        }
        return taskingMapHelper;
    }

    @Override
    public boolean isRefreshMapOnEventSaved() {
        return false;
    }

    @Override
    public void setRefreshMapOnEventSaved(boolean isRefreshMapOnEventSaved) {
        // Do nothing for now
    }

    @Override
    public void setFeatureCollection(FeatureCollection featureCollection) {
        // Do nothing for now
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
        // Do nothing for now
    }

    @Override
    public void openTaskRegister(TaskFilterParams filterParams, TaskingHomeActivity taskingHomeActivity) {
        // Do nothing for now
    }

    @Override
    public boolean isCompassEnabled() {
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
        // Do nothing for now
    }

    @Override
    public void onFeatureSelectedByLongClick(Feature feature, TaskingHomeActivityContract.Presenter taskingHomePresenter) {
        // Do nothing for now
    }

    @Override
    public void onFeatureSelectedByClick(Feature feature, TaskingHomeActivityContract.Presenter taskingHomePresenter) {
        if (taskingHomePresenter.getView() == null || taskingHomePresenter.getView().getContext() == null) {
            return;
        }

        Context context = taskingHomePresenter.getView().getContext();

        String taskCode = feature.getStringProperty(TaskingConstants.Properties.TASK_CODE);
        if (TextUtils.isEmpty(taskCode)) {
            Toast.makeText(context, "Task does not have a task code", Toast.LENGTH_LONG).show();
            return;
        }

        taskCode = taskCode.toLowerCase();

        if (taskCode.startsWith("pnc day")) {
            String motherId = feature.getStringProperty(TaskingConstants.Properties.MOTHER_ID);

            if (motherId != null) {
                CommonPersonObjectClient guardianClient = CoreLibrary.getInstance().context().getEventClientRepository()
                        .fetchCommonPersonObjectClientByBaseEntityId("ec_family_member", motherId, null);

                if (guardianClient == null || guardianClient.getColumnmaps() == null) {
                    CommonPersonObjectClient family = CoreLibrary.getInstance().context().getEventClientRepository()
                            .fetchCommonPersonObjectClientByBaseEntityId("ec_family", motherId, null);
                    guardianClient = family;
                }

                if (guardianClient != null && guardianClient.getColumnmaps() != null) {
                    PncHomeVisitActivity.startMe(context, new MemberObject(guardianClient), false);
                } else {
                    Toast.makeText(context, "The guardian client for this child could not be found", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                Toast.makeText(context, "The guardian client for this child could not be found", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (taskCode.startsWith("anc contact")) {

            String baseEntityId = feature.getStringProperty(TaskingConstants.Properties.BASE_ENTITY_ID);
            if (TextUtils.isEmpty(baseEntityId)) {
                return;
            }

            AncHomeVisitActivity.startMe(context, baseEntityId, false);
        }
    }

    @Override
    public void fetchPlans(String jurisdictionName, BaseDrawerContract.Presenter presenter) {
        // Do nothing for now
    }

    @Override
    public void validateCurrentPlan(String selectedOperationalArea, String currentPlanId, BaseDrawerContract.Presenter presenter) {
        // Do nothing for now
    }

    @Override
    public void setFacility(List<String> defaultLocation, BaseDrawerContract.View view) {
        // Do nothing for now
    }

    @Override
    public void openFilterTaskActivity(TaskFilterParams filterParams, TaskingHomeActivity activity) {
        // Do nothing for now
    }

    @Override
    public TaskRegisterConfiguration getTasksRegisterConfiguration() {
        return taskRegisterConfiguration;
    }

    @Nullable
    @Override
    public MapConfiguration getMapConfiguration() {
        if (mapConfiguration == null) {
            mapConfiguration = new GoldsmithMapConfiguration();
        }

        return mapConfiguration;
    }

    @Override
    public boolean showCurrentLocationButton() {
        return true;
    }

    public class TaskingLibBaseDrawerContractImpl implements BaseDrawerContract.View {

        @Override
        public Activity getContext() {
            return null;
        }

        @Override
        public void initializeDrawerLayout() {
            // Do nothing for now
        }

        @Override
        public void setUpViews(NavigationView navigationView) {
            // Do nothing for now
        }

        @Override
        public void setPlan(String campaign) {
            // Do nothing for now
        }

        @Override
        public void setOperationalArea(String operationalArea) {
            // Do nothing for now
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
            // Do nothing for now
        }

        @Override
        public void setFacility(String facility, String facilityLevel) {
            // Do nothing for now
        }

        @Override
        public void setOperator() {
            // Do nothing for now
        }

        @Override
        public void lockNavigationDrawerForSelection(int title, int message) {
            // Do nothing for now
        }

        @Override
        public void unlockNavigationDrawer() {
            // Do nothing for now
        }

        @Override
        public void lockNavigationDrawerForSelection() {
            // Do nothing for now
        }

        @Override
        public void showOperationalAreaSelector(Pair<String, ArrayList<String>> locationHierarchy) {
            // Do nothing for now
        }

        @Override
        public void showPlanSelector(List<String> campaigns, String entireTreeString) {
            // Do nothing for now
        }

        @Override
        public void displayNotification(int title, int message, Object... formatArgs) {
            // Do nothing for now
        }

        @Override
        public void openDrawerLayout() {
            // Do nothing for now
        }

        @Override
        public void closeDrawerLayout() {
            // Do nothing for now
        }

        @Override
        public BaseDrawerContract.Presenter getPresenter() {
            // TODO: Fix this
            return new BaseDrawerContract.Presenter() {
                @Override
                public void onDrawerClosed() {
                    // Do nothing for now
                }

                @Override
                public void onShowOperationalAreaSelector() {
                    // Do nothing for now
                }

                @Override
                public void onOperationalAreaSelectorClicked(ArrayList<String> name) {
                    // Do nothing for now
                }

                @Override
                public void onShowPlanSelector() {
                    // Do nothing for now
                }

                @Override
                public void onPlanSelectorClicked(ArrayList<String> value, ArrayList<String> name) {
                    // Do nothing for now
                }

                @Override
                public void onPlansFetched(Set<PlanDefinition> planDefinitionSet) {
                    // Do nothing for now
                }

                @Override
                public void unlockDrawerLayout() {
                    // Do nothing for now
                }

                @Override
                public boolean isChangedCurrentSelection() {
                    return false;
                }

                @Override
                public void setChangedCurrentSelection(boolean changedCurrentSelection) {
                    // Do nothing for now
                }

                @Override
                public BaseDrawerContract.View getView() {
                    return null;
                }

                @Override
                public void onViewResumed() {
                    // Do nothing for now
                }

                @Override
                public void onShowOfflineMaps() {
                    // Do nothing for now
                }

                @Override
                public boolean isPlanAndOperationalAreaSelected() {
                    return false;
                }

                @Override
                public void onPlanValidated(boolean isValid) {
                    // Do nothing for now
                }

                @Override
                public void updateSyncStatusDisplay(boolean synced) {
                    // Do nothing for now
                }

                @Override
                public void startOtherFormsActivity() {
                    // Do nothing for now
                }

                @Override
                public void onShowFilledForms() {
                    // Do nothing for now
                }

                @Override
                public void checkSynced() {
                    // Do nothing for now
                }
            };
        }

        @Override
        public void onResume() {
            // Do nothing for now
        }

        @Override
        public void openOfflineMapsView() {
            // Do nothing for now
        }

        @Override
        public void checkSynced() {
            // Do nothing for now
        }

        @Override
        public void toggleProgressBarView(boolean syncing) {
            // Do nothing for now
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
    }

}
