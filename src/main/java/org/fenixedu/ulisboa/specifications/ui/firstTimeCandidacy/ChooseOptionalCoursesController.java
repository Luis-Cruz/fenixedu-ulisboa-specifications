/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: joao.roxo@qub-it.com 
 *               nuno.pinheiro@qub-it.com
 *
 * 
 * This file is part of FenixEdu Specifications.
 *
 * FenixEdu Specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleValidationType;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.joda.time.DateTime;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = FirstTimeCandidacyController.class)
@RequestMapping("/fenixedu-ulisboa-specifications/firsttimecandidacy/chooseoptionalcourses")
public class ChooseOptionalCoursesController extends FenixeduUlisboaSpecificationsBaseController {

    @RequestMapping
    public String chooseoptionalcourses(Model model, RedirectAttributes redirectAttributes) {
        Registration registration = FirstTimeCandidacyController.getStudentCandidacy().getRegistration();
        activateRegistration(registration);

        Degree degree = registration.getDegree();
        if (degree.getFirstYearRegistrationConfiguration() == null
                || !degree.getFirstYearRegistrationConfiguration().getRequiresCoursesEnrolment()) {
            //School does not require first year course enrolment
            createAutomaticEnrolments(registration, ExecutionYear.readCurrentExecutionYear().getFirstExecutionPeriod());
            return chooseoptionalcoursesToContinue(model, redirectAttributes);
        }
        return "fenixedu-ulisboa-specifications/firsttimecandidacy/chooseoptionalcourses";
    }

    @RequestMapping(value = "/opencourseenrollments")
    public String chooseoptionalcoursesToOpenCourseEnrollments(Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        Registration registration = FirstTimeCandidacyController.getStudentCandidacy().getRegistration();
        String link = "/student/bolonhaStudentEnrollment.do?method=prepare&executionSemesterID=%s&registrationOid=%s";
        String format = String.format(link, executionSemester.getExternalId(), registration.getExternalId());

        //request
        String injectChecksumInUrl =
                GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), format, request.getSession());
        return redirect(injectChecksumInUrl, model, redirectAttributes);

    }

    @RequestMapping(value = "/continue")
    public String chooseoptionalcoursesToContinue(Model model, RedirectAttributes redirectAttributes) {
        return redirect("/fenixedu-ulisboa-specifications/firsttimecandidacy/showselectedcourses", model, redirectAttributes);
    }

    @Atomic
    private void activateRegistration(Registration registration) {
        RegistrationState state = registration.getActiveState();
        if (state.getStateType().equals(RegistrationStateType.INACTIVE)) {
            RegistrationState.createRegistrationState(registration, AccessControl.getPerson(), new DateTime(),
                    RegistrationStateType.REGISTERED);
        }
    }

    public void createAutomaticEnrolments(Registration registration, ExecutionSemester executionSemester) {
        StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionSemester);
        if (studentCurricularPlan.getEnrolmentsSet().isEmpty()) {
            createFirstTimeStudentEnrolmentsFor(studentCurricularPlan, studentCurricularPlan.getRoot(), studentCurricularPlan
                    .getDegreeCurricularPlan().getCurricularPeriodFor(1, 1), executionSemester, AccessControl.getPerson()
                    .getUsername());
            registration.updateEnrolmentDate(executionSemester.getExecutionYear());
            if (studentCurricularPlan.getDegreeCurricularPlan().getCurricularRuleValidationType() == CurricularRuleValidationType.YEAR) {
                createFirstTimeStudentEnrolmentsFor(studentCurricularPlan, studentCurricularPlan.getRoot(), studentCurricularPlan
                        .getDegreeCurricularPlan().getCurricularPeriodFor(1, 2), executionSemester.getNextExecutionPeriod(),
                        AccessControl.getPerson().getUsername());
            }
        }
    }

    void createFirstTimeStudentEnrolmentsFor(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularPeriod curricularPeriod, ExecutionSemester executionSemester, String createdBy) {

        if (curriculumGroup.getDegreeModule() != null) {
            for (final Context context : curriculumGroup.getDegreeModule().getContextsWithCurricularCourseByCurricularPeriod(
                    curricularPeriod, executionSemester)) {
                new Enrolment(studentCurricularPlan, curriculumGroup, (CurricularCourse) context.getChildDegreeModule(),
                        executionSemester, EnrollmentCondition.FINAL, createdBy);
            }
        }

        if (!curriculumGroup.getCurriculumModulesSet().isEmpty()) {
            for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
                if (!curriculumModule.isLeaf()) {
                    createFirstTimeStudentEnrolmentsFor(studentCurricularPlan, (CurriculumGroup) curriculumModule,
                            curricularPeriod, executionSemester, createdBy);
                }
            }
        }
    }
}
