package org.smartregister.goldsmith.configuration;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.configuration.ModuleFormProcessor;
import org.smartregister.domain.tag.FormTag;

import java.util.HashMap;
import java.util.List;

public class AncFormProcessor implements ModuleFormProcessor {
    @Override
    public HashMap<Client, List<Event>> extractEventClient(@NonNull String s, @Nullable Intent intent, @Nullable FormTag formTag) throws JSONException {
        return null;
    }

    @Override
    public JSONObject getFormAsJson(@NonNull JSONObject jsonObject, @NonNull String s, @NonNull String s1, @NonNull String s2, @Nullable HashMap<String, String> hashMap) throws JSONException {
        return null;
    }

    @Override
    public boolean saveFormImages(Client client, List<Event> list, String s) {
        return false;
    }
}
