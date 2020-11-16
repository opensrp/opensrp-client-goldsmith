package org.smartregister.goldsmith.interactor;

import org.smartregister.chw.core.interactor.CoreFamilyProfileInteractor;
import org.smartregister.family.util.AppExecutors;

public class FamilyProfileInteractor extends CoreFamilyProfileInteractor {

    FamilyProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public FamilyProfileInteractor() {
        super();
    }

}
