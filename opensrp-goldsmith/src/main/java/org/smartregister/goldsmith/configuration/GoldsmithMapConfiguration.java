package org.smartregister.goldsmith.configuration;

import androidx.annotation.NonNull;

import org.smartregister.tasking.util.TaskingLibraryConfiguration;
import org.smartregister.tasking.view.TaskingMapView;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 20-01-2021.
 */
public class GoldsmithMapConfiguration implements TaskingLibraryConfiguration.MapConfiguration {

    private String[] allTasksLayerIds;
    private String[] priorityTasksLayerIds;

    public GoldsmithMapConfiguration() {
        allTasksLayerIds = new String[]{"reveal-data-points"};
        priorityTasksLayerIds = new String[]{"reveal-data-points-priority"};
    }

    @Override
    public void onPriorityTasksToggle(@NonNull TaskingMapView taskingMapView, boolean on) {

    }

    @Override
    public String[] getAllTasksLayerIds() {
        return allTasksLayerIds;
    }

    @Override
    public String[] getPriorityTasksLayerIds() {
        return priorityTasksLayerIds;
    }
}
