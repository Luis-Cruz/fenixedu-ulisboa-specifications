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

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.DegreeCandidacy;
import org.fenixedu.academic.domain.candidacy.IMDCandidacy;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.ulisboa.specifications.domain.student.access.StudentAccessServices;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsController;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = FenixeduUlisboaSpecificationsController.class, title = "label.title.firstTimeCandidacy")
@RequestMapping(FirstTimeCandidacyController.CONTROLLER_URL)
public class FirstTimeCandidacyController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL = "/fenixedu-ulisboa-specifications/firsttimecandidacy/home";

    private static final String _INSTRUCTIONS_URI = "/instructions";
    public static final String INSTRUCTIONS_URL = CONTROLLER_URL + _INSTRUCTIONS_URI;

    @RequestMapping
    public String home(Model model) {
        Stream<Candidacy> firstTimeCandidacies =
                AccessControl.getPerson().getCandidaciesSet().stream().filter(firstTimeCandidaciesPredicate);
        long count = firstTimeCandidacies.count();
        if (count == 0) {
            throw new RuntimeException(
                    "Students with no DegreeCandidacies or IMDCandidacies are not supported in the first time registration flow");
        }
        if (count > 1) {
            throw new RuntimeException(
                    "Students with multiple DegreeCandidacies or IMDCandidacies are not supported in the first time registration flow");
        }

        return "fenixedu-ulisboa-specifications/firsttimecandidacy/instructions";
    }

    @RequestMapping(value = "/continue")
    public String instructionsToContinue(Model model, RedirectAttributes redirectAttributes) {
        Person person = AccessControl.getPerson();
        StudentAccessServices.requestSyncPersonFromExternal(person);

        return redirect("/fenixedu-ulisboa-specifications/firsttimecandidacy/personalinformationform/fillpersonalinformation",
                model, redirectAttributes);
    }

    private static Predicate<Candidacy> firstTimeCandidaciesPredicate =
            c -> ((c instanceof DegreeCandidacy) || (c instanceof IMDCandidacy))
                    && ((StudentCandidacy) c).getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear());

    public static StudentCandidacy getStudentCandidacy() {
        Stream<Candidacy> firstTimeCandidacies =
                AccessControl.getPerson().getCandidaciesSet().stream().filter(firstTimeCandidaciesPredicate);
        return (StudentCandidacy) firstTimeCandidacies.findAny().get();
    }

    @Atomic
    public static PersonalIngressionData getOrCreatePersonalIngressionData(PrecedentDegreeInformation precedentInformation) {
        PersonalIngressionData personalData = null;
        personalData = precedentInformation.getPersonalIngressionData();
        Student student = AccessControl.getPerson().getStudent();
        if (personalData == null) {
            personalData = student.getPersonalIngressionDataByExecutionYear(ExecutionYear.readCurrentExecutionYear());
            if (personalData != null) {
                //if the student already has a PID it will have another PDI associated, it's necessary to add the new PDI
                personalData.addPrecedentDegreesInformations(precedentInformation);
            } else {
                personalData = new PersonalIngressionData(ExecutionYear.readCurrentExecutionYear(), precedentInformation);
            }
        }

        // It is necessary to create an early Registration so that the RAIDES objects are consistent
        // see PrecedentDegreeInformation.checkHasAllRegistrationOrPhdInformation()
        getOrCreateRegistration();

        return personalData;
    }

    private static Registration getOrCreateRegistration() {
        StudentCandidacy studentCandidacy = FirstTimeCandidacyController.getStudentCandidacy();
        Registration registration = studentCandidacy.getRegistration();
        if (registration != null) {
            return registration;
        }
        registration = new Registration(studentCandidacy.getPerson(), studentCandidacy);

        PrecedentDegreeInformation pdi = studentCandidacy.getPrecedentDegreeInformation();
        pdi.setRegistration(registration);
        pdi.getPersonalIngressionData().setStudent(studentCandidacy.getPerson().getStudent());

        DegreeCurricularPlan degreeCurricularPlan = studentCandidacy.getExecutionDegree().getDegreeCurricularPlan();
        ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        StudentCurricularPlan
                .createBolonhaStudentCurricularPlan(registration, degreeCurricularPlan, new YearMonthDay(), semester);

        RegistrationState registeredState = registration.getActiveState();
        registeredState.setStateDate(registeredState.getStateDate().minusMinutes(1));
        RegistrationState.createRegistrationState(registration, AccessControl.getPerson(), new DateTime(),
                RegistrationStateType.INACTIVE);

        return registration;
    }
}
