/**
 *  Copyright © 2015 Universidade de Lisboa
 *  
 *  This file is part of FenixEdu fenixedu-ulisboa-specifications.
 *  
 *  FenixEdu fenixedu-ulisboa-specifications is free software: you can redistribute 
 *  it and/or modify it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  FenixEdu fenixedu-ulisboa-specifications is distributed in the hope that it will
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with FenixEdu fenixedu-ulisboa-specifications.
 *  If not, see <http://www.gnu.org/licenses/>.
 **/
package org.fenixedu.ulisboa.specifications.domain.student.access.importation;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.StandByCandidacySituation;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.ulisboa.specifications.domain.ULisboaSpecificationsRoot;
import org.fenixedu.ulisboa.specifications.domain.candidacy.FirstTimeCandidacy;
import org.fenixedu.ulisboa.specifications.domain.student.access.DegreeCandidateDTO;
import org.fenixedu.ulisboa.specifications.domain.student.access.DegreeCandidateDTO.MatchingPersonException;
import org.fenixedu.ulisboa.specifications.domain.student.access.DegreeCandidateDTO.NotFoundPersonException;
import org.fenixedu.ulisboa.specifications.domain.student.access.DegreeCandidateDTO.TooManyMatchedPersonsException;
import org.fenixedu.ulisboa.specifications.domain.student.access.StudentAccessServices;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.core.WriteOnReadError;

import com.google.common.collect.Lists;

public class DgesStudentImportationProcess extends DgesStudentImportationProcess_Base {

    protected DgesStudentImportationProcess() {
        super();
    }

    public DgesStudentImportationProcess(ExecutionYear executionYear, Space space, EntryPhase entryPhase,
            DgesStudentImportationFile dgesStudentImportationFile) {
        this();

        init(executionYear, space, entryPhase, dgesStudentImportationFile);
    }

    private void init(final ExecutionYear executionYear, final EntryPhase entryPhase) {
        String[] args = new String[0];
        if (executionYear == null) {
            throw new DomainException("error.DgesBaseProcess.execution.year.is.null", args);
        }
        String[] args1 = new String[0];
        if (entryPhase == null) {
            throw new DomainException("error.DgesBaseProcess.entry.phase.is.null", args1);
        }

        setExecutionYear(executionYear);
        setEntryPhase(entryPhase);
    }

    private void init(ExecutionYear executionYear, Space space, EntryPhase entryPhase,
            DgesStudentImportationFile dgesStudentImportationFile) {
        init(executionYear, entryPhase);

        String[] args = new String[0];

        if (space == null) {
            throw new DomainException("error.DgesStudentImportationProcess.campus.is.null", args);
        }
        String[] args1 = {};
        if (dgesStudentImportationFile == null) {
            throw new DomainException("error.DgesStudentImportationProcess.importation.file.is.null", args1);
        }

        setSpace(space);
        setDgesStudentImportationFile(dgesStudentImportationFile);
    }

    private transient PrintWriter LOG_WRITER = null;

    @Override
    public QueueJobResult execute() throws Exception {
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            LOG_WRITER = new PrintWriter(new BufferedOutputStream(stream));

            importCandidates();
        } catch (WriteOnReadError e) {
            throw e;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }

        finally {
            LOG_WRITER.close();
            stream.close();
        }

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("text/plain");

        queueJobResult.setContent(stream.toByteArray());

