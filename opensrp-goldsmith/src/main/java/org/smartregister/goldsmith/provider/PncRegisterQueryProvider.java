package org.smartregister.goldsmith.provider;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.chw.core.utils.ChildDBConstants;
import org.smartregister.chw.core.utils.ChwDBConstants;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.configuration.ModuleRegisterQueryProviderContract;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.family.util.DBConstants;
import org.smartregister.pojo.InnerJoinObject;
import org.smartregister.pojo.QueryTable;

import java.util.HashSet;
import java.util.Set;

public class PncRegisterQueryProvider extends ModuleRegisterQueryProviderContract {
    @NonNull
    @Override
    public String getObjectIdsQuery(@Nullable String filters, @Nullable String mainCondition) {
        if (TextUtils.isEmpty(filters)) {
            return "SELECT object_id FROM (SELECT object_id, last_interacted_with FROM ec_pregnancy_outcome WHERE  date_removed is NULL AND (entity_type is NULL OR entity_type = 'ec_pregnancy_outcome')) ORDER BY last_interacted_with DESC";
        } else {
            return "SELECT object_id FROM (SELECT object_id, last_interacted_with FROM ec_pregnancy_outcome WHERE  date_removed is NULL AND (entity_type is NULL OR entity_type = 'ec_pregnancy_outcome') AND PHRASE MATCH '"
                    + filters
                    + "*') ORDER BY last_interacted_with DESC";
        }
    }

    @NonNull
    @Override
    public String[] countExecuteQueries(@Nullable String filters, @Nullable String mainCondition) {
        /*QueryGenerator generator = new QueryGenerator()
                .withMainTable(CoreConstants.TABLE_NAME.PNC_MEMBER)
                .withColumn("count("+CoreConstants.TABLE_NAME.PNC_MEMBER + "." + org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID+")")
                .withJoinClause("INNER JOIN " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + " ON "
                        + CoreConstants.TABLE_NAME.PNC_MEMBER + "." + org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID + " = "
                        + CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + org.smartregister.chw.anc.util.DBConstants.KEY.BASE_ENTITY_ID)

                .withWhereClause(getMainCondition());
        return new String[]{generator.generateQuery()};*/
        return new String[]{
                "SELECT count(ec_pregnancy_outcome.base_entity_id) FROM ec_pregnancy_outcome INNER JOIN ec_family_member ON ec_pregnancy_outcome.base_entity_id = " +
                        "ec_family_member.base_entity_id WHERE (  ec_family_member.date_removed is null AND ec_pregnancy_outcome.is_closed is 0  )"
        };
    }

    @NonNull
    @Override
    public String mainSelectWhereIDsIn() {
        String tableName = CoreConstants.TABLE_NAME.PNC_MEMBER;
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        queryBuilder.customJoin("INNER JOIN " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + " ON  " + tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.BASE_ENTITY_ID + " AND " + tableName + "." + ChwDBConstants.IS_CLOSED + " IS " + 0 + " AND " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + ChwDBConstants.IS_CLOSED + " IS " + 0 + " AND " + tableName + "." + ChwDBConstants.DELIVERY_DATE + " IS NOT NULL COLLATE NOCASE ");
        queryBuilder.customJoin("INNER JOIN " + CoreConstants.TABLE_NAME.FAMILY + " ON  " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.RELATIONAL_ID + " = " + CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.BASE_ENTITY_ID + " COLLATE NOCASE ");
        queryBuilder.customJoin("INNER JOIN " + CoreConstants.TABLE_NAME.ANC_MEMBER + " ON  " + tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + CoreConstants.TABLE_NAME.ANC_MEMBER + "." + DBConstants.KEY.BASE_ENTITY_ID + " AND " + tableName + "." + ChwDBConstants.IS_CLOSED + " IS " + 0 + " AND " + tableName + "." + ChwDBConstants.DELIVERY_DATE + " IS NOT NULL COLLATE NOCASE ");

        return queryBuilder.mainCondition(getMainCondition());
    }

    protected String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();

        columnList.add(tableName + "." + ChwDBConstants.DELIVERY_DATE);
        columnList.add(tableName + "." + DBConstants.KEY.BASE_ENTITY_ID);
        columnList.add(CoreConstants.TABLE_NAME.ANC_MEMBER + "." + org.smartregister.chw.anc.util.DBConstants.KEY.PHONE_NUMBER);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.RELATIONAL_ID + " as " + ChildDBConstants.KEY.RELATIONAL_ID);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.FIRST_NAME);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.MIDDLE_NAME);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.LAST_NAME);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.DOB);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.UNIQUE_ID);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.FAMILY_HEAD);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.PRIMARY_CAREGIVER);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.VILLAGE_TOWN);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.FIRST_NAME + " as " + org.smartregister.chw.anc.util.DBConstants.KEY.FAMILY_NAME);

        return columnList.toArray(new String[columnList.size()]);
    }

    public String getMainCondition() {
        return "";
    }


    @Override
    public String mainSelectWhereIdsIn(@NonNull InnerJoinObject[] innerJoinObjects, @NonNull QueryTable[] queryTables) {
        return null;
    }

    @Override
    public String mainSelect(@NonNull InnerJoinObject[] innerJoinObjects, @NonNull QueryTable[] queryTables) {
        return null;
    }
}
