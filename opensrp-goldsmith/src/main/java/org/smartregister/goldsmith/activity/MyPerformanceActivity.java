package org.smartregister.goldsmith.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.core.job.ChwIndicatorGeneratingJob;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.ThirtyDayDashboardFragment;
import org.smartregister.goldsmith.fragment.ThreeMonthDashboardFragment;
import org.smartregister.reporting.domain.TallyStatus;
import org.smartregister.reporting.event.IndicatorTallyEvent;


public class MyPerformanceActivity extends AppCompatActivity {

    private RefreshTargetsReceiver refreshTargetsReceiver = new RefreshTargetsReceiver();
    private ViewPager mViewPager;

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
        if (event.getStatus() == TallyStatus.COMPLETE) {
            if (mViewPager != null) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }
            Toast.makeText(getApplicationContext(), getString(R.string.indicators_updating_complete), Toast.LENGTH_LONG).show();
        }
    }

    // TODO -> Update this to  ViewPager2 & FragmentStateAdapter
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public @NotNull Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ThirtyDayDashboardFragment.newInstance();
                case 1:
                    return ThreeMonthDashboardFragment.newInstance();
                default:
                    return ThirtyDayDashboardFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_performance_dashboard);
        setUpView();
        ChwIndicatorGeneratingJob.scheduleJobImmediately(ChwIndicatorGeneratingJob.TAG);
    }

    private void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }

        LinearLayout back = findViewById(org.smartregister.R.id.top_left_layout);
        if (back != null)
            back.setOnClickListener(v -> finish());

        // Create the adapter that will return a fragment
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private class RefreshTargetsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getBoolean(CoreConstants.CONFIGURATION.UPDATE_REPORTING_INDICATORS)) {
                // TODO
            }
        }
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
