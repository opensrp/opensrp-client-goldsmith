package org.smartregister.goldsmith;

import android.content.res.Configuration;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.vijay.jsonwizard.NativeFormLibrary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.configs.CoreAllClientsRegisterRowOptions;
import org.smartregister.chw.core.loggers.CrashlyticsTree;
import org.smartregister.chw.core.provider.CoreAllClientsRegisterQueryProvider;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.fp.FpLibrary;
import org.smartregister.chw.malaria.MalariaLibrary;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.helper.JsonSpecHelper;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.growthmonitoring.GrowthMonitoringConfig;
import org.smartregister.growthmonitoring.GrowthMonitoringLibrary;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.opd.OpdLibrary;
import org.smartregister.opd.configuration.OpdConfiguration;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.tasking.TaskingLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 21-09-2020.
 */
public class ChwApplication extends CoreChwApplication {

    @Override
    public void onCreate() {
        super.onCreate();


        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        context.updateCommonFtsObject(createCommonFtsObject());

        //Necessary to determine the right form to pick from assets
        CoreConstants.JSON_FORM.setLocaleAndAssetManager(ChwApplication.getCurrentLocale(),
                ChwApplication.getInstance().getApplicationContext().getAssets());

        //Setup Navigation menu. Done only once when app is created
        /*NavigationMenu.setupNavigationMenu(this, new NavigationMenuFlv(), new NavigationModelFlv(),
                getRegisteredActivities(), flavor.hasP2P());*/

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree(ChwApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM()));
        }

        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        initializeLibraries();

        // init json helper
        this.jsonSpecHelper = new JsonSpecHelper(this);

        //init Job Manager
        //JobManager.create(this).addJobCreator(new ChwJobCreator());

        initOfflineSchedules();

        setOpenSRPUrl();

        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = configuration.getLocales().get(0).getLanguage();
        } else {
            language = configuration.locale.getLanguage();
        }

        if (language.equals(Locale.FRENCH.getLanguage())) {
            saveLanguage(Locale.FRENCH.getLanguage());
        }

        // create a folder for guidebooks
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                prepareGuideBooksFolder();
            }
        } else {
            prepareGuideBooksFolder();
        }*/

        EventBus.getDefault().register(this);
    }


    private void initializeLibraries() {

        CoreLibrary.init(context, new ChwSyncConfiguration(), BuildConfig.BUILD_TIMESTAMP);
        CoreLibrary.getInstance().setEcClientFieldsFile(CoreConstants.EC_CLIENT_FIELDS);

        // init libraries
        ImmunizationLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        ConfigurableViewsLibrary.init(context);
        FamilyLibrary.init(context, getMetadata(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        AncLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        PncLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        MalariaLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        FpLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        // Init Reporting library
        ReportingLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        GrowthMonitoringConfig growthMonitoringConfig = new GrowthMonitoringConfig();
        growthMonitoringConfig.setWeightForHeightZScoreFile("weight_for_height.csv");
        GrowthMonitoringLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION, growthMonitoringConfig);
        /*
        if (hasReferrals()) {
            //Setup referral library
            ReferralLibrary.init(this);
            ReferralLibrary.getInstance().setAppVersion(BuildConfig.VERSION_CODE);
            ReferralLibrary.getInstance().setDatabaseVersion(BuildConfig.DATABASE_VERSION);
        }
        */

        OpdLibrary.init(context, getRepository(),
                new OpdConfiguration.Builder(CoreAllClientsRegisterQueryProvider.class)
                        .setBottomNavigationEnabled(true)
                        .setOpdRegisterRowOptions(CoreAllClientsRegisterRowOptions.class)
                        .build(),
                BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION
        );

        SyncStatusBroadcastReceiver.init(this);

        LocationHelper.init(new ArrayList<>(Arrays.asList(BuildConfig.HEALTH_FACILITY_LEVELS)), BuildConfig.DEFAULT_LOCATION);

        // set up processor
        //FamilyLibrary.getInstance().setClientProcessorForJava(ChwClientProcessor.getInstance(getApplicationContext()));
        NativeFormLibrary.getInstance().setClientFormDao(CoreLibrary.getInstance().context().getClientFormRepository());
        TaskingLibrary.init();
    }


    public void setOpenSRPUrl() {
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        preferences.savePreference(AllConstants.DRISHTI_BASE_URL, BuildConfig.opensrp_url);
    }

    @Override
    public FamilyMetadata getMetadata() {
        return null;
    }

    @Override
    public ArrayList<String> getAllowedLocationLevels() {
        return null;
    }

    @Override
    public ArrayList<String> getFacilityHierarchy() {
        return null;
    }

    @Override
    public String getDefaultLocationLevel() {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVisitEvent(Visit visit) {
        if (visit != null) {
            Timber.v("Visit Submitted re processing Schedule for event ' %s '  : %s", visit.getVisitType(), visit.getBaseEntityId());
           /* ChwScheduleTaskExecutor.getInstance().execute(visit.getBaseEntityId(), visit.getVisitType(), visit.getDate());

            ChildAlertService.updateAlerts(visit.getBaseEntityId());*/
        }
    }

}
