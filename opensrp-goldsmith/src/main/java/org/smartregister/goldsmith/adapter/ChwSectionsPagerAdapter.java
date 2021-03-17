package org.smartregister.goldsmith.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.smartregister.goldsmith.fragment.ThirtyDayPerformanceFragment;
import org.smartregister.goldsmith.fragment.ThreeMonthDashboardFragment;

public class ChwSectionsPagerAdapter extends FragmentStatePagerAdapter {

    public ChwSectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public @NotNull Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ThirtyDayPerformanceFragment.newInstance();
            case 1:
                return ThreeMonthDashboardFragment.newInstance();
            default:
                return ThirtyDayPerformanceFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        return super.getItemPosition(object);
    }

}
