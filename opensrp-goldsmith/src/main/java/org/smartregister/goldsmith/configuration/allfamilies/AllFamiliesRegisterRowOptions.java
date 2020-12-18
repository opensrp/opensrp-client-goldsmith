package org.smartregister.goldsmith.configuration.allfamilies;

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

import org.smartregister.family.util.Utils;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.util.ConfigurableRegisterUtils;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.view.contract.SmartRegisterClient;

public class AllFamiliesRegisterRowOptions extends BaseRegisterRowOptions implements View.OnClickListener {

    // TODO: Fix this method name to be more understandable
    @Override
    public boolean isDefaultPopulatePatientColumn() {
        return true;
    }

    @Override
    public void populateClientRow(@NonNull Cursor cursor, @NonNull CommonPersonObjectClient commonPersonObjectClient, @NonNull SmartRegisterClient smartRegisterClient, @NonNull BaseRegisterViewHolder registerViewHolder) {
        // Do nothing

        if (registerViewHolder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) registerViewHolder;
            ConfigurableRegisterUtils.fillValue(viewHolder.patientName, commonPersonObjectClient.getDetails().get("first_name") + " " + commonPersonObjectClient.getDetails().get("last_name"));
            ConfigurableRegisterUtils.fillValue(viewHolder.villageTown, commonPersonObjectClient.getDetails().get("home_address"));
            /*String dobString = commonPersonObjectClient.getDetails().get("dob");

            if (dobString != null) {
                fillValue(viewHolder.patientNameAge, Utils.getAgeFromDate(dobString) + "");
            }*/

            viewHolder.patientColumn.setTag(org.smartregister.R.id.VIEW_TYPE, org.smartregister.goldsmith.util.Constants.RegisterViewConstants.Provider.CLIENT_COLUMN);
            viewHolder.patientColumn.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.patientColumn.setOnClickListener(this);

            viewHolder.tasksActionWrapper.setTag(org.smartregister.R.id.VIEW_TYPE, org.smartregister.goldsmith.util.Constants.RegisterViewConstants.Provider.ACTION_BUTTON_COLUMN);
            viewHolder.tasksActionWrapper.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.tasksActionWrapper.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if (view != null) {
            String viewType = (String) view.getTag(org.smartregister.R.id.VIEW_TYPE);
            CommonPersonObjectClient client = (CommonPersonObjectClient) view.getTag(org.smartregister.R.id.VIEW_CLIENT);
            if (org.smartregister.goldsmith.util.Constants.RegisterViewConstants.Provider.ACTION_BUTTON_COLUMN.equals(viewType)) {
                // Go to tasks?
            } else if (org.smartregister.goldsmith.util.Constants.RegisterViewConstants.Provider.CLIENT_COLUMN.equals(viewType)) {
                goToFamilyProfile(client,view.getContext());
            }
        }
    }

    private void goToFamilyProfile(CommonPersonObjectClient client, Context context) {
        Intent intent = new Intent(context, Utils.metadata().profileActivity);
        intent.putExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT, client);
        context.startActivity(intent);
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
