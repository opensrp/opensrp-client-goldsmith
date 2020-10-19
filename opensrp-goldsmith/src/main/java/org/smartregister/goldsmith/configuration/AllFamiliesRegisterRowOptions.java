package org.smartregister.goldsmith.configuration;

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

public class AllFamiliesRegisterRowOptions extends BaseRegisterRowOptions {

    // TODO: Fix this method name to be more understandable
    @Override
    public boolean isDefaultPopulatePatientColumn() {
        return true;
    }

    @Override
    public void populateClientRow(@NonNull Cursor cursor, @NonNull CommonPersonObjectClient commonPersonObjectClient, @NonNull SmartRegisterClient smartRegisterClient, @NonNull BaseRegisterViewHolder opdRegisterViewHolder) {
        // Do nothing

        if (opdRegisterViewHolder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) opdRegisterViewHolder;
            fillValue(viewHolder.patientName, commonPersonObjectClient.getDetails().get("first_name") + " " + commonPersonObjectClient.getDetails().get("last_name"));
            fillValue(viewHolder.villageTown, commonPersonObjectClient.getDetails().get("home_address"));
            /*String dobString = commonPersonObjectClient.getDetails().get("dob");

            if (dobString != null) {
                fillValue(viewHolder.patientNameAge, Utils.getAgeFromDate(dobString) + "");
            }*/
        }
    }

    @Override
    public boolean isCustomViewHolder() {
        return true;
    }

    @Nullable
    @Override
    public BaseRegisterViewHolder createCustomViewHolder(@NonNull View itemView) {
        // TODO -> Return CustomViewHolder
        return new ViewHolder(itemView);
    }

    @Override
    public boolean useCustomViewLayout() {
        return true;
    }

    @Override
    public int getCustomViewLayoutId() {
        return R.layout.goldsmith_family_register_list_row;
    }

    public static void fillValue(@Nullable TextView v, @NonNull String value) {
        if (v != null) {
            v.setText(value);
        }
    }

    public static class ViewHolder extends BaseRegisterViewHolder {
        public TextView patientName;
        public TextView villageTown;
        public TextView distanceFromProvider;
        public TextView tasksDesc;
        public View patientColumn;
        public View memberIcon;

        public View registerColumns;
        public View tasksActionWrapper;

        public ViewHolder(View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patient_name);
            distanceFromProvider = itemView.findViewById(R.id.client_distance_from_provider);
            tasksDesc = itemView.findViewById(R.id.tv_tasks_content);

            villageTown = itemView.findViewById(R.id.village_town);

            patientColumn = itemView.findViewById(R.id.patient_column);

            memberIcon = itemView.findViewById(R.id.member_icon_layout);

            registerColumns = itemView.findViewById(R.id.register_columns);
            tasksActionWrapper = itemView.findViewById(R.id.task_action_wrapper);
        }

        private void setDistanceFromProvider(String distanceMKm) {
            tasksActionWrapper.setVisibility(View.VISIBLE);
            distanceFromProvider.setText(distanceMKm);
        }

    }
}
