package org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.student.enrollment.EnrolmentContextHandler;

public class UlisboaEnrolmentContextHandler extends EnrolmentContextHandler {

    @Override
    public Optional<String> getReturnURLForStudentInCurricularCourses(HttpServletRequest request, Registration registration) {
        boolean inCandidateEnrolmentProcess =
                registration.getStudentCurricularPlanForCurrentExecutionYear().isInCandidateEnrolmentProcess(
                        ExecutionYear.readCurrentExecutionYear());
        if (inCandidateEnrolmentProcess) {

            return Optional.of(request.getContextPath()
                    + "/fenixedu-ulisboa-specifications/firsttimecandidacy/showselectedcourses");
        } else {
            return new DefaultEnrolmentContextHandler().getReturnURLForStudentInCurricularCourses(request, registration);
        }
    }

    @Override
    public Optional<String> getReturnURLForStudentInClasses(HttpServletRequest request, Registration registration) {
        boolean inCandidateEnrolmentProcess =
                registration.getStudentCurricularPlanForCurrentExecutionYear().isInCandidateEnrolmentProcess(
                        ExecutionYear.readCurrentExecutionYear());
        if (inCandidateEnrolmentProcess) {
            return Optional.of(request.getContextPath()
                    + "/fenixedu-ulisboa-specifications/firsttimecandidacy/showscheduledclasses");
        } else {
            return new DefaultEnrolmentContextHandler().getReturnURLForStudentInClasses(request, registration);
        }
    }

    public Optional<String> getReturnURLForStudentInShifts(HttpServletRequest request, Registration registration) {
        if (registration == null) {
            return Optional.empty();
        }
        boolean inCandidateEnrolmentProcess =
                registration.getStudentCurricularPlanForCurrentExecutionYear().isInCandidateEnrolmentProcess(
                        ExecutionYear.readCurrentExecutionYear());
        if (inCandidateEnrolmentProcess) {
            return Optional.of(request.getContextPath()
                    + "/fenixedu-ulisboa-specifications/firsttimecandidacy/showscheduledclasses");
        }
        return Optional.empty();
    }
}
