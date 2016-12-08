/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/* The container starts a new transaction before running a method 
   if the client is not associated with a transaction. 
   --> ensure that a method always runs within a new transaction.
*/
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless 
public class ConverterFacade {
    // Persistence Unit => defines the entities that are managed by an entity
    // manager & where store the entities
    @PersistenceContext(unitName = "CurrencyConverterPU")
    private EntityManager em;
    
    /**
     * Create a new Currency entity and make it be persistent
     * @param code
     * @param currencyName
     * @param rate 
     */
    public void createCurrency(String code, String currencyName, double rate) {
        Currency newCurrency = new Currency(code, currencyName, rate);
        em.persist(newCurrency);
    }
    
    public void modifyCurrencyRate(String code, double newRate) 
            throws InvalidConversion {
        Currency currencyToModify = em.find(Currency.class, code);
        if (currencyToModify != null)
            currencyToModify.setCurrencyPerSEKRate(newRate);
        else
            throw new InvalidConversion(code);
    }

    /**
     * Conversion of an amount from one currency to another
     * @param originalCurrency
     * @param convertedCurrency
     * @param amount
     * @return
     * @throws InvalidConversion 
     */
    public float conversion(String originalCurrency, String convertedCurrency,
            Float amount) throws InvalidConversion {
        Currency fromCurrency = em.find(Currency.class, originalCurrency);
        if (fromCurrency == null)
            throw new InvalidConversion("Invalid currency" + originalCurrency);
        
        Currency toCurrency = em.find(Currency.class, convertedCurrency);
        if (toCurrency == null)
            throw new InvalidConversion("Invalid currency" +  convertedCurrency);
        
        if (amount == null)
            throw new InvalidConversion("Invalid amount");
        
        Double res = (toCurrency.getCurrencyPerSEKRate() / 
                fromCurrency.getCurrencyPerSEKRate()) * amount;
        return res.floatValue();
    }
 
    /**
     * @return Returns the list of all the (persistent) currencies 
     */
    public Map<String, String> getAllCurrencies() {
        Map<String, String> currencies = new LinkedHashMap<>();
        List<Currency> curreniesObj = em.createQuery("SELECT c FROM Currencies c",
                Currency.class).getResultList();
        for (Currency c : curreniesObj)
            currencies.put(c.getCode() + " - " + c.getCurrencyName(),
                    c.getCode());
        
        return currencies;
    }
    
}
