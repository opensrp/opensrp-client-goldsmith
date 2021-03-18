package org.smartregister.goldsmith;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.evernote.android.job.JobManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.vijay.jsonwizard.NativeFormLibrary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.configs.CoreAllClientsRegisterRowOptions;
import org.smartregister.chw.core.loggers.CrashlyticsTree;
import org.smartregister.chw.core.provider.CoreAllClientsRegisterQueryProvider;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.FormUtils;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.helper.JsonSpecHelper;
import org.smartregister.configuration.ModuleConfiguration;
import org.smartregister.configuration.ModuleMetadata;
import org.smartregister.configuration.ModuleRegister;
import org.smartregister.dto.UserAssignmentDTO;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.goldsmith.activity.FamilyProfileActivity;
import org.smartregister.goldsmith.activity.FamilyWizardFormActivity;
import org.smartregister.goldsmith.activity.LoginActivity;
import org.smartregister.goldsmith.configuration.AncPncToolbarOptions;
import org.smartregister.goldsmith.configuration.GoldsmithTaskingLibraryConfiguration;
import org.smartregister.goldsmith.configuration.ToolbarOptions;
import org.smartregister.goldsmith.configuration.allfamilies.AllFamiliesFormProcessor;
import org.smartregister.goldsmith.configuration.allfamilies.AllFamiliesRegisterActivityStarter;
import org.smartregister.goldsmith.configuration.allfamilies.AllFamiliesRegisterRowOptions;
import org.smartregister.goldsmith.configuration.anc.AncFormProcessor;
import org.smartregister.goldsmith.configuration.anc.AncMemberProfileOptions;
import org.smartregister.goldsmith.configuration.anc.AncRegisterActivityStarter;
import org.smartregister.goldsmith.configuration.anc.AncRegisterRowOptions;
import org.smartregister.goldsmith.configuration.chw.CHWRegisterActivityStarter;
import org.smartregister.goldsmith.configuration.chw.CHWRegisterRowOptions;
import org.smartregister.goldsmith.configuration.chw.CHWToolbarOptions;
import org.smartregister.goldsmith.configuration.pnc.PncFormProcessor;
import org.smartregister.goldsmith.configuration.pnc.PncMemberProfileOptions;
import org.smartregister.goldsmith.configuration.pnc.PncRegisterActivityStarter;
import org.smartregister.goldsmith.configuration.pnc.PncRegisterRowOptions;
import org.smartregister.goldsmith.contract.EventTaskIdProvider;
import org.smartregister.goldsmith.contract.EventTaskIdProviderImpl;
import org.smartregister.goldsmith.job.GoldsmithJobCreator;
import org.smartregister.goldsmith.provider.AllFamiliesRegisterQueryProvider;
import org.smartregister.goldsmith.provider.AncRegisterQueryProvider;
import org.smartregister.goldsmith.provider.CHWRegisterQueryProvider;
import org.smartregister.goldsmith.provider.PncRegisterQueryProvider;
import org.smartregister.goldsmith.repository.GoldsmithRepository;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.opd.OpdLibrary;
import org.smartregister.opd.configuration.OpdConfiguration;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.receiver.ValidateAssignmentReceiver;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.tasking.TaskingLibrary;
import org.smartregister.tasking.util.PreferencesUtil;
import org.smartregister.tasking.util.TaskingConstants;
import org.smartregister.view.activity.FormActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 21-09-2020.
 */
public class GoldsmithApplication extends CoreChwApplication implements ValidateAssignmentReceiver.UserAssignmentListener {

