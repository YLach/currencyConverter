/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

public class InvalidConversion extends Exception {
    
    private static final long serialVersionUID = 16247164402L;

    public InvalidConversion(String mes) {
        super("Invalid conversion: " + mes);
    }   
}
