package org.smartregister.goldsmith.dao;

import org.smartregister.dao.AbstractDao;
import org.smartregister.domain.Practitioner;
import org.smartregister.reporting.domain.IndicatorTally;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChwPractitionerDao extends AbstractDao {

    public static String TASKS = "tasks";
    public static String COMPLETED = "completed";

    public static Practitioner getChwPractitioner(String identifier) {
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

    public static Map<String, String> getPractitionerTaskCompletion(String identifier) {
        String sql = "with tasks as \n" +
                "(select *, count(_id) as total\n" +
                "from task\n" +
                "where task.owner = '" + identifier + "')\n" +
                "select total, count(_id) as count_complete\n" +
                "from tasks where tasks.status = 'COMPLETED'";
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

    public static List<Map<String, IndicatorTally>> getPregnanciesRegisteredLast30Days(String identifier) {
        String sql = "select count(ec_anc_register.base_entity_id) from ec_anc_register\n" +
                "                     inner join ec_anc_log on ec_anc_register.base_entity_id  = ec_anc_log.base_entity_id\n" +
                "                     WHERE ec_anc_register.practitioner_identifier = '" + identifier + "' and STRFTIME('%Y-%m-%d %H:%M:%S', ec_anc_log.date_created) >= date('now', '-1 month')";

        DataMap<Map<String, IndicatorTally>> dataMap = cursor -> {
            Map<String, IndicatorTally> tallyMap = new HashMap<>();
            return tallyMap;
        };
        List<Map<String, IndicatorTally>> results = readData(sql, dataMap);
        if (results == null || results.get(0).isEmpty())
            return null;

        return results;
    }

    public static List<Map<String, IndicatorTally>> getNewBornVisitsLast30Days(String identifier) {
        String sql = "select count(DISTINCT ec.base_entity_id) from ec_child ec\n" +
                "                      inner join (\n" +
                "                        select e.baseEntityId\n" +
                "                        from event e\n" +
                "                        where e.eventType = 'Child Home Visit' AND STRFTIME('%Y-%m-%d %H:%M:%S', e.eventDate) >= date('now', '-1 month')\n" +
                "                        group by e.baseEntityId\n" +
                "                      ) e on ec.base_entity_id = e.baseEntityId\n" +
                "                      inner join ec_family_member ef on ec.base_entity_id = ef.base_entity_id and ef.date_removed is null\n" +
                "                      where ec.practitioner_identifier = '" + identifier + "' and (( ifnull(ec.entry_point,'') <> 'PNC' ) or (ifnull(ec.entry_point,'') = 'PNC' and date(ec.dob, '+28 days')  <= date()))\n" +
                "                      and date(ec.dob) between date('now', '-5 month') and date('now')";

        DataMap<Map<String, IndicatorTally>> dataMap = cursor -> {
            Map<String, IndicatorTally> tallyMap = new HashMap<>();
            return tallyMap;
        };
        List<Map<String, IndicatorTally>> results = readData(sql, dataMap);
        if (results == null || results.get(0).isEmpty())
            return null;

        return results;
    }
}
