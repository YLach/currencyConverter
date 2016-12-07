/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

/**
 *
 * @author YannL
 */
public class UnknownCurrency extends Exception {
    
    private static final long serialVersionUID = 16247164402L;

    public UnknownCurrency(String currencyName) {
        super("Invalid currency: " + currencyName);
    }   
}
