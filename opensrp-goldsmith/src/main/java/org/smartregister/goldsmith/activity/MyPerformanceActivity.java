package org.smartregister.goldsmith.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.core.job.ChwIndicatorGeneratingJob;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.fragment.ThirtyDayDashboardFragment;
import org.smartregister.goldsmith.fragment.ThreeMonthDashboardFragment;
import org.smartregister.reporting.domain.TallyStatus;
import org.smartregister.reporting.event.IndicatorTallyEvent;

import timber.log.Timber;

public class MyPerformanceActivity extends AppCompatActivity {

    private static final String REPORT_LAST_PROCESSED_DATE = "REPORT_LAST_PROCESSED_DATE";
    private ViewPager mViewPager;
    private ImageView refreshIndicatorsIcon;
    private ProgressBar refreshIndicatorsProgressBar;


    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
            refreshIndicatorsProgressBar.setVisibility(View.GONE);
            refreshIndicatorsIcon.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.indicators_updating_complete), Toast.LENGTH_LONG).show();
        }
    }

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
            if (object instanceof ThirtyDayDashboardFragment) {
                ((ThirtyDayDashboardFragment) object).loadIndicatorTallies();
            }
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
        // Create the adapter that will return a fragment
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        refreshIndicatorsIcon = findViewById(R.id.refreshIndicatorsIcon);
        refreshIndicatorsProgressBar = findViewById(R.id.refreshIndicatorsPB);
        refreshIndicatorsProgressBar.setVisibility(View.GONE);

        refreshIndicatorsIcon.setOnClickListener(view -> {
            refreshIndicatorsIcon.setVisibility(View.GONE);
            FadingCircle circle = new FadingCircle();
            refreshIndicatorsProgressBar.setIndeterminateDrawable(circle);
            refreshIndicatorsProgressBar.setVisibility(View.VISIBLE);
            refreshIndicatorData();
        });
    }

    /**
     * Refresh the indicator data by scheduling the IndicatorGeneratingJob immediately
     */
    public void refreshIndicatorData() {
        // Compute everything afresh. Last processed date is set to null to avoid messing with the processing timeline
        ChwApplication.getInstance().getContext().allSharedPreferences().savePreference(REPORT_LAST_PROCESSED_DATE, null);
        ChwIndicatorGeneratingJob.scheduleJobImmediately(ChwIndicatorGeneratingJob.TAG);
        Timber.d("ChwIndicatorGeneratingJob scheduled immediately to compute latest counts...");
        Toast.makeText(getApplicationContext(), getString(R.string.indicators_udpating), Toast.LENGTH_LONG).show();
    }

}
