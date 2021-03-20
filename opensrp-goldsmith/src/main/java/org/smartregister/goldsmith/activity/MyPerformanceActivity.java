package org.smartregister.goldsmith.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.smartregister.chw.core.job.ChwIndicatorGeneratingJob;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.goldsmith.GoldsmithApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.adapter.ChwSectionsPagerAdapter;
import org.smartregister.goldsmith.adapter.SupervisorSectionsPagerAdapter;
import org.smartregister.goldsmith.contract.PerformanceView;
import org.smartregister.goldsmith.listener.SupervisorBottomNavigationLister;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.reporting.domain.TallyStatus;
import org.smartregister.reporting.event.IndicatorTallyEvent;


public class MyPerformanceActivity extends AppCompatActivity implements PerformanceView {

    private RefreshTargetsReceiver refreshTargetsReceiver = new RefreshTargetsReceiver();
    private ViewPager mViewPager;
    private boolean targetsSynced;
    private TallyStatus tallyStatus;
    private boolean isSupervisor;

    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(CoreConstants.ACTION.REPORTING_TARGETS_SYNCED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(refreshTargetsReceiver, filter);
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(refreshTargetsReceiver);
    }

    /**
     * Handle Indicator Tallying complete event from the Reporting lib
     * When done tallying counts, update View
     *
     * @param event The Indicator tally event we're handling
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIndicatorTallyingComplete(IndicatorTallyEvent event) {
        tallyStatus = event.getStatus();
        if (tallyStatus == TallyStatus.COMPLETE && targetsSynced) {
            updateFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_performance_dashboard);
        isSupervisor = ((GoldsmithApplication) GoldsmithApplication.getInstance()).isSupervisor();
        setUpViews();
        setTabTitles();
        ChwIndicatorGeneratingJob.scheduleJobImmediately(ChwIndicatorGeneratingJob.TAG);

        if (isSupervisor) {
            updateSupervisorTopBarView();
            registerBottomNavigation();
        }
    }

    @Override
    public void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }

        LinearLayout back = findViewById(org.smartregister.R.id.top_left_layout);
        if (back != null)
            back.setOnClickListener(v -> finish());

        ViewStub dashBoardTop = findViewById(R.id.dashboard_top);
        int dashboardTopLayout = isSupervisor ? R.layout.task_completion_layout : R.layout.task_completion_layout; // TODO -> Set time frame layout
        dashBoardTop.setLayoutResource(dashboardTopLayout);
        dashBoardTop.inflate();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStatePagerAdapter sectionsPagerAdapter = isSupervisor ? new SupervisorSectionsPagerAdapter(fragmentManager) : new ChwSectionsPagerAdapter(fragmentManager);

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private class RefreshTargetsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getBoolean(Constants.SyncConstants.UPDATE_REPORTING_INDICATORS)) {
                targetsSynced = true;
                if (tallyStatus == TallyStatus.COMPLETE) {
                    updateFragment();
                }
            }
        }
    }

    @Override
    public void updateFragment() {
        if (mViewPager != null) {
            mViewPager.getAdapter().notifyDataSetChanged();
        }
        Toast.makeText(getApplicationContext(), getString(R.string.indicators_updating_complete), Toast.LENGTH_LONG).show();
    }

    private void updateSupervisorTopBarView() {
        int superVisorColor = getResources().getColor(R.color.supervisor_scheme_blue);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setBackgroundColor(superVisorColor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(superVisorColor);
    }

    @Override
    public void setTabTitles() {
        String firstTabTitle = isSupervisor ? getString(R.string.supervisor_performance_tab_title) : getString(R.string.thirty_day_performance_tab_title);
        String secondTabTitle = isSupervisor ? getString(R.string.team_performance_tab_title) : getString(R.string.three_month_performance_tab_title);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setText(firstTabTitle);
        tabLayout.getTabAt(1).setText(secondTabTitle);
    }

    private void registerBottomNavigation() {
        BottomNavigationHelper bottomNavigationHelper = new BottomNavigationHelper();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationHelper.disableShiftMode(bottomNavigationView);
        SupervisorBottomNavigationLister bottomNavigationListener = new SupervisorBottomNavigationLister(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);
        bottomNavigationView.getMenu().findItem(R.id.action_performance).setChecked(true);
    }

    /**
     * Refresh the indicator data by scheduling the IndicatorGeneratingJob immediately
     */
    /*public void refreshIndicatorData() {
        // Compute everything afresh. Last processed date is set to null to avoid messing with the processing timeline
        ChwApplication.getInstance().getContext().allSharedPreferences().savePreference(REPORT_LAST_PROCESSED_DATE, null);
        RecurringIndicatorGeneratingJob.scheduleJobImmediately(RecurringIndicatorGeneratingJob.TAG);
        Timber.d("IndicatorGeneratingJob scheduled immediately to compute latest counts...");
        Toast.makeText(getApplicationContext(), getString(R.string.indicators_updating), Toast.LENGTH_LONG).show();
    }*/

}
