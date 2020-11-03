package org.smartregister.goldsmith.configuration;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configuration.BaseRegisterRowOptions;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.holders.BaseRegisterViewHolder;
import org.smartregister.view.contract.SmartRegisterClient;

import static org.smartregister.goldsmith.util.ConfigurableRegisterUtils.fillValue;

public class AncRegisterRowOptions extends BaseRegisterRowOptions implements View.OnClickListener {

    @Override
    public boolean isDefaultPopulatePatientColumn() {
        return true;
    }

    @Override
    public boolean useCustomViewLayout() {
        return true;
    }

    @Nullable
    @Override
    public BaseRegisterViewHolder createCustomViewHolder(@NonNull View itemView) {
        // TODO -> Return CustomViewHolder
        return new AncRegisterRowOptions.ViewHolder(itemView);
    }

    @Override
    public void populateClientRow(@NonNull Cursor cursor,
                                  @NonNull CommonPersonObjectClient commonPersonObjectClient,
                                  @NonNull SmartRegisterClient smartRegisterClient,
                                  @NonNull BaseRegisterViewHolder registerViewHolder) {
        if (registerViewHolder instanceof AncRegisterRowOptions.ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) registerViewHolder;
            fillValue(viewHolder.patientNameAndAge, commonPersonObjectClient.getDetails().get("first_name") + " " + commonPersonObjectClient.getDetails().get("last_name"));

            viewHolder.patientColumn.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.CLIENT_COLUMN);
            viewHolder.patientColumn.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.patientColumn.setOnClickListener(this);

            viewHolder.visitStatusWrapper.setTag(org.smartregister.R.id.VIEW_TYPE, Constants.RegisterViewConstants.Provider.ACTION_BUTTON_COLUMN);
            viewHolder.visitStatusWrapper.setTag(org.smartregister.R.id.VIEW_CLIENT, commonPersonObjectClient);
            viewHolder.visitStatusWrapper.setOnClickListener(this);
        }
    }

    @Override
    public int getCustomViewLayoutId() {
        return R.layout.goldsmith_family_register_list_row;
    }

    @Override
    public void onClick(View view) {
       if (view != null) {
           // TODO -> goToProfile()
       }
    }

    @Override
    public boolean isCustomViewHolder() {
        return true;
    }

    public static class ViewHolder extends BaseRegisterViewHolder {

        public TextView patientNameAndAge;
        public TextView day;
        public TextView distanceFromProvider;
        public TextView visitStatus;
        public View patientColumn;

        public View registerColumns;
        public View visitStatusWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            registerColumns = itemView.findViewById(R.id.register_columns);
            patientColumn = itemView.findViewById(R.id.patient_column);
            patientNameAndAge = itemView.findViewById(R.id.patient_name_and_age);
            day = itemView.findViewById(R.id.anc_day);
            distanceFromProvider = itemView.findViewById(R.id.client_distance_from_provider);
            visitStatusWrapper = itemView.findViewById(R.id.visit_status_wrapper);
            visitStatus = itemView.findViewById(R.id.tv_visit_status);
        }

        private void setDistanceFromProvider(String distanceMKm) {
            visitStatusWrapper.setVisibility(View.VISIBLE);
            distanceFromProvider.setText(distanceMKm);
        }
    }

}
