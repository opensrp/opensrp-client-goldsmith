package org.smartregister.goldsmith.util;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.activity.GoldsmithTaskRegisterActivity;
import org.smartregister.goldsmith.activity.MyPerformanceActivity;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;

public class Utils {

    public static void handleBottomNavigationSelection(Activity activity, MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        Intent intent = null;
        if (menuItemId == R.id.action_my_tasks) {
            intent = new Intent(activity, GoldsmithTaskRegisterActivity.class);
        } else if (menuItemId == R.id.action_my_chws) {
            intent = new Intent(activity, BaseConfigurableRegisterActivity.class);
        } else if (menuItemId == R.id.action_performance) {
            intent = new Intent(activity, MyPerformanceActivity.class);
        } else if (menuItemId == R.id.action_training) {
            // Do nothing for now
        }
        if (intent != null) {
            activity.startActivity(intent);
            activity.finish();
        }
    }

}
