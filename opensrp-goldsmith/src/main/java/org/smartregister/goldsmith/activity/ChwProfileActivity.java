package org.smartregister.goldsmith.activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.util.Constants;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.contract.ChwProfileContract;
import org.smartregister.goldsmith.presenter.ChwProfilePresenter;
import org.smartregister.view.activity.BaseProfileActivity;

public class ChwProfileActivity extends BaseProfileActivity implements ChwProfileContract.View {

    private TextView tvName;
    private TextView tvDistance;
    private TextView tvAddress;
    private TextView tvHouseholds;
    private TextView tvLastSyncDay;
    private ViewPagerAdapter adapter;
    private String baseEntityId;

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
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        tvName = findViewById(R.id.tv_chw_name);
        tvDistance = findViewById(R.id.tv_distance);
        tvAddress = findViewById(R.id.tv_address);
        tvHouseholds = findViewById(R.id.tv_households);
        tvLastSyncDay = findViewById(R.id.tv_last_sync_day);

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
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        if (getIntent().getBooleanExtra(Constants.INTENT_KEY.GO_TO_DUE_PAGE, false)) {
            viewPager.setCurrentItem(1);
        }

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
        baseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        presenter = new ChwProfilePresenter(this, baseEntityId);
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
}
