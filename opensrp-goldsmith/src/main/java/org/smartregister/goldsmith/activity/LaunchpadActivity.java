package org.smartregister.goldsmith.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.AllConstants;
import org.smartregister.CoreLibrary;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.view.LaunchpadView;
import org.smartregister.view.activity.BaseConfigurableRegisterActivity;

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


        LaunchpadView myChwsClients  = findViewById(R.id.launchpadAct_myChws);
        myChwsClients.setOnClickListener(v -> CoreLibrary.getInstance()
                .startRegisterActivity(LaunchpadActivity.this));

    }

    private void startIntent(@NonNull Intent intent, @NonNull Intent startingIntent) {
        intent.putExtras(startingIntent);
        startActivity(intent);
    }
}