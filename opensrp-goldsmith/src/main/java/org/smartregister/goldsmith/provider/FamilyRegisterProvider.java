package org.smartregister.goldsmith.provider;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.Task;
import org.smartregister.family.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FamilyRegisterProvider extends org.smartregister.family.provider.FamilyRegisterProvider {

    protected final Context context;
    private final View.OnClickListener onClickListener;
    protected AsyncTask<Void, Void, Void> updateAsyncTask;

    public FamilyRegisterProvider(Context context, CommonRepository commonRepository, Set visibleColumns, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        super(context, commonRepository, visibleColumns, onClickListener, paginationClickListener);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, RegisterViewHolder viewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        String familyHeadId = pc.getColumnmaps().get("family_head");
        if (StringUtils.isNotBlank(familyHeadId)) {
            super.getView(cursor, client, viewHolder);
        }

        if (!(viewHolder.memberIcon instanceof LinearLayout)) {
            return;
        }

        ((LinearLayout) viewHolder.memberIcon).removeAllViews();

        if (updateAsyncTask != null) {
            String familyBaseEntityId = pc.getCaseId();
            Utils.startAsyncTask(new UpdateAsyncTask(context, viewHolder, familyBaseEntityId), null);
        }
    }

    public void addImageView(RegisterViewHolder viewHolder, int resourceId) {
        // TODO -> Make this public in CHW-CORE
        ImageView imageView = new ImageView(context);
        int size = convertDpToPixel(22, context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(resourceId);
        imageView.getLayoutParams().height = size;
        imageView.getLayoutParams().width = size;
        LinearLayout linearLayout = (LinearLayout) viewHolder.memberIcon;
        linearLayout.addView(imageView);
    }

    public void updateTaskStatus(Context context, RegisterViewHolder viewHolder, Task task) {
        // TODO -> Implement (update row)
        // setTaskStatus()
        // updateTaskDistance()
    }

    public void updateTaskDistance(Context context, RegisterViewHolder viewHolder, Task task) {
        // TODO -> Implement
    }

    public static int convertDpToPixel(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int getAncWomenCount(String familyBaseID) {
        return 0; // TODO -> ANC Dao is in CHW-CORE
    }

    public static int getPncWomenCount(String familyBaseID) {
        return 0; // TODO -> PNC Dao is in CHW-CORE
    }

    protected void updatePncAncIcons(org.smartregister.family.provider.FamilyRegisterProvider.RegisterViewHolder viewHolder, int womanCount, String register) {
        // TODO -> This should be generified
    }

    protected void updateChildIcons(org.smartregister.family.provider.FamilyRegisterProvider.RegisterViewHolder viewHolder, List<Map<String, String>> list, int ancWomanCount, int pncWomanCount) {
        // TODO -> This should be generified
    }

    protected void setTaskStatus(Context context, Button dueButton) {
        // TODO -> Implement
    }

    private class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private final RegisterViewHolder viewHolder;
        private final String familyBaseEntityId;

        private UpdateAsyncTask(Context context, RegisterViewHolder viewHolder, String familyBaseEntityId) {
            this.context = context;
            this.viewHolder = viewHolder;
            this.familyBaseEntityId = familyBaseEntityId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO -> Get Tasks, Child, ANC & PNC women counts
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            // TODO -> Update icons using data from doInBackground
            updateChildIcons(null, null, 0, 0);
            updateTaskStatus(null, null, null);
        }
    }

}
