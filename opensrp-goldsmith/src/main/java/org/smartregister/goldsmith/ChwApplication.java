package org.smartregister.goldsmith;

import org.smartregister.configurableviews.helper.JsonSpecHelper;
import org.smartregister.view.activity.DrishtiApplication;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 21-09-2020.
 */
public class ChwApplication extends DrishtiApplication {

    public JsonSpecHelper jsonSpecHelper;

    @Override
    public void logoutCurrentUser() {

    }



    public static JsonSpecHelper getJsonSpecHelper() {
        return ((ChwApplication) getInstance()).jsonSpecHelper;
    }
}
