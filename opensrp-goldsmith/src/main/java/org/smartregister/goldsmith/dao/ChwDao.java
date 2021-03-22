package org.smartregister.goldsmith.dao;

import org.smartregister.dao.AbstractDao;
import org.smartregister.domain.Practitioner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChwDao extends AbstractDao {

    public static String TASKS = "tasks";
    public static String COMPLETED = "completed";


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


    public static Map<String, String> getChwTaskCompletion(String identifier) {
        String sql = "with tasks as \n" +
                "(select *, count(_id) as total\n" +
                "from task\n" +
                "where task.for = '" + identifier + "')\n" +
                "select total, count(_id) as count_complete\n" +
                "from tasks where tasks.status = 'READY'";
        DataMap<Map<String, String>> dataMap = cursor -> {
            Map<String, String> taskCompletion = new HashMap<>();
            String totalTasks = getCursorValue(cursor, "total");
            taskCompletion.put(TASKS, totalTasks != null ? totalTasks : "0");
            taskCompletion.put(COMPLETED, getCursorValue(cursor, "count_complete"));
            return taskCompletion;
        };
        List<Map<String, String>> results = readData(sql, dataMap);
        if (results == null || results.get(0).isEmpty())
            return null;

        return results.get(0);
    }
}
