package org.fenixedu.ulisboa.specifications.service.report.registrationhistory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class RegistrationHistoryReportService {

    private Set<ExecutionYear> enrolmentExecutionYears = Sets.newHashSet();
    private Set<DegreeType> degreeTypes = Sets.newHashSet();
    private Set<Degree> degrees = Sets.newHashSet();
    private Set<RegistrationRegimeType> regimeTypes = Sets.newHashSet();
    private Set<RegistrationProtocol> registrationProtocols = Sets.newHashSet();
    private Set<IngressionType> ingressionTypes = Sets.newHashSet();
    private Set<RegistrationStateType> registrationStateTypes = Sets.newHashSet();
    private Set<StatuteType> statuteTypes = Sets.newHashSet();
    private Boolean firstTimeOnly;
    private Boolean withEnrolments;
    private Boolean dismissalsOnly;
    private Boolean improvementEnrolmentsOnly;
    private Integer studentNumber;
    private Collection<ProgramConclusion> programConclusionsToFilter = Sets.newHashSet();

    private Set<ProgramConclusion> programConclusions = Sets.newHashSet();

    private boolean detailed = true;

    private Set<ExecutionYear> graduatedExecutionYears = Sets.newHashSet();
    private LocalDate graduationPeriodStartDate;
    private LocalDate graduationPeriodEndDate;

    private static final Comparator<RegistrationConclusionBean> CONCLUSION_BEAN_COMPARATOR = (x, y) -> {

        if (x.isConclusionProcessed() && !y.isConclusionProcessed()) {
            return 1;
        }

        if (!x.isConclusionProcessed() && y.isConclusionProcessed()) {
            return -1;
        }

        return StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE.compare(x.getStudentCurricularPlan(),
                y.getStudentCurricularPlan());
    };

    public RegistrationHistoryReportService() {

    }

    public void filterEnrolmentExecutionYears(Collection<ExecutionYear> executionYears) {
        this.enrolmentExecutionYears.addAll(executionYears);
    }

    public void filterGraduatedExecutionYears(Collection<ExecutionYear> executionYears) {
        this.graduatedExecutionYears.addAll(executionYears);
    }

    public void filterDegreeTypes(Collection<DegreeType> degreeTypes) {
        this.degreeTypes.addAll(degreeTypes);
    }

    public void filterDegrees(Collection<Degree> degrees) {
        this.degrees.addAll(degrees);
    }

    public void filterRegimeTypes(Collection<RegistrationRegimeType> regimeTypes) {
        this.regimeTypes.addAll(regimeTypes);
    }

    public void filterRegistrationProtocols(Collection<RegistrationProtocol> protocols) {
        this.registrationProtocols.addAll(protocols);
    }

    public void filterIngressionTypes(Collection<IngressionType> ingressionTypes) {
        this.ingressionTypes.addAll(ingressionTypes);
    }

    public void filterRegistrationStateTypes(Collection<RegistrationStateType> registrationStateTypes) {
        this.registrationStateTypes.addAll(registrationStateTypes);
    }

    public void filterStatuteTypes(Collection<StatuteType> statuteTypes) {
        this.statuteTypes.addAll(statuteTypes);
    }

    public void filterFirstTimeOnly(Boolean firstTime) {
        this.firstTimeOnly = firstTime;
    }

    public void filterWithEnrolments(final Boolean input) {
        this.withEnrolments = input;
    }

    public void filterDismissalsOnly(Boolean dismissalsOnly) {
        this.dismissalsOnly = dismissalsOnly;
    }

    public void filterImprovementEnrolmentsOnly(Boolean improvementsEnrolmentsOnly) {
        this.improvementEnrolmentsOnly = improvementsEnrolmentsOnly;
    }

    public void filterStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void filterGraduationPeriodStartDate(final LocalDate startDate) {
        this.graduationPeriodStartDate = startDate;
    }

    public void filterGraduationPeriodEndDate(final LocalDate endDate) {
        this.graduationPeriodEndDate = endDate;
    }

    public void filterProgramConclusions(Set<ProgramConclusion> programConclusions) {
        this.programConclusionsToFilter.addAll(programConclusions);
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public Collection<RegistrationHistoryReport> generateReport() {

        this.programConclusions = calculateProgramConclusions().stream()
                .filter(pc -> this.programConclusionsToFilter.isEmpty() || this.programConclusionsToFilter.contains(pc))
                .collect(Collectors.toSet());

        final Set<RegistrationHistoryReport> enrolmentResult = new HashSet<RegistrationHistoryReport>();
        for (final ExecutionYear executionYear : this.enrolmentExecutionYears) {
            enrolmentResult.addAll(processEnrolled(executionYear));
        }

        if (this.graduatedExecutionYears.isEmpty()) {
            return enrolmentResult;
        }

        final Set<RegistrationHistoryReport> finalResult =
                new HashSet<RegistrationHistoryReport>(processGraduated(enrolmentResult));

        return finalResult;
    }

    private Set<ProgramConclusion> calculateProgramConclusions() {
        final Set<ProgramConclusion> result = Sets.newHashSet();

        final Set<DegreeType> degreeTypesToProcess =
                this.degreeTypes.isEmpty() ? DegreeType.all().collect(Collectors.toSet()) : this.degreeTypes;
        for (final DegreeType degreeType : degreeTypesToProcess) {
            for (final Degree degree : degreeType.getDegreeSet()) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    result.addAll(ProgramConclusion.conclusionsFor(degreeCurricularPlan).collect(Collectors.toSet()));
                }
            }
        }

        return result;

    }

    private Collection<RegistrationHistoryReport> processGraduated(final Set<RegistrationHistoryReport> enrolmentResult) {

        final Set<RegistrationHistoryReport> graduatedResultSet = Sets.newHashSet();

        for (RegistrationHistoryReport reportEntry : enrolmentResult) {
            for (final ProgramConclusion programConclusion : this.programConclusions) {

                final RegistrationConclusionBean conclusionBean = reportEntry.getConclusionReportFor(programConclusion);

                if (conclusionBean == null) {
                    continue;
                }

                if (!conclusionBean.isConcluded()) {
                    continue;
                }

                final ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
                final LocalDate conclusionDate = conclusionBean.getConclusionDate().toLocalDate();

                if (!this.graduatedExecutionYears.contains(conclusionYear)) {
                    continue;
                }

                if (graduationPeriodStartDate != null && conclusionDate.isBefore(graduationPeriodStartDate)) {
                    continue;
                }

                if (graduationPeriodEndDate != null && conclusionDate.isAfter(graduationPeriodEndDate)) {
                    continue;
                }

                graduatedResultSet.add(reportEntry);
            }
        }

        return graduatedResultSet;
    }

    private Collection<RegistrationHistoryReport> processEnrolled(final ExecutionYear executionYear) {

        //TODO: common filters should be cached
        final Predicate<Registration> degreeTypeFilter =
                r -> this.degreeTypes.isEmpty() || this.degreeTypes.contains(r.getDegreeType());

        final Predicate<Registration> degreeFilter = r -> this.degrees.isEmpty() || this.degrees.contains(r.getDegree());

        final Predicate<Registration> regimeTypeFilter =
                r -> this.regimeTypes.isEmpty() || this.regimeTypes.contains(r.getRegimeType(executionYear));

        final Predicate<Registration> protocolFilter =
                r -> this.registrationProtocols.isEmpty() || this.registrationProtocols.contains(r.getRegistrationProtocol());

        final Predicate<Registration> ingressionTypeFilter =
                r -> this.ingressionTypes.isEmpty() || this.ingressionTypes.contains(r.getIngressionType());

        final Predicate<Registration> registrationStateFilter =
                r -> this.registrationStateTypes.isEmpty() || r.getLastRegistrationState(executionYear) != null
                        && this.registrationStateTypes.contains(r.getLastRegistrationState(executionYear).getStateType());

        final Predicate<Registration> statuteTypeFilter = r -> this.statuteTypes.isEmpty()
                || r.getStudent().getStudentStatutesSet().stream().filter(s -> s.isValidOn(executionYear))
                        .anyMatch(s -> this.statuteTypes.contains(s.getType()))
                || r.getStudentStatutesSet().stream().filter(s -> s.isValidOn(executionYear))
                        .anyMatch(s -> this.statuteTypes.contains(s.getType()));

        final Predicate<Registration> firstTimeFilter =
                r -> this.firstTimeOnly == null || this.firstTimeOnly.booleanValue() && r.getRegistrationYear() == executionYear
                        || !this.firstTimeOnly.booleanValue() && r.getRegistrationYear() != executionYear;

        final Predicate<Registration> studentNumberFilter =
                r -> this.studentNumber == null || this.studentNumber.intValue() == r.getStudent().getNumber().intValue()
                        || this.studentNumber.intValue() == r.getNumber().intValue();

        return buildSearchUniverse(executionYear).stream()

                .filter(studentNumberFilter)

                .filter(firstTimeFilter).filter(degreeTypeFilter)

                .filter(degreeFilter).filter(regimeTypeFilter)

                .filter(protocolFilter).filter(ingressionTypeFilter)

                .filter(registrationStateFilter).filter(statuteTypeFilter)

                .map(r -> buildRegistrationHistoryReport(r, executionYear)).collect(Collectors.toSet());

    }

    protected Set<Registration> buildSearchUniverse(final ExecutionYear executionYear) {

        final Set<Registration> result = Sets.newHashSet();

        if (this.dismissalsOnly != null && this.dismissalsOnly.booleanValue()) {
            result.addAll(executionYear.getExecutionPeriodsSet().stream().flatMap(ep -> ep.getCreditsSet().stream())
                    .map(c -> c.getStudentCurricularPlan().getRegistration()).collect(Collectors.toSet()));
        }

        if (this.improvementEnrolmentsOnly != null && this.improvementEnrolmentsOnly.booleanValue()) {
            result.addAll(executionYear.getExecutionPeriodsSet().stream()
                    .flatMap(e -> e.getEnrolmentEvaluationsSet().stream().map(ev -> ev.getRegistration()))
                    .collect(Collectors.toSet()));
        }

        if (this.withEnrolments == null || !this.withEnrolments.booleanValue()) {
            // registration/start execution year relation
            result.addAll(executionYear.getStudentsSet());
        }

        result.addAll(executionYear.getRegistrationDataByExecutionYearSet().stream()
                .filter(r -> !r.getRegistration().getEnrolments(executionYear).isEmpty()).map(r -> r.getRegistration())
                .collect(Collectors.toSet()));

        return result;
    }

    private RegistrationHistoryReport buildRegistrationHistoryReport(Registration registration, ExecutionYear executionYear) {
        final RegistrationHistoryReport result = new RegistrationHistoryReport(registration, executionYear);

        if (detailed) {
            addConclusion(result);
            addCurriculum(result);
            final Collection<Enrolment> enrolmentsByYear = result.getRegistration().getEnrolments(result.getExecutionYear());
            addEnrolmentsAndCreditsCount(result, enrolmentsByYear);
            addExecutionYearAverages(result, enrolmentsByYear);
        }

        return result;
    }

    private void addEnrolmentsAndCreditsCount(RegistrationHistoryReport result, Collection<Enrolment> enrolmentsByYear) {

        final Predicate<Enrolment> normalFilter = normalEnrolmentFilter(result);
        final Predicate<Enrolment> extraCurricularFilter = extraCurricularEnrolmentFilter();
        final Predicate<Enrolment> standaloneFilter = standaloneEnrolmentFilter();

        result.setEnrolmentsCount(countFiltered(enrolmentsByYear, normalFilter));
        result.setEnrolmentsCredits(sumCredits(enrolmentsByYear, normalFilter));

        result.setExtraCurricularEnrolmentsCount(countFiltered(enrolmentsByYear, extraCurricularFilter));
        result.setExtraCurricularEnrolmentsCredits(sumCredits(enrolmentsByYear, extraCurricularFilter));

        result.setStandaloneEnrolmentsCount(countFiltered(enrolmentsByYear, standaloneFilter));
        result.setStandaloneEnrolmentsCredits(sumCredits(enrolmentsByYear, standaloneFilter));
    }

    protected Predicate<Enrolment> standaloneEnrolmentFilter() {
        return e -> e.isStandalone();
    }

    protected Predicate<Enrolment> extraCurricularEnrolmentFilter() {
        return e -> e.isExtraCurricular();
    }

    protected Predicate<Enrolment> normalEnrolmentFilter(RegistrationHistoryReport result) {
        return e -> (e.getCurriculumGroup().isInternalCreditsSourceGroup()
                || !e.getCurriculumGroup().isNoCourseGroupCurriculumGroup())
                && (e.getParentCycleCurriculumGroup() == null || !e.getParentCycleCurriculumGroup().isExternal());
    }

    private int countFiltered(Collection<Enrolment> enrolments, Predicate<Enrolment> filter) {
        return (int) enrolments.stream().filter(filter.and(e -> !e.isAnnulled())).count();
    }

    private BigDecimal sumCredits(Collection<Enrolment> enrolments, Predicate<Enrolment> filter) {
        return enrolments.stream().filter(filter.and(e -> !e.isAnnulled())).map(e -> e.getEctsCreditsForCurriculum())
                .reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
    }

    private void addExecutionYearAverages(RegistrationHistoryReport report, Collection<Enrolment> enrolmentsByYear) {
        report.setExecutionYearSimpleAverage(calculateSimpleAverage(report, enrolmentsByYear));
        report.setExecutionYearWeightedAverage(calculateWeightedAverage(report, enrolmentsByYear));

    }

    private BigDecimal calculateWeightedAverage(RegistrationHistoryReport report, Collection<Enrolment> enrolmentsByYear) {
        BigDecimal gradesSum = BigDecimal.ZERO;
        BigDecimal creditsSum = BigDecimal.ZERO;
        for (final Enrolment enrolment : enrolmentsByYear.stream().filter(normalEnrolmentFilter(report))
                .filter(e -> e.isApproved() && e.getGrade().isNumeric()).collect(Collectors.toSet())) {
            gradesSum = gradesSum.add(enrolment.getGrade().getNumericValue().multiply(enrolment.getEctsCreditsForCurriculum()));
            creditsSum = creditsSum.add(enrolment.getEctsCreditsForCurriculum());
        } ;

        return gradesSum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : gradesSum.divide(creditsSum, MathContext.DECIMAL128)
                .setScale(3, RoundingMode.HALF_UP);
    }

    protected BigDecimal calculateSimpleAverage(RegistrationHistoryReport report, Collection<Enrolment> enrolmentsByYear) {
        BigDecimal gradesSum = BigDecimal.ZERO;
        int total = 0;
        for (final Enrolment enrolment : enrolmentsByYear.stream().filter(normalEnrolmentFilter(report))
                .filter(e -> e.isApproved() && e.getGrade().isNumeric()).collect(Collectors.toSet())) {
            gradesSum = gradesSum.add(enrolment.getGrade().getNumericValue());
            total++;
        } ;

        return gradesSum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : gradesSum
                .divide(BigDecimal.valueOf(total), MathContext.DECIMAL128).setScale(3, RoundingMode.HALF_UP);
    }

    private void addCurriculum(RegistrationHistoryReport result) {
        result.setCurriculum(result.getStudentCurricularPlan().getCurriculum(new DateTime(), result.getExecutionYear()));
    }

    private void addConclusion(final RegistrationHistoryReport result) {

        final Multimap<ProgramConclusion, RegistrationConclusionBean> conclusions = ArrayListMultimap.create();
        for (final StudentCurricularPlan studentCurricularPlan : result.getRegistration().getStudentCurricularPlansSet()) {
            for (final ProgramConclusion programConclusion : ProgramConclusion.conclusionsFor(studentCurricularPlan)
                    .collect(Collectors.toSet())) {
                conclusions.put(programConclusion, new RegistrationConclusionBean(studentCurricularPlan, programConclusion));
            }
        }

        for (final ProgramConclusion programConclusion : this.programConclusions) {

            if (!conclusions.containsKey(programConclusion)) {
                result.addEmptyConclusion(programConclusion);
            } else {
                final Collection<RegistrationConclusionBean> conclusionsByProgramConclusion = conclusions.get(programConclusion);
                if (conclusionsByProgramConclusion.size() == 1) {
                    result.addConclusion(programConclusion, conclusionsByProgramConclusion.iterator().next());
                } else {
                    result.addConclusion(programConclusion, conclusionsByProgramConclusion.stream()
                            .sorted(CONCLUSION_BEAN_COMPARATOR.reversed()).findFirst().get());
                }
            }

        }
    }

}