        stream.close();
        return queueJobResult;
    }

    public void importCandidates() {
        Locale.setDefault(new Locale("pt", "PT"));
        List<DegreeCandidateDTO> degreeCandidateDTOs =
                parseDgesFile(getDgesStudentImportationFile().getContent(), getEntryPhase());

        LOG_WRITER.println(String.format("DGES Entries for %s : %s", getSpace().getName(), degreeCandidateDTOs.size()));

        createDegreeCandidacies(degreeCandidateDTOs);
    }

    protected List<DegreeCandidateDTO> parseDgesFile(byte[] contents, EntryPhase entryPhase) {
        List<DegreeCandidateDTO> result;
        try {
            result = readContentToDtos(contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setConstantFields(entryPhase, result);
        return result;

    }

    private void setConstantFields(EntryPhase entryPhase, Collection<DegreeCandidateDTO> result) {
        for (final DegreeCandidateDTO degreeCandidateDTO : result) {
            degreeCandidateDTO.setEntryPhase(entryPhase);
        }
    }

    protected List<DegreeCandidateDTO> readContentToDtos(final byte[] contents) throws IOException {
        List<DegreeCandidateDTO> result = Lists.newArrayList();

        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(contents), Charset.forName("ISO-8859-15")));

        String personalDataLine = null;
        while ((personalDataLine = reader.readLine()) != null) {
            if (StringUtils.isEmpty(personalDataLine.trim())) {
                continue;
            }

            final DegreeCandidateDTO degreeCandidateDTO = new DegreeCandidateDTO();
            fillMainInformation(degreeCandidateDTO, personalDataLine);

            final String addressLine = reader.readLine();

            if (StringUtils.isEmpty(addressLine.trim())) {
                continue;
            }

            fillAddressAndContactsInformation(degreeCandidateDTO, addressLine);

            result.add(degreeCandidateDTO);
        }

        return result;
    }

    /**
     * <pre>
     * tipo_linha  char(1)     valor '*'
     * 
     * curso_sup   char(4)     codigo curso colocacao  (ver codigos no Guia)
     * num_bi      char(9)     numero de b.i. do aluno
     * loc_bi      char(2)     local emissao do b.i.  (ver codigos)
     * sexo        char(1)     sexo do aluno
     * data_nasc   char(8)     data de nascimento AAAAMMDD
     * conting     char(1)     contingente candidatura (ver codigos)
     * prefcol     char(1)     nr de opcao VALIDA a q corresp. colocacao (1-6)
     * etapcol     num(2)      etapa de colocacao (1-17)
     * 
     * estado      char(2)     não utilizado
     * exclusao    char(1)     não utilizado
     * militar     char(1)     não utilizado
     * deficiente  char(1)     não utilizado
     * GAES        char(3)     não utilizado
     * numCand     num(5)      não utilizado
     * 
     * pontos      num(5,1)    nota crit. seriacao para este curso (0.0-100.0)
     * nome        char(60)    nome (sem espacos a direita)
     * </pre>
     */
    private static int[] MAIN_INFORMATION_LINE_FIELD_SPEC = new int[] { 1, 4, 9, 2, 1, 8, 1, 1, 2, 13, 5, 60 };

    /**
     * <pre>
     * tipo_linha   char(1)     valor '#'
     * 
     * morada1      char(40)    morada do aluno - parte 1
     * morada2      char(40)    morada do aluno - parte 2
     * codpos       char(4)     codigo postal
     * codlocal     char(40)    localidade
     * telefone     char(40)    telefone para contacto
     * </pre>
     */
    private static int[] ADDRESS_AND_CONTACTS_LINE_FIELD_SPEC = new int[] { 1, 40, 40, 4, 40, 40 };

    protected void fillMainInformation(DegreeCandidateDTO degreeCandidateDTO, String line) {
        String[] fields = splitLine(line, MAIN_INFORMATION_LINE_FIELD_SPEC);
        degreeCandidateDTO.setDegreeCode(fields[1].trim());
        degreeCandidateDTO.setDocumentIdNumber(fields[2].trim());
        degreeCandidateDTO.setGender(parseGender(fields[4].trim()));

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime date = formatter.parseDateTime(fields[5].trim());
        degreeCandidateDTO.setDateOfBirth(date.toYearMonthDay());

        degreeCandidateDTO.setContigent(fields[6].trim());
        degreeCandidateDTO.setPlacingOption(Integer.valueOf(fields[7].trim()));
        degreeCandidateDTO.setIngression(getIngression(degreeCandidateDTO.getContigent()));
        BigDecimal entryGrade = new BigDecimal(fields[10].trim().replace(",", "."));
        degreeCandidateDTO.setEntryGrade(entryGrade.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        BigDecimal highSchoolEntryGrade = entryGrade.setScale(2, RoundingMode.HALF_EVEN);
        highSchoolEntryGrade = highSchoolEntryGrade.divide(new BigDecimal(10), RoundingMode.HALF_EVEN);
        degreeCandidateDTO.setHighSchoolFinalGrade(highSchoolEntryGrade.setScale(0, RoundingMode.HALF_EVEN).toString());
        degreeCandidateDTO.setName(fields[11].trim());
    }

    private Gender parseGender(String gender) {
        if (gender.equals("M")) {
            return Gender.MALE;
        } else if (gender.equals("F")) {
            return Gender.FEMALE;
        }

        throw new RuntimeException("Unknown gender: " + gender);
    }

    private IngressionType getIngression(String contigent) {
        IngressionType ingression = ULisboaSpecificationsRoot.getInstance().getIngressionType(contigent);
        if (ingression == null) {
            throw new RuntimeException("Contigent: " + contigent
                    + " is not mapped to any IngressionType. Please configure an IngressionType for contigent: " + contigent);
        }
        return ingression;
    }

    protected void fillAddressAndContactsInformation(DegreeCandidateDTO degreeCandidateDTO, String line) {
        String[] fields = splitLine(line, ADDRESS_AND_CONTACTS_LINE_FIELD_SPEC);

        degreeCandidateDTO.setAddress(fields[1].trim() + " " + fields[2].trim());
        degreeCandidateDTO.setPhoneNumber(fields[5].trim());
    }

    private String[] splitLine(String line, int[] fieldSizeSpec) {
        String[] result = new String[fieldSizeSpec.length];
        int currentIndex = 0;
        for (int i = 0; i < fieldSizeSpec.length; i++) {
            int fieldSize = fieldSizeSpec[i];
            if (i == fieldSizeSpec.length - 1) {
                result[i] = line.substring(currentIndex);
            } else {
                result[i] = line.substring(currentIndex, currentIndex + fieldSize);
            }
            currentIndex = currentIndex + fieldSize;
        }
        return result;
    }

    private void createDegreeCandidacies(List<DegreeCandidateDTO> degreeCandidateDTOs) {
        int processed = 0;
        int personsCreated = 0;

        Authenticate.mock(User.findByUsername("manager"));
        try {
            for (final DegreeCandidateDTO degreeCandidateDTO : degreeCandidateDTOs) {

                if (++processed % 150 == 0) {
                    System.out.println("Processed :" + processed);
                }

                LOG_WRITER.println("-------------------------------------------------------------------");
                LOG_WRITER.println("Processar: " + degreeCandidateDTO.toString());

                Person person = null;
                try {
                    person = matchingPerson(degreeCandidateDTO);
                    // Person may not yet have a user, so we will create it
                    if (person.getUser() == null) {
                        person.setUser(new User(person.getProfile()));
                    }
                } catch (DegreeCandidateDTO.NotFoundPersonException e) {
                    person = degreeCandidateDTO.createPerson();
                    LOG_WRITER.println("Pessoa Criada");
                    personsCreated++;
                } catch (DegreeCandidateDTO.TooManyMatchedPersonsException e) {
                    LOG_WRITER.println(String.format(
                            "Candidato com a Identificação %s possui mais do que uma identidade no sistema",
                            degreeCandidateDTO.getDocumentIdNumber()));
                    continue;
                } catch (DegreeCandidateDTO.MatchingPersonException e) {
                    throw new RuntimeException(e);
                }

                if ((person.getStudent() != null) && !person.getStudent().getRegistrationsSet().isEmpty()) {
                    LOG_WRITER.println(String.format("Candidato com a Identificação %s é aluno com o nº %s com matriculas",
                            degreeCandidateDTO.getDocumentIdNumber(), person.getStudent().getNumber()));

                    boolean hasEnrolmentsInExecutionYear = false;
                    for (Registration registration : person.getStudent().getActiveRegistrations()) {
                        if (registration.getEnrolments(getExecutionYear()).size() > 0) {
                            LOG_WRITER
                                    .println(String
                                            .format("Candidato com a Identificação %s é aluno com o nº %s possui matriculas com inscrições no ano lectivo de candidatura",
                                                    degreeCandidateDTO.getDocumentIdNumber(), person.getStudent().getNumber()));
                            hasEnrolmentsInExecutionYear = true;
                            break;
                        }
                    }

                    if (hasEnrolmentsInExecutionYear) {
                        continue;
                    }
                }

                if (person.getTeacher() != null) {
                    LOG_WRITER.println(String.format("Candidato com a Identificação %s é docente com o username %s",
                            degreeCandidateDTO.getDocumentIdNumber(), person.getUsername()));
                    continue;
                }

                if (person.getStudent() == null) {
                    new Student(person);
                    LOG_WRITER.println("Aluno Criado");
                }

                RoleType.CANDIDATE.actualGroup().grant(person.getUser());

                StudentCandidacy studentCandidacy = createCandidacy(degreeCandidateDTO, person);
                new StandByCandidacySituation(studentCandidacy, getPerson());

                StudentAccessServices.triggerSyncPersonToExternal(person);
            }
        } finally {
            Authenticate.unmock();
        }
    }

    public static List<DgesStudentImportationProcess> readDoneJobs(ExecutionYear executionYear) {
        List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();
        CollectionUtils.select(executionYear.getDgesStudentImportationProcessSet(), new Predicate() {
            @Override
            public boolean evaluate(Object process) {
                return (process instanceof DgesStudentImportationProcess) && ((QueueJob) process).getDone();
            }
        }, jobList);
        return jobList;
    }

    public static List<DgesStudentImportationProcess> readUndoneJobs(ExecutionYear executionYear) {
        return new ArrayList(CollectionUtils.subtract(readAllJobs(executionYear), readDoneJobs(executionYear)));
    }

    public static List<DgesStudentImportationProcess> readAllJobs(ExecutionYear executionYear) {
        List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();
        CollectionUtils.select(executionYear.getDgesStudentImportationProcessSet(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof DgesStudentImportationProcess);
            }
        }, jobList);
        return jobList;
    }

    public static boolean canRequestJob() {
        return QueueJob.getUndoneJobsForClass(DgesStudentImportationProcess.class).isEmpty();
    }

    private Person matchingPerson(DegreeCandidateDTO degreeCandidateDTO) throws MatchingPersonException {
        Collection<Person> persons = Person.readByDocumentIdNumber(degreeCandidateDTO.getDocumentIdNumber());

        if (persons.isEmpty()) {
            throw new NotFoundPersonException();
        }

        if (persons.size() > 1) {
            throw new TooManyMatchedPersonsException();
        }

        final Person person = persons.iterator().next();

        if (person.getDateOfBirthYearMonthDay() != null
                && person.getDateOfBirthYearMonthDay().compareTo(degreeCandidateDTO.getDateOfBirth()) == 0) {
            return person;
        }

        if (person.getName().equalsIgnoreCase(degreeCandidateDTO.getName())) {
            return person;
        }

        throw new TooManyMatchedPersonsException();
    }

    private StudentCandidacy createCandidacy(DegreeCandidateDTO degreeCandidateDTO, Person person) {
        ExecutionDegree executionDegree = degreeCandidateDTO.getExecutionDegree(getExecutionYear(), getSpace());
        StudentCandidacy candidacy = null;

        if (executionDegree.getDegree().getDegreeType().isBolonhaDegree()
                || executionDegree.getDegree().getDegreeType().isIntegratedMasterDegree()) {
            candidacy =
                    new FirstTimeCandidacy(person, executionDegree, getPerson(), degreeCandidateDTO.getEntryGrade(),
                            degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(),
                            degreeCandidateDTO.getEntryPhase(), degreeCandidateDTO.getPlacingOption());
        } else {
            throw new RuntimeException("Unexpected degree type from DGES file");
        }

        candidacy.setHighSchoolType(degreeCandidateDTO.getHighSchoolType());
        candidacy.setFirstTimeCandidacy(true);
        candidacy.getPrecedentDegreeInformation().setConclusionGrade(degreeCandidateDTO.getHighSchoolFinalGrade());
        candidacy.setDgesStudentImportationProcess(this);

        return candidacy;
    }

    public static java.util.function.Predicate<Registration> registrationHasDgesImportationProcessForCurrentYear() {
        return new java.util.function.Predicate<Registration>() {
            @Override
            public boolean test(Registration r) {
                return r.getStudentCandidacy() != null
                        && r.getStudentCandidacy().getDgesStudentImportationProcess() != null
                        && r.getStudentCandidacy().getDgesStudentImportationProcess().getExecutionYear() == ExecutionYear
                                .readCurrentExecutionYear();
            }
        };
    }
}
