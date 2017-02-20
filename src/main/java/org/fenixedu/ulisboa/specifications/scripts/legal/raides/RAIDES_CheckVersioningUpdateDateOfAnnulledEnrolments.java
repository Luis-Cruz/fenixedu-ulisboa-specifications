package org.fenixedu.ulisboa.specifications.scripts.legal.raides;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.treasury.util.Constants;

public class RAIDES_CheckVersioningUpdateDateOfAnnulledEnrolments extends CustomTask {

    @Override
    public void runTask() throws Exception {
        doIt();
        
        throw new RuntimeException("abort");
    }

    private void doIt() {
        final ExecutionYear ex = ExecutionYear.readCurrentExecutionYear();
        
        for (final ExecutionSemester es : ex.getExecutionPeriodsSet()) {
            for (final Enrolment enrolment : es.getEnrolmentsSet()) {
                if(!enrolment.isAnnulled()) {
                    continue;
                }
                
                taskLog("I\tANNULLED ENROLMENT\t%s\t%s\t%s\t%s\t%s\n", 
                        enrolment.getExternalId(),
                        enrolment.getCode(),
                        enrolment.getExecutionPeriod().getQualifiedName(),
                        enrolment.getStudent().getNumber(),
                        enrolment.getVersioningUpdateDate().getDate().toString(Constants.DATE_TIME_FORMAT_YYYY_MM_DD));
            }
        }
    }
}
