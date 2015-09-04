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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.AdmittedCandidacySituation;
import org.fenixedu.academic.domain.candidacy.RegisteredCandidacySituation;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.domain.student.access.StudentAccessServices;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = FirstTimeCandidacyController.class)
@RequestMapping("/fenixedu-ulisboa-specifications/firsttimecandidacy/finished")
public class FinishedController extends FenixeduUlisboaSpecificationsBaseController {

    @RequestMapping
    public String finished(Model model) {
        Registration registration = FirstTimeCandidacyController.getStudentCandidacy().getRegistration();
        Student student = registration.getStudent();
        StudentAccessServices.triggerSyncStudentToExternal(student);
        return "fenixedu-ulisboa-specifications/firsttimecandidacy/finished";
    }

    @RequestMapping(value = "/printalldocuments", produces = "application/pdf")
    public ResponseEntity<byte[]> finishedToPrintAllDocuments(Model model, RedirectAttributes redirectAttributes) {
        Person person = AccessControl.getPerson();
        StudentCandidacy candidacy = FirstTimeCandidacyController.getStudentCandidacy();
        closeStudentCandidacy(person, candidacy);

        byte[] pdfBytes = candidacy.getSummaryFile().getContent();
        String filename = person.getStudent().getNumber() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline;filename=" + filename);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
        return response;
    }

    @Atomic
    private void closeStudentCandidacy(Person person, StudentCandidacy candidacy) {
        AdmittedCandidacySituation situation = new AdmittedCandidacySituation(candidacy, person);
        situation.setSituationDate(situation.getSituationDate().minusMinutes(1));

        new RegisteredCandidacySituation(candidacy, person);
    }
}
