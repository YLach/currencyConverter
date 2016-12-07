/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class ConverterFacade {
    @PersistenceContext(unitName = "CurrencyConverterPU")
    private EntityManager em;
    

    public void createCurrency(String code, String currencyName, double rate) {
        Currency newCurrency = new Currency(code, currencyName, rate);
        em.persist(newCurrency);
    }
    
    public void modifyCurrencyRate(String code, double newRate) {
        Currency currencyToModify = em.find(Currency.class, code);
        if (currencyToModify != null) {
            currencyToModify.setCurrencyPerSEKRate(newRate);
        } 
        // TODO: error handling
    }

    public double conversion(String originalCurrency, String convertedCurrency,
            double amount) throws UnknownCurrency {
        Currency fromCurrency = em.find(Currency.class, originalCurrency);
        if (fromCurrency == null)
            throw new UnknownCurrency(originalCurrency);
        
        Currency toCurrency = em.find(Currency.class, convertedCurrency);
        if (toCurrency == null)
            throw new UnknownCurrency(convertedCurrency);
        
        return ((fromCurrency.getCurrencyPerSEKRate() / 
                toCurrency.getCurrencyPerSEKRate()) * amount);
    }
 
    public Map<String, String> getAllCurrencies() {
        System.err.println("ICI");
        Map<String, String> currencies = new LinkedHashMap<>();
        List<Currency> curreniesObj = em.createQuery("SELECT c FROM Currencies c",
                                                        Currency.class).getResultList();
        for (Currency c : curreniesObj)
            currencies.put(c.getCode(), c.getCurrencyName());
        
        return currencies;
    }
    
}
