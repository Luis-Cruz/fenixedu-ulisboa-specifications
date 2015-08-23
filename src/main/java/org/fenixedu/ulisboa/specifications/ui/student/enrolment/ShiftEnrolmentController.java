/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: shezad.anavarali@qub-it.com
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-specifications.
 *
 * FenixEdu fenixedu-ulisboa-specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu fenixedu-ulisboa-specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu fenixedu-ulisboa-specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.ulisboa.specifications.ui.student.enrolment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInClassesCandidate;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.ShiftToEnrol;
import org.fenixedu.academic.service.services.enrollment.shift.ReadShiftsToEnroll;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.struts.action.student.enrollment.EnrolmentContextHandler;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsController;
import org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy.util.UlisboaEnrolmentContextHandler;
import org.joda.time.DateTime;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.Atomic;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author shezad - Aug 11, 2015
 *
 */
@SpringFunctionality(app = FenixeduUlisboaSpecificationsController.class, title = "label.title.shiftEnrolment",
        accessGroup = "activeStudents")
@RequestMapping("/student/shiftEnrolment")
public class ShiftEnrolmentController extends FenixeduUlisboaSpecificationsBaseController {

    @RequestMapping
    public String home(Model model) {

        Registration selectedRegistration = (Registration) model.asMap().get("registration");
        EnrolmentPeriod selectedEnrolmentPeriod = (EnrolmentPeriod) model.asMap().get("enrolmentPeriod");

        UlisboaEnrolmentContextHandler ulisboaEnrolmentContextHandler = new UlisboaEnrolmentContextHandler();
        Optional<String> returnURL = ulisboaEnrolmentContextHandler.getReturnURLForStudentInShifts(request, selectedRegistration);
        if (returnURL.isPresent()) {
            request.setAttribute("returnURL", returnURL.get());
        }

        checkUserIfStudentAndOwnRegistration(selectedRegistration);

        final Student student = Authenticate.getUser().getPerson().getStudent();
        final List<EnrolmentPeriodDTO> enrolmentBeans = new ArrayList<EnrolmentPeriodDTO>();
        for (Registration registration : student.getRegistrationsToEnrolInShiftByStudent()) {
            for (EnrolmentPeriod enrolmentPeriod : registration.getActiveDegreeCurricularPlan().getEnrolmentPeriodsSet()) {
                ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
                if (isValidPeriodForUser(enrolmentPeriod, registration.getStudentCurricularPlan(currentExecutionYear),
                        currentExecutionYear)) {
                    if (ulisboaEnrolmentContextHandler.getReturnURLForStudentInShifts(request, registration).isPresent()
                            && registration != selectedRegistration) {
                        //If the registration has a returnURL (makes part of a workflow) and it is not the selected registration, skip it
                        continue;
                    }
                    if (returnURL.isPresent() && registration != selectedRegistration) {
                        //If we currently have a returnURL and the registration is not the selected registration, skip it
                        continue;
                    }
                    boolean selected = selectedRegistration == registration && selectedEnrolmentPeriod == enrolmentPeriod;
                    enrolmentBeans.add(new EnrolmentPeriodDTO(registration, enrolmentPeriod, selected));
                }
            }
        }

        if (!enrolmentBeans.isEmpty()) {
            enrolmentBeans.sort((eb1, eb2) -> eb1.compareTo(eb2));
            if (selectedRegistration == null || selectedEnrolmentPeriod == null) {
                final EnrolmentPeriodDTO selectedEnrolmentBean = enrolmentBeans.iterator().next();
                selectedEnrolmentBean.setSelected(true);
                selectedRegistration = selectedEnrolmentBean.getRegistration();
                selectedEnrolmentPeriod = selectedEnrolmentBean.getEnrolmentPeriod();
            }

            final ExecutionSemester executionSemester = selectedEnrolmentPeriod.getExecutionPeriod();

            try {
                final List<ShiftToEnrol> shiftsToEnrol =
                        ReadShiftsToEnroll.runReadShiftsToEnroll(selectedRegistration, executionSemester);
                model.addAttribute("shiftsToEnrol", shiftsToEnrol);
//                model.addAttribute("numberOfExecutionCoursesHavingNotEnroledShifts",
//                        getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(selectedRegistration, executionSemester));
            } catch (NotAuthorizedException e) {
                addErrorMessage(e.getLocalizedMessage(), model);
            } catch (FenixServiceException e) {
                addErrorMessage(e.getLocalizedMessage(), model);
//            addActionMessage(request, "error.enrollment.period.closed", exception.getArgs());
            } catch (DomainException e) {
                addErrorMessage(e.getLocalizedMessage(), model);
            }

        }

        model.addAttribute("enrolmentBeans", enrolmentBeans);

        return "student/shiftEnrolment/shiftEnrolment";
    }

