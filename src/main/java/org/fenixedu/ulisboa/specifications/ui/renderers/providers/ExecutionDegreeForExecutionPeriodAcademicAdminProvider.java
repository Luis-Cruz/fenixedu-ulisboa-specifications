/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.ulisboa.specifications.ui.renderers.providers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;
import org.fenixedu.academic.predicate.AcademicPredicates;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreeForExecutionPeriodAcademicAdminProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();

        final HasExecutionSemester hasExecutionSemester = (HasExecutionSemester) source;
        final ExecutionSemester executionPeriod = hasExecutionSemester.getExecutionPeriod();
        if (executionPeriod != null) {
            final ExecutionYear executionYear = executionPeriod.getExecutionYear();
            executionDegrees.addAll(executionYear.getExecutionDegreesSet());
        }

        
        TreeSet<ExecutionDegree> result =
                new TreeSet<ExecutionDegree>(Comparator.comparing(ExecutionDegree::getPresentationName));

        // ist150958: eliminate degrees for which there are no permissions
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (AcademicPredicates.MANAGE_EXECUTION_COURSES.evaluate(executionDegree.getDegree())) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
