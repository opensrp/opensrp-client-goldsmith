
package org.smartregister.goldsmith.repository;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.AllConstants;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.configurableviews.repository.ConfigurableViewsRepository;
import org.smartregister.goldsmith.BuildConfig;
import org.smartregister.goldsmith.ChwApplication;
import org.smartregister.immunization.repository.VaccineNameRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
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


        onUpgrade(database, 1, BuildConfig.DATABASE_VERSION);

        // Others supposed to be in onUpgrade calls
        UniqueIdRepository.createTable(database);
        SettingsRepository.onUpgrade(database);
        PlanDefinitionRepository.createTable(database);
        PlanDefinitionSearchRepository.createTable(database);

        ClientFormRepository.createTable(database);
        ManifestRepository.createTable(database);


        // Add vaccine tables
        VaccineRepository.createTable(database);
        VaccineNameRepository.createTable(database);
        VaccineTypeRepository.createTable(database);


        VisitRepository.createTable(database);
        VisitDetailsRepository.createTable(database);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.w("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");


        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {

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
}
