package org.smartregister.goldsmith.provider;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.configuration.ModuleRegisterQueryProviderContract;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.goldsmith.util.Constants;
import org.smartregister.pojo.InnerJoinObject;
import org.smartregister.pojo.QueryTable;

import java.util.HashSet;
import java.util.Set;

public class CHWRegisterQueryProvider extends ModuleRegisterQueryProviderContract {

    @NonNull
    @Override
    public String getObjectIdsQuery(@Nullable String filters, @Nullable String mainCondition) {
        if (TextUtils.isEmpty(filters)) {
            return "SELECT object_id FROM (SELECT object_id, last_interacted_with FROM practitioner_search WHERE  is_closed is 0) ORDER BY last_interacted_with DESC";
        } else {
            return "SELECT object_id FROM (SELECT object_id, last_interacted_with FROM practitioner_search WHERE  is_closed = 0) AND PHRASE MATCH '"
                    + filters
                    + "*') ORDER BY last_interacted_with DESC";
        }
    }

    @NonNull
    @Override
    public String[] countExecuteQueries(@Nullable String filters, @Nullable String mainCondition) {
        return new String[]{"SELECT count(identifier) FROM practitioner"};
    }

    @NonNull
    @Override
    public String mainSelectWhereIDsIn() {
        String tableName = "practitioner";

        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        return queryBuilder.mainCondition("");
    }

    private String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();

        columnList.add(tableName + "." + Constants.DBConstants.IDENTIFIER);
        columnList.add(tableName + "." + Constants.DBConstants.IS_ACTIVE);
        columnList.add(tableName + "." + Constants.DBConstants.NAME);
        columnList.add(tableName + "." + Constants.DBConstants.USER_ID);
        columnList.add(tableName + "." + Constants.DBConstants.USERNAME);

        return columnList.toArray(new String[0]);
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
