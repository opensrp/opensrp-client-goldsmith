package org.smartregister.goldsmith;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.evernote.android.job.JobManager;
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
import org.smartregister.chw.core.utils.FormUtils;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.helper.JsonSpecHelper;
import org.smartregister.configuration.ModuleConfiguration;
import org.smartregister.configuration.ModuleMetadata;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.goldsmith.activity.FamilyProfileActivity;
import org.smartregister.goldsmith.activity.FamilyWizardFormActivity;
import org.smartregister.goldsmith.activity.LoginActivity;
import org.smartregister.goldsmith.configuration.AllFamiliesFormProcessor;
import org.smartregister.goldsmith.configuration.AllFamiliesRegisterActivityStarter;
import org.smartregister.goldsmith.configuration.AllFamiliesRegisterRowOptions;
import org.smartregister.goldsmith.job.GoldsmithJobCreator;
import org.smartregister.goldsmith.provider.AllFamiliesRegisterQueryProvider;
import org.smartregister.goldsmith.configuration.GoldsmithTaskingLibraryConfiguration;
import org.smartregister.goldsmith.repository.GoldsmithRepository;
import org.smartregister.growthmonitoring.GrowthMonitoringConfig;
import org.smartregister.growthmonitoring.GrowthMonitoringLibrary;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.opd.OpdLibrary;
import org.smartregister.opd.configuration.OpdConfiguration;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.tasking.util.PreferencesUtil;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.activity.FormActivity;
import org.smartregister.tasking.TaskingLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 21-09-2020.
 */
public class ChwApplication extends CoreChwApplication {

    private org.smartregister.configuration.LocationTagsConfiguration locationTagsConfiguration;

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
        JobManager.create(this).addJobCreator(new GoldsmithJobCreator());

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

        locationTagsConfiguration = new LocationTagsConfiguration();
        initializeAllFamiliesRegister();

        // TODO: Remove this and move it to some other place
        if (TextUtils.isEmpty(PreferencesUtil.getInstance().getCurrentPlanId()) && !TextUtils.isEmpty(BuildConfig.PNC_PLAN_ID)) {
            PreferencesUtil.getInstance().setCurrentPlanId(BuildConfig.PNC_PLAN_ID);
        }

        // TODO: Evaluate if to remove this setting the operational area automatically
        // TODO: Move this to after sync also
        PreferencesUtil prefsUtil = PreferencesUtil.getInstance();
        String operationalAreaName = prefsUtil.getCurrentOperationalArea();

        if (TextUtils.isEmpty(operationalAreaName)) {
            AllSharedPreferences allSharedPreferences = DrishtiApplication.getInstance().getContext().allSharedPreferences();
            operationalAreaName = LocationHelper.getInstance().getDefaultLocation();

            if (!TextUtils.isEmpty(operationalAreaName)) {
                allSharedPreferences.saveCurrentLocality(operationalAreaName);
                prefsUtil.setCurrentOperationalArea(operationalAreaName);
            }
        }
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
        //MalariaLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        //FpLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        ReportingLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);

        //TODO uncomment the below if Growth Monitoring is being used
       /* GrowthMonitoringConfig growthMonitoringConfig = new GrowthMonitoringConfig();
        growthMonitoringConfig.setWeightForHeightZScoreFile("weight_for_height.csv");
        GrowthMonitoringLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION, growthMonitoringConfig);*/
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

        // Set display date format for date pickers in native forms
        /*Form form = new Form();
        form.setDatePickerDisplayFormat("dd MMM yyyy");*/

        // set up processor
        //FamilyLibrary.getInstance().setClientProcessorForJava(ChwClientProcessor.getInstance(getApplicationContext()));
        NativeFormLibrary.getInstance().setClientFormDao(CoreLibrary.getInstance().context().getClientFormRepository());
        TaskingLibrary.init(new GoldsmithTaskingLibraryConfiguration());
    }

    public void initializeAllFamiliesRegister() {
        ModuleConfiguration allFamiliesConfiguration = new ModuleConfiguration.Builder(
                "All Families",
                AllFamiliesRegisterQueryProvider.class,
                new ConfigViewsLib(),
                AllFamiliesRegisterActivityStarter.class
        ).setModuleMetadata(new ModuleMetadata(
                "family_register",
                "ec_family",
                "Family Registration",
                null,
                null,
                "all-families",
                FormActivity.class,
                BaseFamilyProfileActivity.class,
                false,
                ""
        )).setModuleFormProcessorClass(AllFamiliesFormProcessor.class)
                .setRegisterRowOptions(AllFamiliesRegisterRowOptions.class)
                .setJsonFormActivity(FamilyWizardFormActivity.class)
                .setBottomNavigationEnabled(false)
                .setNewLayoutEnabled(true)
                .setRegisterLogo(R.drawable.ic_action_goldsmith_gold_placeholder_back)
                .build();
        CoreLibrary.getInstance().addModuleConfiguration(true, "all-families", allFamiliesConfiguration);
    }

    public void setOpenSRPUrl() {
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        preferences.savePreference(AllConstants.DRISHTI_BASE_URL, BuildConfig.opensrp_url);
    }

    @Override
    public void logoutCurrentUser() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(intent);
        context.userService().logoutSession();
    }

    @Override
    public FamilyMetadata getMetadata() {
        FamilyMetadata metadata = FormUtils.getFamilyMetadata(new FamilyProfileActivity(), getDefaultLocationLevel(), getFacilityHierarchy(), getFamilyLocationFields());

        HashMap<String, String> setting = new HashMap<>();
        setting.put(Constants.CustomConfig.FAMILY_FORM_IMAGE_STEP, JsonFormUtils.STEP1);
        setting.put(Constants.CustomConfig.FAMILY_MEMBER_FORM_IMAGE_STEP, JsonFormUtils.STEP2);
        metadata.setCustomConfigs(setting);
        return metadata;
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

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new GoldsmithRepository(getInstance().getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            Timber.e(e, "Error on getRepository: ");

        }
        return repository;
    }


    static class ConfigViewsLib implements ModuleConfiguration.ConfigurableViewsLibrary {

        @Override
        public void registerViewConfigurations(List<String> viewIdentifiers) {

        }

        @Override
        public void unregisterViewConfigurations(List<String> viewIdentifiers) {

        }
    }


    static class LocationTagsConfiguration implements org.smartregister.configuration.LocationTagsConfiguration {

        @NonNull
        @Override
        public ArrayList<String> getAllowedLevels() {
            return new ArrayList<String>(Arrays.asList("Country", "County"));
        }

        @NonNull
        @Override
        public String getDefaultLocationLevel() {
            return "County";
        }

        @NonNull
        @Override
        public ArrayList<String> getLocationLevels() {
            return new ArrayList<>(Arrays.asList("Country", "County"));
        }

        @NonNull
        @Override
        public ArrayList<String> getHealthFacilityLevels() {
            return new ArrayList<String>(Arrays.asList("County"));
        }
    }

    @Override
    public ClientProcessorForJava getClientProcessorForJava() {
        return GoldsmithClientProcessor.getInstance(getApplicationContext());
    }

    @Override
    public ClientProcessorForJava getClientProcessor() {
        return getClientProcessorForJava();
    }
}