    @RequestMapping(value = "/switchEnrolmentPeriod/{registrationOid}/{periodOid}")
    public String switchEnrolmentPeriod(@PathVariable("registrationOid") Registration registration,
            @PathVariable("periodOid") EnrolmentPeriod enrolmentPeriod, Model model) {

        model.addAttribute("registration", registration);
        model.addAttribute("enrolmentPeriod", enrolmentPeriod);

        return home(model);
    }

    @RequestMapping(value = "possibleShiftsToEnrol.json/{registrationOid}/{executionCourseOid}/{shiftType}")
    public @ResponseBody String getPossibleShiftsToEnrol(@PathVariable("registrationOid") Registration registration,
            @PathVariable("executionCourseOid") ExecutionCourse executionCourse, @PathVariable("shiftType") ShiftType shiftType) {

        checkUserIfStudentAndOwnRegistration(registration);

        final Set<Shift> shifts = new HashSet<>();

        // if student has a school class and it has shifts for EC and type, we only can allow him to add those shifts
        final Optional<SchoolClass> optionalSchoolClass = registration.getSchoolClassBy(executionCourse.getExecutionPeriod());
        if (optionalSchoolClass.isPresent()) {
            shifts.addAll(optionalSchoolClass.get().getAssociatedShiftsSet().stream()
                    .filter(s -> s.getExecutionCourse() == executionCourse && s.getTypes().contains(shiftType))
                    .collect(Collectors.toSet()));
        }

        // otherwise, he can chose any shift
        if (shifts.isEmpty()) {
            shifts.addAll(executionCourse.getShiftsByTypeOrderedByShiftName(shiftType));
        }

        final JsonArray result = new JsonArray();
        for (final Shift shift : shifts) {
            if (shift.getLotacao().intValue() > shift.getStudentsSet().size()) {
                JsonObject jsonShift = new JsonObject();
                jsonShift.addProperty("name", shift.getNome());
                jsonShift.addProperty("type", shift.getShiftTypesPrettyPrint());
                jsonShift.addProperty("lessons", shift.getLessonPresentationString());
                jsonShift.addProperty("externalId", shift.getExternalId());
                result.add(jsonShift);
            }
        }

        return new GsonBuilder().create().toJson(result);
    }

