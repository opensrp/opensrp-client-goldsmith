package org.smartregister.goldsmith.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.CoreLibrary;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.job.LocationTaskServiceJob;
import org.smartregister.goldsmith.view.LaunchpadView;
import org.smartregister.job.DocumentConfigurationServiceJob;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.job.SyncServiceJob;

public class LaunchpadActivity extends AppCompatActivity {

    private LaunchpadView tasksButton;
    private Intent startingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpad);

        startingIntent = getIntent();

        tasksButton = findViewById(R.id.launchpadAct_myTasks);
        tasksButton.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchpadActivity.this, GoldsmithTaskRegisterActivity.class);
            startIntent(intent, startingIntent);
        });

        LaunchpadView myChwsClients = findViewById(R.id.launchpadAct_myClients);
        myChwsClients.setOnClickListener(v -> CoreLibrary.getInstance()
                .startRegisterActivity(LaunchpadActivity.this));

        // Enable immediate sync
        LaunchpadView sync = findViewById(R.id.launchpadAct_sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationTaskServiceJob.scheduleJobImmediately(LocationTaskServiceJob.TAG);
                PullUniqueIdsServiceJob.scheduleJobImmediately(PullUniqueIdsServiceJob.TAG);
                DocumentConfigurationServiceJob.scheduleJobImmediately(DocumentConfigurationServiceJob.TAG);
            }
        });
    }

    private void startIntent(@NonNull Intent intent, @NonNull Intent startingIntent) {
        intent.putExtras(startingIntent);
        startActivity(intent);
    }
}