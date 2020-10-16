package org.smartregister.goldsmith.configuration;

import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseRegisterRowOptions;
import org.smartregister.goldsmith.R;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.customcontrols.CustomFontTextView;

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
        return R.layout.family_register_list_row;
    }

    public static void fillValue(@Nullable TextView v, @NonNull String value) {
        if (v != null) {
            v.setText(value);
        }
    }

    public static class ViewHolder extends BaseRegisterViewHolder {
        public TextView patientName;
        public TextView villageTown;
        public Button dueButton;
        public View patientColumn;
        public View memberIcon;

        public View registerColumns;
        public View dueWrapper;

        public ViewHolder(View itemView) {
            super(itemView);

            patientName = itemView.findViewById(org.smartregister.family.R.id.patient_name);

            villageTown = itemView.findViewById(org.smartregister.family.R.id.village_town);
            dueButton = itemView.findViewById(org.smartregister.family.R.id.due_button);

            patientColumn = itemView.findViewById(org.smartregister.family.R.id.patient_column);

            memberIcon = itemView.findViewById(org.smartregister.family.R.id.member_icon_layout);

            registerColumns = itemView.findViewById(org.smartregister.family.R.id.register_columns);
            dueWrapper = itemView.findViewById(org.smartregister.family.R.id.due_button_wrapper);
        }

    }
}
