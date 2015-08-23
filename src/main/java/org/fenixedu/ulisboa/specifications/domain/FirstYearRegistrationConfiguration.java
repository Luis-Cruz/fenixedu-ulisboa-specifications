package org.fenixedu.ulisboa.specifications.domain;

import org.fenixedu.academic.domain.Degree;

import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class FirstYearRegistrationConfiguration extends FirstYearRegistrationConfiguration_Base {

    public FirstYearRegistrationConfiguration(Degree degree) {
        super();
        setGlobalConfiguration(FirstYearRegistrationGlobalConfiguration.getInstance());
        setDegree(degree);
    }

    public void delete() {
        setDegree(null);
        setGlobalConfiguration(null);
        super.deleteDomainObject();
    }

    @ConsistencyPredicate
    private boolean isOnlyShiftsOrClassesEnrolment() {
        return !(getRequiresClassesEnrolment() && getRequiresShiftsEnrolment());
    }
}
