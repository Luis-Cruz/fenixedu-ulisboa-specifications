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

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.FenixeduUlisboaSpecificationsSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.domain.DisabilityType;
import org.fenixedu.ulisboa.specifications.domain.PersonUlisboaSpecifications;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;
import edu.emory.mathcs.backport.java.util.Collections;

@BennuSpringController(value = FirstTimeCandidacyController.class)
@RequestMapping(DisabilitiesFormController.CONTROLLER_URL)
public class DisabilitiesFormController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL = "/fenixedu-ulisboa-specifications/firsttimecandidacy/disabilitiesform";

    private static final String _FILLDISABILITIES_URI = "/filldisabilities";
    public static final String FILLDISABILITIES_URL = CONTROLLER_URL + _FILLDISABILITIES_URI;

    @RequestMapping(value = _FILLDISABILITIES_URI, method = RequestMethod.GET)
    public String filldisabilities(Model model) {
        List<DisabilityType> allDisabilities = DisabilityType.readAll().collect(Collectors.toList());
        Collections.sort(allDisabilities);
        model.addAttribute("disabilityTypeValues", allDisabilities);

        fillFormIfRequired(model);
        return "fenixedu-ulisboa-specifications/firsttimecandidacy/disabilitiesform/filldisabilities";
    }

    private void fillFormIfRequired(Model model) {
        if (!model.containsAttribute("disabilitiesForm")) {
            DisabilitiesForm form = new DisabilitiesForm();
            PersonUlisboaSpecifications personUlisboa = AccessControl.getPerson().getPersonUlisboaSpecifications();
            if (personUlisboa != null) {
                form.setHasDisabilities(personUlisboa.getHasDisabilities());
                if (personUlisboa.getDisabilityType() != null) {
                    form.setDisabilityType(personUlisboa.getDisabilityType());
                }
                form.setOtherDisabilityType(personUlisboa.getOtherDisabilityType());
                form.setNeedsDisabilitySupport(personUlisboa.getNeedsDisabilitySupport());
            }
            model.addAttribute("disabilitiesForm", form);
        }
    }

    @RequestMapping(value = _FILLDISABILITIES_URI, method = RequestMethod.POST)
    public String filldisabilities(DisabilitiesForm form, Model model, RedirectAttributes redirectAttributes) {
        if (!validate(form, model)) {
            return filldisabilities(model);
        }

        try {
            writeData(form);
            model.addAttribute("disabilitiesForm", form);
            return redirect(MotivationsExpectationsFormController.FILLMOTIVATIONSEXPECTATIONS_URL, model, redirectAttributes);
        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE, "label.error.create")
                    + de.getLocalizedMessage(), model);
            return filldisabilities(model);
        }
    }

    private boolean validate(DisabilitiesForm form, Model model) {
        if (form.getHasDisabilities()) {
            if ((form.getDisabilityType() == null) || form.getDisabilityType().isOther()
                    && StringUtils.isEmpty(form.getOtherDisabilityType())) {
                addErrorMessage(BundleUtil.getString(FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE,
                        "error.candidacy.workflow.DisabilitiesForm.disabilityType.must.be.filled"), model);
                return false;
            }
            if (form.getNeedsDisabilitySupport() == null) {
                addErrorMessage(BundleUtil.getString(FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE,
                        "error.candidacy.workflow.DisabilitiesForm.needsDisabilitySupport.must.be.filled"), model);
                return false;
            }
        }
        return true;
    }

    @Atomic
    protected void writeData(DisabilitiesForm form) {
        PersonUlisboaSpecifications personUlisboa = PersonUlisboaSpecifications.findOrCreate(AccessControl.getPerson());
        personUlisboa.setHasDisabilities(form.getHasDisabilities());
        if (form.getHasDisabilities()) {
            personUlisboa.setDisabilityType(form.getDisabilityType());
            personUlisboa.setOtherDisabilityType(form.getOtherDisabilityType());
            personUlisboa.setNeedsDisabilitySupport(form.getNeedsDisabilitySupport());
        } else {
            personUlisboa.setDisabilityType(null);
            personUlisboa.setOtherDisabilityType("");
            personUlisboa.setNeedsDisabilitySupport(false);
        }
    }

    public static class DisabilitiesForm {
        private boolean hasDisabilities = false;

        private DisabilityType disabilityType;

        private String otherDisabilityType;

        private Boolean needsDisabilitySupport = null;

        public boolean getHasDisabilities() {
            return hasDisabilities;
        }

        public void setHasDisabilities(boolean hasDisabilities) {
            this.hasDisabilities = hasDisabilities;
        }

        public DisabilityType getDisabilityType() {
            return disabilityType;
        }

        public void setDisabilityType(DisabilityType disabilityType) {
            this.disabilityType = disabilityType;
        }

        public String getOtherDisabilityType() {
            return otherDisabilityType;
        }

        public void setOtherDisabilityType(String otherDisabilityType) {
            this.otherDisabilityType = otherDisabilityType;
        }

        public Boolean getNeedsDisabilitySupport() {
            return needsDisabilitySupport;
        }

        public void setNeedsDisabilitySupport(Boolean needsDisabilitySupport) {
            this.needsDisabilitySupport = needsDisabilitySupport;
        }
    }
}
