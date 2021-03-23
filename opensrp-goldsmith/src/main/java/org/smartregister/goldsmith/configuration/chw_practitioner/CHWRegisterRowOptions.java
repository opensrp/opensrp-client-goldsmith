package org.smartregister.goldsmith.configuration.chw_practitioner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.AllConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseRegisterRowOptions;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.ChwProfileActivity;
import org.smartregister.goldsmith.dao.ChwPractitionerDao;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.view.contract.SmartRegisterClient;

import java.text.MessageFormat;
import java.util.Map;

import static org.smartregister.goldsmith.util.ConfigurableRegisterUtils.fillValue;

public class CHWRegisterRowOptions extends BaseRegisterRowOptions implements View.OnClickListener {

    @Override
    public boolean isDefaultPopulatePatientColumn() {
        return true;
    }

    @Override
    public boolean useCustomViewLayout() {
        return true;
    }

    @Override
    public int getCustomViewLayoutId() {
        return R.layout.chw_register_list_row;
    }

    @Nullable
    @Override
    public BaseRegisterViewHolder createCustomViewHolder(@NonNull View itemView) {
        return new CHWRegisterRowOptions.ViewHolder(itemView);
    }

    @Override
    public boolean isCustomViewHolder() {
        return true;
    }

    @Override
    public void populateClientRow(@NonNull Cursor cursor,
                                  @NonNull CommonPersonObjectClient commonPersonObjectClient,
                                  @NonNull SmartRegisterClient smartRegisterClient,
                                  @NonNull BaseRegisterViewHolder registerViewHolder) {
        if (registerViewHolder instanceof CHWRegisterRowOptions.ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) registerViewHolder;
            fillValue(viewHolder.chwName, commonPersonObjectClient.getDetails().get("name"));
            updateTaskCompletionColumn(commonPersonObjectClient, viewHolder);

            viewHolder.chwColumn.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.CHW_COLUMN);
            viewHolder.chwColumn.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.chwColumn.setOnClickListener(this);

            viewHolder.performanceWrapper.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.PERFORMANCE_COLUMN);
            viewHolder.performanceWrapper.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.performanceWrapper.setOnClickListener(this);
        }
    }

    private void updateTaskCompletionColumn(CommonPersonObjectClient client, ViewHolder viewHolder) {
        GoldsmithApplication application = (GoldsmithApplication) GoldsmithApplication.getInstance();
        Runnable runnable = () -> {
            Map<String, String> taskCompletionDetails = ChwPractitionerDao.getPractitionerTaskCompletion(client.getDetails().get("identifier"));
            application.getAppExecutors().mainThread().execute(() -> updateCompletionViews(taskCompletionDetails, viewHolder));
        };
        application.getAppExecutors().diskIO().execute(runnable);
    }


    private void updateCompletionViews(Map<String, String> taskCompletionDetails, ViewHolder viewHolder) {
        if (taskCompletionDetails != null) {
            double totalTaskCount = Double.parseDouble(taskCompletionDetails.get(ChwPractitionerDao.TASKS));
            double tasksCompletedCount = Double.parseDouble(taskCompletionDetails.get(ChwPractitionerDao.COMPLETED));

            int percentage = totalTaskCount > 0 ? (int)((tasksCompletedCount / totalTaskCount) * 100) : 0;
            viewHolder.performancePercentage.setText(MessageFormat.format(viewHolder.performancePercentage.getContext().getString(R.string.performance_completed_percentage), percentage));
            viewHolder.tasksCompleted.setText(MessageFormat.format(viewHolder.tasksCompleted.getContext().getString(R.string.chw_row_performance_completed_fraction), tasksCompletedCount, totalTaskCount));
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            CommonPersonObjectClient chwClient = (CommonPersonObjectClient) view.getTag(org.smartregister.R.id.VIEW_CLIENT);
            goToChwProfile(chwClient, view.getContext());
        }
    }

    private void goToChwProfile(CommonPersonObjectClient chwClient, Context context) {
        Intent intent = new Intent(context, ChwProfileActivity.class);
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, chwClient);
        context.startActivity(intent);
    }

    public static class ViewHolder extends BaseRegisterViewHolder {

        public TextView chwName;
        public TextView lastSyncDay;
        public TextView performancePercentage;
        public TextView tasksCompleted;
        public View chwColumn;
        public View performanceWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            chwColumn = itemView.findViewById(R.id.chw_column);
            chwName = itemView.findViewById(R.id.chw_name);
            lastSyncDay = itemView.findViewById(R.id.chw_last_sync_day);
            performancePercentage = itemView.findViewById(R.id.tv_chw_performance_percentage);
            tasksCompleted = itemView.findViewById(R.id.tv_tasks_completed);
            performanceWrapper = itemView.findViewById(R.id.chw_performance_wrapper);
        }
    }
}
