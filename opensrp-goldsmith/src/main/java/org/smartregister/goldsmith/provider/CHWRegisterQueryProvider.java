package org.smartregister.goldsmith.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.configuration.ModuleRegisterQueryProviderContract;
import org.smartregister.pojo.InnerJoinObject;
import org.smartregister.pojo.QueryTable;

public class CHWRegisterQueryProvider extends ModuleRegisterQueryProviderContract {
    @NonNull
    @Override
    public String getObjectIdsQuery(@Nullable String s, @Nullable String s1) {
        return null;
    }

    @NonNull
    @Override
    public String[] countExecuteQueries(@Nullable String s, @Nullable String s1) {
        return new String[0];
    }

    @NonNull
    @Override
    public String mainSelectWhereIDsIn() {
        return null;
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
