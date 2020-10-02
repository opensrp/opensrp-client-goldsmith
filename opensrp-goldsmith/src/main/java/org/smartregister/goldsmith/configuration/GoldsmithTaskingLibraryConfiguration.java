package org.smartregister.goldsmith.configuration;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.Task;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.tasking.contract.BaseContract;
import org.smartregister.tasking.contract.BaseDrawerContract;
import org.smartregister.tasking.contract.BaseFormFragmentContract;
import org.smartregister.tasking.model.BaseTaskDetails;
import org.smartregister.tasking.model.CardDetails;
import org.smartregister.tasking.model.TaskDetails;
import org.smartregister.tasking.model.TaskFilterParams;
import org.smartregister.tasking.util.TaskingLibraryConfiguration;
import org.smartregister.tasking.viewholder.TaskRegisterViewHolder;
import org.smartregister.util.AppExecutors;

import java.util.List;
import java.util.Map;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 02-10-2020.
 */
public class GoldsmithTaskingLibraryConfiguration extends TaskingLibraryConfiguration {
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
        return null;
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
        return 0;
    }

    @Override
    public String getAdminPasswordNotNearStructures() {
        return null;
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
        return null;
    }

    @Override
    public String getCurrentOperationalAreaId() {
        return null;
    }

    @Override
    public Integer getDatabaseVersion() {
        return null;
    }

    @Override
    public void tagEventTaskDetails(List<Event> list, SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public Boolean displayDistanceScale() {
        return null;
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
    public void onTaskRegisterBindViewHolder(@NonNull Context context, @NonNull TaskRegisterViewHolder taskRegisterViewHolder, @NonNull View.OnClickListener onClickListener, @NonNull TaskDetails taskDetails, int i) {

    }

    @NonNull
    @Override
    public AppExecutors getAppExecutors() {
        return null;
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
}
