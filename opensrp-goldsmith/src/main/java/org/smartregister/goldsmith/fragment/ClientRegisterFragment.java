package org.smartregister.goldsmith.fragment;

import android.view.View;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.fragment.BaseFamilyRegisterFragment;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.provider.FamilyRegisterProvider;
import org.smartregister.view.contract.IView;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;


public class ClientRegisterFragment extends BaseRegisterFragment {

/*
    @Override
    public void initializeAdapter(Set<IView> visibleColumns) {

    }

    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        FamilyRegisterProvider familyRegisterProvider = new FamilyRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }*/

    @Override
    protected int getLayout() {
        return R.layout.fragment_base_register; // TODO -> Implement new layout
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public void setUniqueID(String s) {

    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {

    }

    @Override
    protected String getMainCondition() {
        return null;
    }

    @Override
    protected String getDefaultSortQuery() {
        return null;
    }

    @Override
    protected void startRegistration() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    public void goToMapView() {
        // TODO -> Declare this in RegisterFragment.View contract
    }

    @Override
    public void showNotFoundPopup(String s) {

    }
}