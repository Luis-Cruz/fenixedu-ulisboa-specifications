/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
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
package org.fenixedu.ulisboa.specifications.ui.firstyearconfiguration;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.bennu.FenixeduUlisboaSpecificationsSpringConfiguration;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.ulisboa.specifications.domain.FirstYearRegistrationConfiguration;
import org.fenixedu.ulisboa.specifications.domain.FirstYearRegistrationGlobalConfiguration;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@SpringFunctionality(app = FenixeduUlisboaSpecificationsController.class, title = "label.title.firstYearConfiguration",
        accessGroup = "logged")
@RequestMapping(FirstYearRegistrationConfigurationController.CONTROLLER_URL)
public class FirstYearRegistrationConfigurationController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration";

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    @Atomic
    public void deleteFirstYearRegistrationConfiguration(FirstYearRegistrationConfiguration firstYearRegistrationConfiguration) {
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(Model model) {
        List<FirstYearRegistrationConfigurationBean> searchfirstyearregistrationconfigurationResultsDataSet =
                getSearchUniverseDataSet();

        model.addAttribute("searchfirstyearregistrationconfigurationResultsDataSet",
                searchfirstyearregistrationconfigurationResultsDataSet);
        model.addAttribute("firstYearRegistrationGlobalConfiguration", FirstYearRegistrationGlobalConfiguration.getInstance());
        return "fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/search";
    }

    private static final String SEARCH_TO_EDIT_URI = "/search/edit";
    public static final String SEARCH_TO_EDIT_URL = CONTROLLER_URL + SEARCH_TO_EDIT_URI;

    @RequestMapping(value = SEARCH_TO_EDIT_URL)
    public String processSearchToEdit(Model model, RedirectAttributes redirectAttributes) {
        return redirect("/fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/edit", model,
                redirectAttributes);
    }

    private static final String _EDIT_URI = "/edit";
    public static final String EDIT_URL = CONTROLLER_URL + _EDIT_URI;

    @RequestMapping(value = _EDIT_URI)
    public String edit(Model model) {
        List<FirstYearRegistrationConfigurationBean> editResultsDataSet = getSearchUniverseDataSet();

        model.addAttribute("editResultsDataSet", editResultsDataSet);
        return "fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/edit";
    }

    @RequestMapping(value = _EDIT_URI + "/save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @Atomic
    public void saveLine(FirstYearRegistrationConfigurationBean bean) {
        FirstYearRegistrationConfiguration firstYearRegistrationConfiguration =
                bean.getDegree().getFirstYearRegistrationConfiguration();
        if (bean.hasFieldsAsTrue()) {

            if (firstYearRegistrationConfiguration == null) {
                firstYearRegistrationConfiguration = new FirstYearRegistrationConfiguration(bean.getDegree());
            }
            firstYearRegistrationConfiguration.setRequiresClassesEnrolment(bean.getRequiresClassesEnrolment());
            firstYearRegistrationConfiguration.setRequiresShiftsEnrolment(bean.getRequiresShiftsEnrolment());
            firstYearRegistrationConfiguration.setRequiresCoursesEnrolment(bean.getRequiresCoursesEnrolment());
            firstYearRegistrationConfiguration.setRequiresVaccination(bean.getRequiresVaccination());
        } else {
            if (firstYearRegistrationConfiguration != null) {
                firstYearRegistrationConfiguration.delete();
            }
        }
    }

    @Atomic
    @RequestMapping(value = _EDIT_URI + "/uploadTemplate", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public String uploadTemplate(@RequestParam(value = "mod43Template", required = true) MultipartFile mod43Template,
            Model model, RedirectAttributes redirectAttributes) {
        try {
            String fileName = mod43Template.getOriginalFilename();
            byte[] fileContent = mod43Template.getBytes();
            FirstYearRegistrationGlobalConfiguration.getInstance().uploadMod43Template(fileName, fileContent);
            List<FirstYearRegistrationConfigurationBean> searchfirstyearregistrationconfigurationResultsDataSet =
                    getSearchUniverseDataSet();

            model.addAttribute("searchfirstyearregistrationconfigurationResultsDataSet",
                    searchfirstyearregistrationconfigurationResultsDataSet);
            model.addAttribute("firstYearRegistrationGlobalConfiguration", FirstYearRegistrationGlobalConfiguration.getInstance());
            return "fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/search";
        } catch (Exception e) {
            addErrorMessage((BundleUtil.getString(FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE,
                    "label.error.uploadMod43") + e.getLocalizedMessage()), model);
            List<FirstYearRegistrationConfigurationBean> searchfirstyearregistrationconfigurationResultsDataSet =
                    getSearchUniverseDataSet();

            model.addAttribute("searchfirstyearregistrationconfigurationResultsDataSet",
                    searchfirstyearregistrationconfigurationResultsDataSet);
            model.addAttribute("firstYearRegistrationGlobalConfiguration", FirstYearRegistrationGlobalConfiguration.getInstance());
            return "fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/search";
        }
    }

    @Atomic
    @RequestMapping(value = _SEARCH_URI + "/cleanTemplate", method = RequestMethod.GET)
    public String cleanTemplate(Model model, RedirectAttributes redirectAttributes) {

        FirstYearRegistrationGlobalConfiguration.getInstance().cleanTemplate();

        List<FirstYearRegistrationConfigurationBean> searchfirstyearregistrationconfigurationResultsDataSet =
                getSearchUniverseDataSet();

        model.addAttribute("searchfirstyearregistrationconfigurationResultsDataSet",
                searchfirstyearregistrationconfigurationResultsDataSet);
        model.addAttribute("firstYearRegistrationGlobalConfiguration", FirstYearRegistrationGlobalConfiguration.getInstance());
        return "fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/search";
    }

    private List<FirstYearRegistrationConfigurationBean> getSearchUniverseDataSet() {
        return Bennu.getInstance().getDegreesSet().stream().filter(d -> d.isActive())
                .map(d -> new FirstYearRegistrationConfigurationBean(d)).collect(Collectors.toList());
    }

    public static class FirstYearRegistrationConfigurationBean {

        private Degree degree;
        private boolean requiresVaccination;
        private boolean requiresCoursesEnrolment;
        private boolean requiresClassesEnrolment;
        private boolean requiresShiftsEnrolment;

        public Degree getDegree() {
            return this.degree;
        }

        public String getDegreeExternalId() {
            return degree.getExternalId();
        }

        public void setDegreeExternalId(String degreeExternalId) {
            this.degree = FenixFramework.getDomainObject(degreeExternalId);
        }

        public String getDegreeName() {
            return this.degree.getNameI18N().getContent();
        }

        public String getDegreeCode() {
            return this.degree.getCode();
        }

        public boolean getRequiresVaccination() {
            return requiresVaccination;
        }

        public void setRequiresVaccination(boolean value) {
            requiresVaccination = value;
        }

        public boolean getRequiresCoursesEnrolment() {
            return requiresCoursesEnrolment;
        }

        public void setRequiresCoursesEnrolment(boolean value) {
            requiresCoursesEnrolment = value;
        }

        public boolean getRequiresClassesEnrolment() {
            return requiresClassesEnrolment;
        }

        public void setRequiresClassesEnrolment(boolean value) {
            requiresClassesEnrolment = value;
        }

        public FirstYearRegistrationConfigurationBean() {

        }

        public FirstYearRegistrationConfigurationBean(Degree degree) {
            this.degree = degree;
            if (degree.getFirstYearRegistrationConfiguration() != null) {
                FirstYearRegistrationConfiguration firstYearRegistrationConfiguration =
                        degree.getFirstYearRegistrationConfiguration();
                this.setRequiresVaccination(firstYearRegistrationConfiguration.getRequiresVaccination());
                this.setRequiresCoursesEnrolment(firstYearRegistrationConfiguration.getRequiresCoursesEnrolment());
                this.setRequiresClassesEnrolment(firstYearRegistrationConfiguration.getRequiresClassesEnrolment());
                this.setRequiresVaccination(firstYearRegistrationConfiguration.getRequiresVaccination());
                this.setRequiresCoursesEnrolment(firstYearRegistrationConfiguration.getRequiresCoursesEnrolment());
                this.setRequiresClassesEnrolment(firstYearRegistrationConfiguration.getRequiresClassesEnrolment());
                this.setRequiresShiftsEnrolment(firstYearRegistrationConfiguration.getRequiresShiftsEnrolment());
            }
        }

        public boolean hasFieldsAsTrue() {
            return requiresClassesEnrolment || requiresCoursesEnrolment || requiresShiftsEnrolment || requiresVaccination;
        }

        public boolean getRequiresShiftsEnrolment() {
            return requiresShiftsEnrolment;
        }

        public void setRequiresShiftsEnrolment(boolean requiresShiftsEnrolment) {
            this.requiresShiftsEnrolment = requiresShiftsEnrolment;
        }
    }
}
