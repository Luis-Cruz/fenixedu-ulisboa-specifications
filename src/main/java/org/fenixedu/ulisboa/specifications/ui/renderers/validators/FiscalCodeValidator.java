package org.fenixedu.ulisboa.specifications.ui.renderers.validators;

import static org.fenixedu.treasury.util.Constants.treasuryBundle;

import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class FiscalCodeValidator extends HtmlValidator {

    private static final long serialVersionUID = 1L;

    private String countryCode;
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    @Override
    public void performValidation() {
        final String fiscalCode = getComponent().getValue();
        if(Strings.isNullOrEmpty(fiscalCode)) {
            setValid(true);
            return;
        }
        
        setValid(TreasuryBridgeAPIFactory.implementation().isValidFiscalNumber(countryCode, fiscalCode));
        if(!isValid()) {
            setMessage(treasuryBundle("label.DebtAccountController.invalidFiscalCode"));
        }
    }

}
