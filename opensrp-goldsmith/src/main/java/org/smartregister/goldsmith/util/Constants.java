package org.smartregister.goldsmith.util;

import org.smartregister.chw.core.utils.CoreConstants;

public class Constants extends CoreConstants {

    public interface RegisterViewConstants {

        interface ModuleOptions {
            String ANC = "ANC";
            String PNC = "PNC";
            String ALL_FAMILIES = "All Families";
            String CHW = "My CHWs";
        }

        interface Provider {
            String CLIENT_COLUMN = "client_column";
            String CHW_COLUMN = "chw_column";
            String ACTION_BUTTON_COLUMN = "action_button_column";
            String PERFORMANCE_COLUMN = "performance_column";
        }
    }

    public interface Client {
        String PHONE_NUMBER = "phone_number";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
    }

    public static class IntentKeysConstants {
        public static String GENDER = "gender";
        public static String DOB = "dob";
    }

    public static class TableName extends CoreConstants.TABLE_NAME {
        public static final String CHW_PRACTITIONER = "practitioner";
    }

    public static class SyncConstants {
        public static String UPDATE_REPORTING_INDICATORS = "update_reporting_indicators";
    }

    public static final class DateFormatsConstants {
        public static final String HOME_VISIT_DISPLAY = "dd MMM yyyy";
    }

    public interface FormSubmissionField {
        String pncHfNextVisitDateFieldType = "pnc_hf_next_visit_date";
    }

    public interface GoldsmithEventTypes {
        String REGISTER_FAMILY_STRUCTURE_EVENT = "Register_Family_Structure_Event";
    }

    public interface ANC_MEMBER_OBJECTS extends org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS {
        String FHIR_TASK_ID = "fhirTaskId";
    }

    public interface EventDetails {
        String TASK_ID = "taskIdentifier";
    }

    public interface DBConstants extends DB_CONSTANTS {
        String IS_ACTIVE = "is_active";
        String NAME = "name";
        String USER_ID = "user_id";
        String USERNAME = "username";
        String IDENTIFIER = "identifier";
        String RELATIONAL_ID = "relationalid";
    }
}
