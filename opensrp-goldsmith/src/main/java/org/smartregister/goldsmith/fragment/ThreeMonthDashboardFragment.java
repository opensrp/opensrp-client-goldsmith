package org.smartregister.goldsmith.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.smartregister.goldsmith.R;

public class ThreeMonthDashboardFragment extends Fragment {

    public static ThreeMonthDashboardFragment newInstance() {
        ThreeMonthDashboardFragment fragment = new ThreeMonthDashboardFragment();
        Bundle args = new Bundle();
        // TODO -> Add to bundle or delete args
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Implementation to be provided later
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_three_month_dashboard, container, false);
    }
}