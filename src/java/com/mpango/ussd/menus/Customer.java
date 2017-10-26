/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.menus;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author jmulutu
 */
public class Customer {

    private int customerID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String email;
    private Date dateJoin;
    private Date birthDay;
    private String gender;
    private String MSISDN;
    private String ID_NUMBER;
    private String loanLimit;
    private boolean hasLoan = false;
    private String activeLoanRef = "";
    private String PIN;
    private String FOSAAccountNumber;
    
    
    public Customer(){}

    /**
     * @return the customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the dateJoin
     */
    public Date getDateJoin() {
        return dateJoin;
    }

    /**
     * @return the birthDay
     */
    public Date getBirthDay() {
        return birthDay;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the MSISDN
     */
    public String getMSISDN() {
        return MSISDN;
    }

    /**
     * @return the ID_NUMBER
     */
    public String getID_NUMBER() {
        return ID_NUMBER;
    }

    /**
     * @return the loanLimit
     */
    public String getLoanLimit() {
        return loanLimit;
    }

    /**
     * @return the hasLoan
     */
    public boolean isHasLoan() {
        return hasLoan;
    }

    /**
     * @return the activeLoanRef
     */
    public String getActiveLoanRef() {
        return activeLoanRef;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param dateJoin the dateJoin to set
     */
    public void setDateJoin(Date dateJoin) {
        this.dateJoin = dateJoin;
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @param MSISDN the MSISDN to set
     */
    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    /**
     * @param ID_NUMBER the ID_NUMBER to set
     */
    public void setID_NUMBER(String ID_NUMBER) {
        this.ID_NUMBER = ID_NUMBER;
    }

    /**
     * @param loanLimit the loanLimit to set
     */
    public void setLoanLimit(String loanLimit) {
        this.loanLimit = loanLimit;
    }

    /**
     * @param hasLoan the hasLoan to set
     */
    public void setHasLoan(boolean hasLoan) {
        this.hasLoan = hasLoan;
    }

    /**
     * @param activeLoanRef the activeLoanRef to set
     */
    public void setActiveLoanRef(String activeLoanRef) {
        this.activeLoanRef = activeLoanRef;
    }

    /**
     * @return the PIN
     */
    public String getPIN() {
        return PIN;
    }

    /**
     * @param PIN the PIN to set
     */
    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    /**
     * @return the FOSAAccountNumber
     */
    public String getFOSAAccountNumber() {
        return FOSAAccountNumber;
    }

    /**
     * @param FOSAAccountNumber the FOSAAccountNumber to set
     */
    public void setFOSAAccountNumber(String FOSAAccountNumber) {
        this.FOSAAccountNumber = FOSAAccountNumber;
    }

   
    
    

}
