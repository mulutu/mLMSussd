/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpango.ussd;

/**
 *
 * @author jmulutu
 */
public class UssdSession {
    
    private String sessionID;
    private byte serviceCode;
    
    public UssdSession(){}

    /**
     * @return the sessionID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * @return the serviceCode
     */
    public byte getServiceCode() {
        return serviceCode;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @param serviceCode the serviceCode to set
     */
    public void setServiceCode(byte serviceCode) {
        this.serviceCode = serviceCode;
    }
    
    
}
