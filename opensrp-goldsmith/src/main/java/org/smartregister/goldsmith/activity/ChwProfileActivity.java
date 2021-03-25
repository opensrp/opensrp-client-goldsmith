package org.smartregister.goldsmith.activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.smartregister.AllConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.adapter.ChwProfilePagerAdapter;
import org.smartregister.goldsmith.contract.ChwProfileContract;
import org.smartregister.goldsmith.listener.SupervisorBottomNavigationLister;
import org.smartregister.goldsmith.presenter.ChwProfilePresenter;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.view.activity.BaseProfileActivity;

public class ChwProfileActivity extends BaseProfileActivity implements ChwProfileContract.View {

    private TextView tvName;
    private TextView tvDistance;
    private TextView tvAddress;
    private TextView tvHouseholds;
    private TextView tvLastSyncDay;

    @Override
    protected void onCreation() {
        setContentView(R.layout.chw_profile);

        Toolbar toolbar = findViewById(R.id.chw_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        appBarLayout = findViewById(org.smartregister.family.R.id.toolbar_appbarlayout);
        initializePresenter();
        setupViews();
        registerBottomNavigation();
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        tvName = findViewById(R.id.tv_chw_name);
        tvDistance = findViewById(R.id.tv_distance);
        tvAddress = findViewById(R.id.tv_address);
        tvHouseholds = findViewById(R.id.tv_households);
        tvLastSyncDay = findViewById(R.id.tv_last_sync_day);

        ViewStub dashBoardTop = findViewById(R.id.dashboard_top);
        dashBoardTop.setLayoutResource(R.layout.time_frame_layout);
        dashBoardTop.inflate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chw_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO -> Implement this in v2
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        ChwProfilePagerAdapter profilePagerAdapter = new ChwProfilePagerAdapter(getSupportFragmentManager(), this.getIntent().getExtras());
        viewPager.setAdapter(profilePagerAdapter);
        return viewPager;
    }

    @Override
    protected void fetchProfileData() {
        presenter().fetchProfileData();
    }

    @Override
    public void setProfileName(String fullName) {
        tvName.setText(fullName);
    }

    @Override
    public void setChwDistance(String distance) {
        tvDistance.setText(distance);
    }

    @Override
    public void setChwAddress(String address) {
        tvAddress.setText(address);
    }

    @Override
    public void setHouseholdNumber(String households) {
        tvHouseholds.setText(households);
    }

    @Override
    public void setLastSyncDay(String lastSyncDay) {
        tvLastSyncDay.setText(lastSyncDay);
    }

    @Override
    public ChwProfileContract.Presenter presenter() {
        return (ChwProfileContract.Presenter) presenter;
    }

    @Override
    protected void initializePresenter() {
        CommonPersonObjectClient client = (CommonPersonObjectClient) getIntent().getSerializableExtra(AllConstants.INTENT_KEY.COMMON_PERSON_CLIENT);
        String identifier = client.getDetails().get("identifier");
        presenter = new ChwProfilePresenter(this, identifier);
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        presenter().fetchProfileData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter() != null) {
            presenter().onDestroy(isChangingConfigurations());
        }
    }

    private void registerBottomNavigation() {
        BottomNavigationHelper bottomNavigationHelper = new BottomNavigationHelper();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationHelper.disableShiftMode(bottomNavigationView);
        SupervisorBottomNavigationLister bottomNavigationListener = new SupervisorBottomNavigationLister(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);
        bottomNavigationView.getMenu().findItem(R.id.action_my_chws).setChecked(true);
    }
}
