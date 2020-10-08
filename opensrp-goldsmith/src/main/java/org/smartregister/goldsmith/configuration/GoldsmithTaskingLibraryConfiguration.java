package org.smartregister.goldsmith.configuration;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.Task;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.R;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.tasking.configuration.TaskRegisterV2Configuration;
import org.smartregister.tasking.contract.BaseContract;
import org.smartregister.tasking.contract.BaseDrawerContract;
import org.smartregister.tasking.contract.BaseFormFragmentContract;
import org.smartregister.tasking.model.BaseTaskDetails;
import org.smartregister.tasking.model.CardDetails;
import org.smartregister.tasking.model.TaskDetails;
import org.smartregister.tasking.model.TaskFilterParams;
import org.smartregister.tasking.util.CardDetailsUtil;
import org.smartregister.tasking.util.TaskingLibraryConfiguration;
import org.smartregister.tasking.viewholder.PrioritizedTaskRegisterViewHolder;
import org.smartregister.util.AppExecutors;
import org.smartregister.util.Utils;

import java.util.List;
import java.util.Map;

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
    public String mainSelect(String s) {
        return null;
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
        return null;
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

    }

    @Override
    public void onTaskRegisterBindViewHolder(@NonNull Context context, @NonNull RecyclerView.ViewHolder taskRegisterViewHolder, @NonNull View.OnClickListener onClickListener, @NonNull TaskDetails taskDetails, int i) {
        if (taskRegisterViewHolder instanceof PrioritizedTaskRegisterViewHolder) {
            PrioritizedTaskRegisterViewHolder taskViewHolder = (PrioritizedTaskRegisterViewHolder) taskRegisterViewHolder;

            String taskTitle = "";

            if ("pnc_visit".equals(taskDetails.getTaskCode())) {
                taskTitle = "PNC Visit Day 2";
            }

            taskViewHolder.setTaskIcon(R.drawable.ic_temp_pnc_visit_icon);
            int entityAgeInYrs = Utils.getAgeFromDate(taskDetails.getClient().getDetails().get("dob"));
            String entityName = taskDetails.getClient().getDetails().get("first_name")+ " "
                    + taskDetails.getClient().getDetails().get("last_name") + ", " + entityAgeInYrs;
            taskViewHolder.setTaskEntityName(entityName);
            taskViewHolder.setTaskTitle(taskTitle);
            taskViewHolder.setTaskRelativeTimeAssigned("Assigned today");
            taskViewHolder.setAction(R.drawable.ic_action_walk, "3 km", onClickListener);

        } else {
            Timber.i("The RecyclerView.ViewHolder is not an instance of PrioritizedTaskRegisterViewHolder");
        }
    }

    @NonNull
    @Override
    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    @Override
    public BaseDrawerContract.View getDrawerMenuView(BaseDrawerContract.DrawerActivity drawerActivity) {
        return null;
    }

    @Override
    public void showTasksCompleteActionView(TextView textView) {

    }

    @Override
    public Map<String, Object> getServerConfigs() {
        return null;
    }

    @Override
    public TaskRegisterConfiguration getTasksRegisterConfiguration() {
        return taskRegisterConfiguration;
    }
}
