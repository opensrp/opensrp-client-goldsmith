package org.smartregister.goldsmith.util;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConfigurableRegisterUtils {

    public static void fillValue(@Nullable TextView v, @NonNull String value) {
        if (v != null) {
            v.setText(value);
        }
    }
}