    private org.smartregister.configuration.LocationTagsConfiguration locationTagsConfiguration;
    protected EventTaskIdProvider eventTaskIdProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());

        CommonFtsObject commonFtsObject = getCommonFtsObject();
        context.updateCommonFtsObject(commonFtsObject);

        //Necessary to determine the right form to pick from assets
        CoreConstants.JSON_FORM.setLocaleAndAssetManager(GoldsmithApplication.getCurrentLocale(),
                GoldsmithApplication.getInstance().getApplicationContext().getAssets());

        //Setup Navigation menu. Done only once when app is created
        /*NavigationMenu.setupNavigationMenu(this, new NavigationMenuFlv(), new NavigationModelFlv(),
                getRegisteredActivities(), flavor.hasP2P());*/

        if (BuildConfig.DEBUG) {
            if (Timber.forest().size() == 0) {
                Timber.plant(new Timber.DebugTree());
            }
        } else {
            //TODO: This CrashlyticsTree needs to be updated
            Timber.plant(new CrashlyticsTree(GoldsmithApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM()));
        }

        //Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

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

        initializeRegisters();

        // TODO: Remove this and move it to some other place
        if (TextUtils.isEmpty(PreferencesUtil.getInstance().getCurrentPlanId()) && !TextUtils.isEmpty(BuildConfig.PNC_PLAN_ID)) {
            PreferencesUtil.getInstance().setCurrentPlanId(BuildConfig.PNC_PLAN_ID);
        }

        Mapbox.getInstance(this, BuildConfig.MAPBOX_SDK_ACCESS_TOKEN);

        ValidateAssignmentReceiver.init(this);
        ValidateAssignmentReceiver.getInstance().addListener(this);

        //throw new RuntimeException("Some exception occurred");
    }

    @NotNull
    protected CommonFtsObject getCommonFtsObject() {
        CommonFtsObject commonFtsObject = createCommonFtsObject();
        String[] previousTables = commonFtsObject.getTables();
        String[] tables = new String[previousTables.length + 1];

        for (int i = 0; i < tables.length - 1; i++) {
            tables[i] = previousTables[i];
        }

        tables[tables.length - 1] = TaskingConstants.Tables.STRUCTURE_FAMILY_RELATIONSHIP;

        CommonFtsObject updatedFtsObject = new CommonFtsObject(tables);
        for (String table : commonFtsObject.getTables()) {
            updatedFtsObject.updateSearchFields(table, commonFtsObject.getSearchFields(table));
            updatedFtsObject.updateSortFields(table, commonFtsObject.getSortFields(table));
        }

        updatedFtsObject.updateSearchFields(TaskingConstants.Tables.STRUCTURE_FAMILY_RELATIONSHIP, new String[]{"family_base_entity_id"});
        return updatedFtsObject;
    }

    private void initializeRegisters() {
        if (isSupervisor()) {
            initializeCHWRegister();
        } else {
            initializeAllFamiliesRegister();
            initializeAncRegisters();
            initializePncRegisters();
        }
    }


    private void initializeLibraries() {
        // init libraries

        CoreLibrary.init(context, new GoldsmithSyncConfiguration(), BuildConfig.BUILD_TIMESTAMP);
        CoreLibrary.getInstance().setEcClientFieldsFile(CoreConstants.EC_CLIENT_FIELDS);
        ImmunizationLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        ConfigurableViewsLibrary.init(context);
        FamilyLibrary.init(context, getMetadata(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        AncLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        AncLibrary.getInstance().setSubmitOnSave(true);
        PncLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        ReportingLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);

        //TODO uncomment the below if Growth Monitoring is being used
       /* GrowthMonitoringConfig growthMonitoringConfig = new GrowthMonitoringConfig();
        growthMonitoringConfig.setWeightForHeightZScoreFile("weight_for_height.csv");
        GrowthMonitoringLibrary.init(context, getRepository(), BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION, growthMonitoringConfig);
        */

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

        NativeFormLibrary.getInstance().setClientFormDao(CoreLibrary.getInstance().context().getClientFormRepository());
        TaskingLibrary.init(new GoldsmithTaskingLibraryConfiguration());
    }

    public void initializeAllFamiliesRegister() {
        ModuleConfiguration allFamiliesConfiguration = new ModuleConfiguration.Builder(
                Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES,
                AllFamiliesRegisterQueryProvider.class,
                new ConfigViewsLib(),
                AllFamiliesRegisterActivityStarter.class
        ).setModuleMetadata(new ModuleMetadata(
                new ModuleRegister(
                        "family_register",
                        CoreConstants.TABLE_NAME.FAMILY_MEMBER,
                        CoreConstants.EventType.FAMILY_REGISTRATION,
                        CoreConstants.EventType.UPDATE_FAMILY_REGISTRATION,
                        Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES),
                locationTagsConfiguration,
                FormActivity.class,
                BaseFamilyProfileActivity.class,
                false,
                ""
        )).setModuleFormProcessorClass(AllFamiliesFormProcessor.class)
                .setRegisterRowOptions(AllFamiliesRegisterRowOptions.class)
                .setJsonFormActivity(FamilyWizardFormActivity.class)
                .setBottomNavigationEnabled(false)
                .setToolbarOptions(ToolbarOptions.class)
                .build();
        CoreLibrary.getInstance().addModuleConfiguration(true,
                Constants.RegisterViewConstants.ModuleOptions.ALL_FAMILIES,
                allFamiliesConfiguration);
    }

    public void initializeAncRegisters() {
        ModuleConfiguration ancModuleConfiguration = new ModuleConfiguration.Builder(
                Constants.RegisterViewConstants.ModuleOptions.ANC,
                AncRegisterQueryProvider.class,
                new ConfigViewsLib(),
                AncRegisterActivityStarter.class
        ).setModuleMetadata(new ModuleMetadata(
                new ModuleRegister("anc_member_registration",
                        CoreConstants.TABLE_NAME.ANC_MEMBER,
                        CoreConstants.EventType.ANC_REGISTRATION,
                        CoreConstants.EventType.UPDATE_ANC_REGISTRATION,
                        Constants.RegisterViewConstants.ModuleOptions.ANC),
                locationTagsConfiguration,
                FormActivity.class,
                BaseAncMemberProfileActivity.class,
                false,
                ""
        )).setModuleFormProcessorClass(AncFormProcessor.class)
                .setRegisterRowOptions(AncRegisterRowOptions.class)
                .setJsonFormActivity(FamilyWizardFormActivity.class)
                .setBottomNavigationEnabled(false)
                .setToolbarOptions(AncPncToolbarOptions.class)
                .setMemberProfileOptionsClass(AncMemberProfileOptions.class)
                .build();
        CoreLibrary.getInstance().addModuleConfiguration(false,
                Constants.RegisterViewConstants.ModuleOptions.ANC,
                ancModuleConfiguration);
    }

    public void initializePncRegisters() {
        ModuleConfiguration pncModuleConfiguration = new ModuleConfiguration.Builder(
                Constants.RegisterViewConstants.ModuleOptions.PNC,
                PncRegisterQueryProvider.class,
                new ConfigViewsLib(),
                PncRegisterActivityStarter.class
        ).setModuleMetadata(new ModuleMetadata(
                new ModuleRegister("pregnancy_outcome",
                        CoreConstants.TABLE_NAME.PNC_MEMBER,
                        Constants.EventType.PREGNANCY_OUTCOME, null,
                        Constants.RegisterViewConstants.ModuleOptions.PNC),
                locationTagsConfiguration,
                FormActivity.class,
                BasePncMemberProfileActivity.class,
                false,
                ""
        )).setModuleFormProcessorClass(PncFormProcessor.class)
                .setRegisterRowOptions(PncRegisterRowOptions.class)
                .setJsonFormActivity(FamilyWizardFormActivity.class)
                .setBottomNavigationEnabled(false)
                .setToolbarOptions(AncPncToolbarOptions.class)
                .setMemberProfileOptionsClass(PncMemberProfileOptions.class)
                .build();
        CoreLibrary.getInstance().addModuleConfiguration(
                false,
                Constants.RegisterViewConstants.ModuleOptions.PNC,
                pncModuleConfiguration);
    }

    public void initializeCHWRegister() {
        ModuleConfiguration chwModuleConfiguration = new ModuleConfiguration.Builder(
                Constants.RegisterViewConstants.ModuleOptions.CHW,
                CHWRegisterQueryProvider.class,
                new ConfigViewsLib(),
                CHWRegisterActivityStarter.class
        ).setModuleMetadata(new ModuleMetadata(
                new ModuleRegister("",
                        Constants.GoldsmithTableName.CHW_MEMBER,
                        null, null, // We're not processing any of these events?
                        Constants.RegisterViewConstants.ModuleOptions.CHW),
                locationTagsConfiguration,
                FormActivity.class,
                null,
                false,
                ""
        )).setRegisterRowOptions(CHWRegisterRowOptions.class)
                .setBottomNavigationEnabled(true)
                .setToolbarOptions(CHWToolbarOptions.class)
                .build();
        CoreLibrary.getInstance().addModuleConfiguration(
                true, // This is the only module in Supervisor mode
                Constants.RegisterViewConstants.ModuleOptions.CHW,
                chwModuleConfiguration);
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
        setting.put(org.smartregister.family.util.Constants.CustomConfig.FAMILY_FORM_IMAGE_STEP, JsonFormUtils.STEP1);
        setting.put(org.smartregister.family.util.Constants.CustomConfig.FAMILY_MEMBER_FORM_IMAGE_STEP, JsonFormUtils.STEP2);
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

    @Override
    public void onUserAssignmentRevoked(UserAssignmentDTO userAssignmentDTO) {
        // Do nothing for now
    }


    static class ConfigViewsLib implements ModuleConfiguration.ConfigurableViewsLibrary {

        @Override
        public void registerViewConfigurations(List<String> viewIdentifiers) {
            // Do nothing for now
        }

        @Override
        public void unregisterViewConfigurations(List<String> viewIdentifiers) {
            // Do nothing for now
        }
    }


    static class LocationTagsConfiguration implements org.smartregister.configuration.LocationTagsConfiguration {

        @NonNull
        @Override
        public ArrayList<String> getAllowedLevels() {
            return new ArrayList<>(Arrays.asList("Country", "County"));
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

    public EventTaskIdProvider getEventTaskIdProvider() {
        if (eventTaskIdProvider == null) {
            eventTaskIdProvider = new EventTaskIdProviderImpl();
        }

        return eventTaskIdProvider;
    }

    public boolean isSupervisor() {
        return "Supervisor".equals(CoreLibrary.getInstance().context().allSharedPreferences().getUserPractitionerRole());
    }
}
