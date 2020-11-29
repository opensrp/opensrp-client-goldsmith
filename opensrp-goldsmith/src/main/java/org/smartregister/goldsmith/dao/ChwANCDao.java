package org.smartregister.goldsmith.dao;

import androidx.annotation.Nullable;

import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.dao.AbstractDao;

import java.util.Date;
import java.util.List;

public class ChwANCDao extends AbstractDao {

    public static @Nullable
    Date getLatestVisitDate(String baseEntityId) {
        Date lastVisitDate = null;
        Visit lastVisit = getLatestVisit(baseEntityId);
        if (lastVisit != null) {
            lastVisitDate = lastVisit.getDate();
        }
        return lastVisitDate;
    }

    public static Visit getLatestVisit(String baseEntityId) {
        String sql = "SELECT * FROM visits WHERE visits.base_entity_id = '" + baseEntityId + "' LIMIT 1";
        DataMap<Visit> dataMap = cus -> {
            Visit visit = new Visit();
            visit.setVisitId(getCursorValue(cus, "visit_id"));
            visit.setVisitType(getCursorValue(cus, "visit_type"));
            //visit.setVisitGroup(getCursorValue(cus, ""));
            visit.setParentVisitID(getCursorValue(cus, "parent_visit_id"));
            visit.setPreProcessedJson(getCursorValue(cus, "pre_processed"));
            visit.setBaseEntityId(getCursorValue(cus, "base_entity_id"));
            visit.setDate(new Date(Long.parseLong(getCursorValue(cus, "visit_date"))));
            visit.setJson(getCursorValue(cus, "visit_json"));
            visit.setFormSubmissionId(getCursorValue(cus, "form_submission_id"));
            visit.setProcessed(Integer.parseInt(getCursorValue(cus, "processed")) == 1);
            visit.setCreatedAt(new Date(Long.parseLong(getCursorValue(cus, "created_at"))));
            visit.setUpdatedAt(new Date(Long.parseLong(getCursorValue(cus, "updated_at"))));
            return visit;
        };

        List<Visit> results = AbstractDao.readData(sql, dataMap);
        return (results != null && results.size() > 0) ? results.get(0) : null;
    }

}
