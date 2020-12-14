
package org.smartregister.goldsmith.repository;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.AllConstants;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.chw.core.repository.MonthlyTalliesRepository;
import org.smartregister.chw.core.repository.ScheduleRepository;
import org.smartregister.configurableviews.repository.ConfigurableViewsRepository;
import org.smartregister.domain.db.Column;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.goldsmith.util.RepositoryUtils;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineNameRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.AlertRepository;
import org.smartregister.repository.CampaignRepository;
import org.smartregister.repository.ClientFormRepository;
import org.smartregister.repository.ClientRelationshipRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.ManifestRepository;
import org.smartregister.repository.PlanDefinitionRepository;
import org.smartregister.repository.PlanDefinitionSearchRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.repository.StructureRepository;
import org.smartregister.repository.TaskRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.DatabaseMigrationUtils;
import org.smartregister.view.activity.DrishtiApplication;

import timber.log.Timber;

import static org.smartregister.repository.EventClientRepository.Table.event;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 05-10-2020.
 */
public class GoldsmithRepository extends Repository {


    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;


    public GoldsmithRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context,
                AllConstants.DATABASE_NAME,
                BuildConfig.DATABASE_VERSION,
                openSRPContext.session(),
                ChwApplication.createCommonFtsObject(),
                openSRPContext.sharedRepositoriesArray());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
        ConfigurableViewsRepository.createTable(database);
        EventClientRepository.createTable(database, EventClientRepository.Table.client, EventClientRepository.client_column.values());
        EventClientRepository.createTable(database, event, EventClientRepository.event_column.values());
        ClientRelationshipRepository.createTable(database);

        CampaignRepository.createTable(database);
        TaskRepository.createTable(database);
        LocationRepository.createTable(database);
        StructureRepository.createTable(database);

        // Others supposed to be in onUpgrade calls
        UniqueIdRepository.createTable(database);
        SettingsRepository.onUpgrade(database);
        PlanDefinitionRepository.createTable(database);
        PlanDefinitionSearchRepository.createTable(database);
        ScheduleRepository.createTable(database);

        ClientFormRepository.createTable(database);
        ManifestRepository.createTable(database);


        // Add vaccine tables
        VaccineRepository.createTable(database);
        VaccineNameRepository.createTable(database);
        VaccineTypeRepository.createTable(database);


        VisitRepository.createTable(database);
        VisitDetailsRepository.createTable(database);
        DatabaseMigrationUtils.addColumnIfNotExists(database, VisitRepository.VISIT_TABLE, "visit_group", "varchar");


        // Add recurring service repositories
        RecurringServiceTypeRepository.createTable(database);
        RecurringServiceRecordRepository.createTable(database);

        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
        IMDatabaseUtils.populateRecurringServices(DrishtiApplication.getInstance().getApplicationContext(), database, recurringServiceTypeRepository);

        // Add reporting tables
        IndicatorRepository.createTable(database);
        IndicatorQueryRepository.createTable(database);
        DailyIndicatorCountRepository.createTable(database);
        MonthlyTalliesRepository.createTable(database);

        onUpgrade(database, 1, BuildConfig.DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.w("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");


        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    upgradeToVersion2(DrishtiApplication.getInstance(), db);
                    break;
                case 3:
                    upgradeToVersion3(db);
                    break;
                case 4:
                    upgradeToVersion4(db);
                    break;
                case 5:
                    upgradeToVersion5(db);
                    break;
                default:
                    break;
            }
            upgradeTo++;
        }
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase(String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalStateException("Password is blank");
        }
        try {
            if (readableDatabase == null || !readableDatabase.isOpen()) {
                readableDatabase = super.getReadableDatabase(password);
            }
            return readableDatabase;
        } catch (Exception e) {
            Timber.e(e, "Database Error. ");
            return null;
        }

    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase(String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalStateException("Password is blank");
        } else if (writableDatabase == null || !writableDatabase.isOpen()) {
            writableDatabase = super.getWritableDatabase(password);
        }
        return writableDatabase;
    }

    @Override
    public synchronized void close() {
        if (readableDatabase != null) {
            readableDatabase.close();
        }

        if (writableDatabase != null) {
            writableDatabase.close();
        }
        super.close();
    }



    private static void upgradeToVersion2(Context context, SQLiteDatabase db) {
        try {
            // add missing vaccine columns
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_EVENT_ID_COL);
            db.execSQL(VaccineRepository.EVENT_ID_INDEX);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_FORMSUBMISSION_ID_COL);
            db.execSQL(VaccineRepository.FORMSUBMISSION_INDEX);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_OUT_OF_AREA_COL);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_OUT_OF_AREA_COL_INDEX);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_HIA2_STATUS_COL);
            IMDatabaseUtils.accessAssetsAndFillDataBaseForVaccineTypes(context, db);

            // add missing event repository table
            Column[] columns = {EventClientRepository.event_column.formSubmissionId};
            EventClientRepository.createIndex(db, EventClientRepository.Table.event, columns);

            db.execSQL(VaccineRepository.ALTER_ADD_CREATED_AT_COLUMN);
            VaccineRepository.migrateCreatedAt(db);

            db.execSQL(RecurringServiceRecordRepository.ALTER_ADD_CREATED_AT_COLUMN);
            RecurringServiceRecordRepository.migrateCreatedAt(db);

            // add missing alert table info
            db.execSQL(AlertRepository.ALTER_ADD_OFFLINE_COLUMN);
            db.execSQL(AlertRepository.OFFLINE_INDEX);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_TEAM_COL);
            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_TEAM_ID_COL);
            db.execSQL(RecurringServiceRecordRepository.UPDATE_TABLE_ADD_TEAM_COL);
            db.execSQL(RecurringServiceRecordRepository.UPDATE_TABLE_ADD_TEAM_ID_COL);

            db.execSQL(VaccineRepository.UPDATE_TABLE_ADD_CHILD_LOCATION_ID_COL);
            db.execSQL(RecurringServiceRecordRepository.UPDATE_TABLE_ADD_CHILD_LOCATION_ID_COL);

            // Setup reporting
            ReportingLibrary reportingLibraryInstance = ReportingLibrary.getInstance();
            String indicatorsConfigFile = "config/indicator-definitions.yml";
            reportingLibraryInstance.readConfigFile(indicatorsConfigFile, db);
        } catch (Exception e) {
            Timber.e(e, "upgradeToVersion2 ");
        }
    }

    private static void upgradeToVersion3(SQLiteDatabase db) {
        try {
            // delete possible duplication
            db.execSQL(RepositoryUtils.DELETE_DUPLICATE_SCHEDULES);
            db.execSQL(ScheduleRepository.USER_UNIQUE_INDEX);
        } catch (Exception e) {
            Timber.e(e, "upgradeToVersion2 ");
        }
    }

    private static void upgradeToVersion4(SQLiteDatabase db) {
        try {
            // delete possible duplication
            db.execSQL(RepositoryUtils.ADD_MISSING_REPORTING_COLUMN);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private static void upgradeToVersion5(SQLiteDatabase db) {
        try {
            RepositoryUtils.addDetailsColumnToFamilySearchTable(db);
        } catch (Exception e) {
            Timber.e(e, "upgradeToVersion5");
        }
    }
}
