/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-specifications.
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
package org.fenixedu.academic.ui.renderers.providers.enrollment.bolonha;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.dto.student.IStudentCurricularPlanBean;
import org.fenixedu.academic.ui.renderers.providers.ExecutionPeriodsForDismissalsStudentCurricularPlanProvider;
import org.fenixedu.academic.ui.renderers.providers.ExecutionPeriodsForStudentCurricularPlanProvider;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @see {@link ExecutionPeriodsForStudentCurricularPlanProvider} and
 *      {@link ExecutionPeriodsForDismissalsStudentCurricularPlanProvider}
 * 
 */
public class ExecutionPeriodsForEnrolmentProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {

        final StudentCurricularPlan studentCurricularPlan = ((IStudentCurricularPlanBean) source).getStudentCurricularPlan();
        final LocalDate beginDate = new LocalDate(studentCurricularPlan.getStartDateYearMonthDay());
        final LocalDate endDate = new LocalDate(getLastExecutionYear(studentCurricularPlan).getEndDateYearMonthDay());

        final SortedSet<ExecutionSemester> result = new TreeSet<ExecutionSemester>(Collections.reverseOrder());
        result.addAll(ExecutionSemester.readExecutionPeriodsInTimePeriod(beginDate, endDate));

        return result;
    }

    static private ExecutionYear getLastExecutionYear(final StudentCurricularPlan plan) {
        final SortedSet<ExecutionYear> result = Sets.newTreeSet(ExecutionYear.COMPARATOR_BY_YEAR);

        final ExecutionYear lastScpExecutionYear = plan.getLastExecutionYear();
        if (lastScpExecutionYear != null) {
            result.add(lastScpExecutionYear);
        }

        final DegreeCurricularPlan dcp = plan.getDegreeCurricularPlan();
        result.addAll(getEndContextExecutionYears(dcp.getRoot()));

        if (result.isEmpty()) {
            result.add(ExecutionYear.readCurrentExecutionYear());
        }

        return result.last();
    }

    static public Set<ExecutionYear> getEndContextExecutionYears(final CourseGroup input) {
        final Set<ExecutionYear> result = Sets.newHashSet();
        for (final Context context : input.getChildContexts(CourseGroup.class)) {
            final ExecutionSemester endSemester = context.getEndExecutionPeriod();
            if (endSemester != null) {
                result.add(endSemester.getExecutionYear());
            }
            result.addAll(getEndContextExecutionYears((CourseGroup) context.getChildDegreeModule()));
        }
        return result;
    }

}
