package org.smartregister.goldsmith.util;

import org.smartregister.chw.core.utils.CoreConstants;

public class Constants extends CoreConstants {

    public interface RegisterViewConstants {

        interface ModuleOptions {
            String ANC =  "ANC";
            String PNC = "PNC";
            String ALL_FAMILIES = "All Families";
        }

        interface Provider {
            String CLIENT_COLUMN = "client_column";
            String ACTION_BUTTON_COLUMN = "action_button_column";
        }
    }
}
