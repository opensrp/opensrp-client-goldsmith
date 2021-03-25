package org.smartregister.goldsmith.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.smartregister.goldsmith.fragment.ChwProfileFragment;

public class ChwProfilePagerAdapter extends FragmentStatePagerAdapter {

    private Bundle extras;

    public ChwProfilePagerAdapter(@NonNull FragmentManager fm, Bundle extras) {
        super(fm);
        this.extras = extras;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ChwProfileFragment.newInstance(extras);
    }

    @Override
    public int getCount() {
        return 1;
    }

}
