package org.smartregister.goldsmith.configuration.chw;

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
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.MyPerformanceActivity;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.view.contract.SmartRegisterClient;

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
            fillValue(viewHolder.chwName, commonPersonObjectClient.getDetails().get("first_name") + " " + commonPersonObjectClient.getDetails().get("last_name"));

            viewHolder.chwColumn.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.CHW_COLUMN);
            viewHolder.chwColumn.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.chwColumn.setOnClickListener(this);

            viewHolder.performanceWrapper.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.PERFORMANCE_COLUMN);
            viewHolder.performanceWrapper.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.performanceWrapper.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            CommonPersonObjectClient chwClient = (CommonPersonObjectClient) view.getTag(org.smartregister.R.id.VIEW_CLIENT);
            goToChwPerformance(chwClient, view.getContext());
        }
    }

    private void goToChwPerformance(CommonPersonObjectClient chwClient, Context context) {
        Intent intent = new Intent(context, MyPerformanceActivity.class);
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
