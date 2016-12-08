/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "Currencies")
public class Currency implements Serializable {  
    private static final long serialVersionUID = 16247164401L;
    
    
    @Id
    private String code;
    private String currencyName;
    
    // SEK is the reference currency to calculate rate conversion
    private double currencyPerSEKRate;
    
    public Currency() {
    }

    public Currency(String code, String currencyName, double currencyPerSEKRate) {
        this.code = code;
        this.currencyName = currencyName;
        this.currencyPerSEKRate = currencyPerSEKRate;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
      
    public void setCurrencyPerSEKRate(double rate) {
        this.currencyPerSEKRate = rate;
    }
    
    public double getCurrencyPerSEKRate() {
        return this.currencyPerSEKRate;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if (!this.code.equals(other.getCode())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Currency{" + "code=" + code + ", currencyName=" +
                currencyName + ", conversionRates=" + currencyPerSEKRate + " " + this.code + "/SEK }";
    }
}