    @RequestMapping(value = "/addShift/{registrationOid}/{periodOid}/{shiftOid}")
    public String addShift(@PathVariable("registrationOid") Registration registration,
            @PathVariable("periodOid") EnrolmentPeriod enrolmentPeriod, @PathVariable("shiftOid") Shift shift, Model model) {

        checkUserIfStudentAndOwnRegistration(registration);

        try {
            addShiftService(registration, shift);
            addInfoMessage(BundleUtil.getString("resources.FenixeduUlisboaSpecificationsResources",
                    "message.shiftEnrolment.addShift.success"), model);
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        model.addAttribute("registration", registration);
        model.addAttribute("enrolmentPeriod", enrolmentPeriod);

        return home(model);
    }

    @Atomic
    protected void addShiftService(Registration registration, Shift shift) {
        if (!shift.reserveForStudent(registration)) {
            throw new DomainException("error.registration.replaceSchoolClass.shiftFull", shift.getNome(),
                    shift.getShiftTypesPrettyPrint(), shift.getExecutionCourse().getName());
        }
    }

    @RequestMapping(value = "/removeShift/{registrationOid}/{periodOid}/{shiftOid}")
    public String removeShift(@PathVariable("registrationOid") Registration registration,
            @PathVariable("periodOid") EnrolmentPeriod enrolmentPeriod, @PathVariable("shiftOid") Shift shift, Model model) {

        checkUserIfStudentAndOwnRegistration(registration);

        try {
            removeShiftService(registration, shift);
            addInfoMessage(BundleUtil.getString("resources.FenixeduUlisboaSpecificationsResources",
                    "message.shiftEnrolment.removeShift.success"), model);
        } catch (DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        model.addAttribute("registration", registration);
        model.addAttribute("enrolmentPeriod", enrolmentPeriod);

        return home(model);
    }

    @Atomic
    private void removeShiftService(Registration registration, Shift shift) {
        registration.removeShifts(shift);
    }

    //copied from Registration, but assuming all shiftTypes
//    private Integer getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(final Registration registration,
//            final ExecutionSemester executionSemester) {
//        int result = 0;
//        final List<Shift> enroledShifts = registration.getShiftsFor(executionSemester);
//        for (final ExecutionCourse executionCourse : registration.getAttendingExecutionCoursesFor(executionSemester)) {
//            for (final ShiftType shiftType : executionCourse.getShiftTypes()) {
//                if (!enroledShifts.stream().anyMatch(
//                        enroledShift -> enroledShift.getExecutionCourse() == executionCourse
//                                && enroledShift.containsType(shiftType))) {
//                    result++;
//                    break;
//                }
//            }
//        }
//        return result;
//    }

    @RequestMapping(value = "currentSchedule.json/{registrationOid}/{executionSemesterOid}",
            produces = "application/json; charset=utf-8")
    public @ResponseBody String schedule(@PathVariable("registrationOid") Registration registration,
            @PathVariable("executionSemesterOid") ExecutionSemester executionSemester) {

        checkUserIfStudentAndOwnRegistration(registration);

        final JsonArray result = new JsonArray();

        for (final Shift shift : registration.getShiftsFor(executionSemester)) {
            for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                final DateTime now = new DateTime();
                final DateTime weekDay = now.withDayOfWeek(lesson.getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat());
                final DateTime startTime =
                        weekDay.withTime(lesson.getBeginHourMinuteSecond().getHour(), lesson.getBeginHourMinuteSecond()
                                .getMinuteOfHour(), 0, 0);
                final DateTime endTime =
                        weekDay.withTime(lesson.getEndHourMinuteSecond().getHour(), lesson.getEndHourMinuteSecond()
                                .getMinuteOfHour(), 0, 0);

                final JsonObject event = new JsonObject();
                event.addProperty("id", lesson.getExternalId());
                event.addProperty("start", startTime.toString());
                event.addProperty("end", endTime.toString());
                event.addProperty("title", shift.getExecutionCourse().getName() + " (" + shift.getShiftTypesCodePrettyPrint()
                        + ")");
                result.add(event);
            }
        }

        return result.toString();
    }

    protected void checkUserIfStudentAndOwnRegistration(Registration registration) {
        final Student student = Authenticate.getUser().getPerson().getStudent();
        if (student == null || registration != null && registration.getStudent() != student) {
            throw new SecurityException("error.authorization.notOwnRegistration");
        }
    }

    public static class EnrolmentPeriodDTO implements Serializable, Comparable<EnrolmentPeriodDTO> {

        private final Registration registration;
        private final EnrolmentPeriod enrolmentPeriod;
        private Boolean selected;

        public EnrolmentPeriodDTO(Registration registration, EnrolmentPeriod enrolmentPeriod, Boolean selected) {
            super();
            this.registration = registration;
            this.enrolmentPeriod = enrolmentPeriod;
            this.selected = selected;
        }

        public Registration getRegistration() {
            return registration;
        }

        public EnrolmentPeriod getEnrolmentPeriod() {
            return enrolmentPeriod;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        @Override
        public int compareTo(EnrolmentPeriodDTO o) {
            int result = Degree.COMPARATOR_BY_NAME_AND_ID.compare(getRegistration().getDegree(), o.getRegistration().getDegree());
            return result == 0 ? getEnrolmentPeriod().getExecutionPeriod().compareTo(o.getEnrolmentPeriod().getExecutionPeriod()) : result;
        }

    }

    private boolean isValidPeriodForUser(EnrolmentPeriod ep, StudentCurricularPlan studentCurricularPlan,
            ExecutionYear currentExecutionYear) {
        // Coditions to be valid:
        // 1 - period has to be valid
        //     AND
        //          a - Student is candidate AND period is for candidate
        //            OR
        //          b - Period is for curricular courses (implicitly assuming student is not candidate)

        if (ep.isValid()) {
            if (studentCurricularPlan.isInCandidateEnrolmentProcess(currentExecutionYear)) {
                return ep instanceof EnrolmentPeriodInClassesCandidate;
            } else {
                return ep.isForClasses();
            }
        }
        return false;
    }
}
