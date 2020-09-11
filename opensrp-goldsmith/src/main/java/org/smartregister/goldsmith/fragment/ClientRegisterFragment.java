package org.smartregister.goldsmith.fragment;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.fragment.BaseFamilyRegisterFragment;
import org.smartregister.goldsmith.R;
import org.smartregister.goldsmith.provider.FamilyRegisterProvider;

import java.util.HashMap;
import java.util.Set;


public class ClientRegisterFragment extends BaseFamilyRegisterFragment {

    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        FamilyRegisterProvider familyRegisterProvider = new FamilyRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_base_register; // TODO -> Implement new layout
    }

    @Override
    protected void initializePresenter() {

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

    public void goToMapView() {
        // TODO -> Declare this in RegisterFragment.View contract
    }

}