package org.smartregister.goldsmith.configuration.chw;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseRegisterRowOptions;
import org.smartregister.goldsmith.R;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.view.contract.SmartRegisterClient;


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
            // TODO -> Fill display values & set tags  + listeners
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            goToChwPerformance();
        }
    }

    private void goToChwPerformance() {
        // TODO -> Launch CHW Performance activity
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
