package org.smartregister.goldsmith.listener;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.smartregister.listener.BottomNavigationListener;

public class SupervisorBottomNavigationLister extends BottomNavigationListener {

    private Activity activityContext;

    public SupervisorBottomNavigationLister(Activity activityContext) {
        super(activityContext);
        this.activityContext = activityContext;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // TODO -> Navigate to Screen selected
        activityContext.finish();
        return true;
    }

}
