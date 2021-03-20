package org.smartregister.goldsmith.dao;

import org.smartregister.dao.AbstractDao;
import org.smartregister.goldsmith.domain.ChwPerson;

import java.util.List;

public class ChwDao extends AbstractDao {


    public static ChwPerson getChw(String identifier) {
        String sql = "SELECT * FROM chw_practitioner WHERE identifier = '" + identifier + "'";

        DataMap<ChwPerson> dataMap = cursor -> {
            ChwPerson chw = new ChwPerson();
            chw.setIdentifier(identifier);
            chw.setActive(Integer.parseInt(getCursorValue(cursor, "active")) > 0);
            chw.setFullName(getCursorValue(cursor, "name"));
            chw.setUserId(getCursorValue(cursor, "user_id"));
            chw.setUserName(getCursorValue(cursor, "username"));
            return chw;
        };

        List<ChwPerson> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }
}
