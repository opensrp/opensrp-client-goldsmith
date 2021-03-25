package org.smartregister.goldsmith.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.smartregister.goldsmith.fragment.SupervisorPerformanceFragment;
import org.smartregister.goldsmith.fragment.TeamPerformanceFragment;

public class SupervisorSectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SupervisorSectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SupervisorPerformanceFragment.newInstance();
            case 1:
                return TeamPerformanceFragment.newInstance();
            default:
                return SupervisorPerformanceFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
