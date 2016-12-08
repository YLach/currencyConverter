/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.view;

import converter.controller.ConverterFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


@Named(value = "convManager")
@RequestScoped
public class ConvManager implements Serializable {

    @EJB
    private ConverterFacade converterFacade;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Float amountToConvert;
    private Float amountConverted;
    private Map<String, String> currencies;
    private Exception transactionFailure;
     

    @PostConstruct
    public void initCurrencies() {
        if (converterFacade.getAllCurrencies().isEmpty()) {
            
            converterFacade.createCurrency("EUR", "Euros", 0.1023976481d);
            converterFacade.createCurrency("SEK", "Swedish Krona", 1d);
            converterFacade.createCurrency("USD", "US Dollar", 0.1100736983d);
            converterFacade.createCurrency("BGP", "British Pound", 0.0872583118);
        }
    }
    
    public void check() throws IOException{
    if (amountToConvert == null) {
        FacesContext.getCurrentInstance().getExternalContext().redirect("converter.xhtml");
    }
}
    
    
    public String conversion() {
        try {
            amountConverted = converterFacade.conversion(fromCurrencyCode,
                toCurrencyCode, Math.abs(amountToConvert));
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
    
    private void handleException(Exception e) {
        e.printStackTrace(System.err);
        transactionFailure = e;
        amountConverted = null;
    }
    
    public boolean getSuccess() {
        if (transactionFailure == null && amountConverted != null)
            System.err.println("SUCCESS");
        else
            System.err.println("ERROR");
        return (transactionFailure == null && amountConverted != null);
    }

    /**
     * Returns the latest thrown exception.
     */
    public Exception getException() {
        return transactionFailure;
    }

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-config navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix() {
        return "";
    }
    
    public ConverterFacade getConverterFacade() {
        return converterFacade;
    }

    public void setConverterFacade(ConverterFacade converterFacade) {
        this.converterFacade = converterFacade;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }
    
    public String getToCurrencyCode() {
        return toCurrencyCode;
    }
    
    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public Float getAmountToConvert() {
        return this.amountToConvert;
    }

    public void setAmountToConvert(Float amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public Map<String, String> getCurrencies() {
        currencies = converterFacade.getAllCurrencies();
        return currencies;
    }    

    public Float getAmountConverted() {
        return amountConverted;
    }

    public void setAmountConverted(Float amountConverted) {
        this.amountConverted = amountConverted;
    }
    
    
}
