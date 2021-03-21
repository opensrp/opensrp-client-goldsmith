package org.smartregister.goldsmith.dao;

import org.smartregister.dao.AbstractDao;
import org.smartregister.domain.Practitioner;

import java.util.List;

public class ChwDao extends AbstractDao {


    public static Practitioner getChw(String identifier) {
        String sql = "SELECT * FROM practitioner WHERE identifier = '" + identifier + "'";

        DataMap<Practitioner> dataMap = cursor -> {
            Practitioner chw = new Practitioner();
            chw.setIdentifier(identifier);
            chw.setActive(Integer.parseInt(getCursorValue(cursor, "is_active")) > 0);
            chw.setName(getCursorValue(cursor, "name"));
            chw.setUserId(getCursorValue(cursor, "user_id"));
            chw.setUsername(getCursorValue(cursor, "username"));
            return chw;
        };

        List<Practitioner> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }
}
