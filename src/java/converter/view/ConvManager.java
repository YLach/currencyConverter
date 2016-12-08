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
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;


@Named(value = "convManager")
@RequestScoped
public class ConvManager implements Serializable {
    private static final long serialVersionUID = 16247164401L;
    
    @EJB
    private ConverterFacade converterFacade;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Float amountToConvert;
    private Float amountConverted;
    private Map<String, String> currencies; // Key : label, Value : code
    private Exception transactionFailure;
     
    /**
     * Called once the managed bean is constructed
     * Initialization of the currencies
     */
    @PostConstruct
    public void initCurrencies() {
        if (converterFacade.getAllCurrencies().isEmpty()) {
            converterFacade.createCurrency("EUR", "Euros", 0.1023976481d);
            converterFacade.createCurrency("SEK", "Swedish Krona", 1d);
            converterFacade.createCurrency("USD", "US Dollar", 0.1100736983d);
            converterFacade.createCurrency("BGP", "British Pound", 0.0872583118);
        }
    }
    
    /**
     * Called once the submit button is pressed
     * Conversion of the amount get from the HTML page from the currency 
     * corresponding to fromCurrencyCode to the currency corresponding to
     * toCurrencyCode
     */
    public String conversion() {
        try {
            transactionFailure = null;
            amountConverted = converterFacade.conversion(fromCurrencyCode,
                toCurrencyCode, amountToConvert);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
    
    /**
     * Security against URL forging
     * @throws IOException 
     */
    public void checkConversion() throws IOException{
        if (amountToConvert == null) {
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect("converter.xhtml");
        }
    }
    
    /**
     * Security against URL forging
     * @throws IOException 
     */
    public void checkError() throws IOException{
        if (transactionFailure == null) {
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect("converter.xhtml");
        }
    }
    
    
    
    private void handleException(Exception e) {
        e.printStackTrace(System.err);
        transactionFailure = e;
        amountConverted = null;
    }
    
    /**
     * Checks whether an error occurred or not. Used by the dynamic navigation
     * rules
     */
    public boolean getSuccess() {
        return (transactionFailure == null && amountConverted != null);
    }

    /**
     * Returns the latest thrown exception.
     */
    public Exception getException() {
        return transactionFailure;
    }

    // Getters & setters accessed by the JSF pages

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
        if (amountToConvert != null)
            this.amountToConvert = Math.abs(amountToConvert);
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
}
