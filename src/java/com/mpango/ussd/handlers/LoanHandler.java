/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.handlers;

import com.mpango.ussd.IdGenerator;
import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.menus.Customer;
import com.mpango.ussd.menus.Handler2;
import com.mpango.ussd.menus.Input2;
import com.mpango.ussd.menus.Node2;
import com.mpango.ussd.menus.Selection2;
import com.mpango.ussd.session.Session;
import com.mpango.ussd.session.SessionParamName;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;

/**
 *
 * @author jmulutu
 */
public class LoanHandler implements Handler2 {
    
    static final Logger logger = Logger.getLogger(LoanHandler.class.getName());

    private int state = 0;
    private int loanamount = 0;
    private int loanterm = 0;
    private int confirm = 0;
    private String sessionID = null;
    private String service = "";
    private int paymentMode = 0;
    private int repaymentAmount = 0;
    private MathContext mc = MathContext.DECIMAL64;

    @Override
    public String handler(MoUssdReq moUssdReq) {
        String result = "";
        String userinput = moUssdReq.getMessage();
        Session sessionObject = moUssdReq.getSession();
        String customerMSISDN = (String) sessionObject.get(SessionParamName.CUSTOMER_MSISDN);
        String newSessionID = (String) sessionObject.get(SessionParamName.SESSION_ID);
        Selection2 USSDMenuFinal = loanservices();

        if (sessionID != null) {
            if (newSessionID.equalsIgnoreCase(sessionID)) {
                //
            } else {
                sessionID = newSessionID;
                state = 0;
            }
        } else {
            sessionID = newSessionID;
            state = 0;
        }

        if (state == 0) {
            result = result + USSDMenuFinal.render();
            state++;
        } else if (state == 1) {
            int loanServiceID = Integer.parseInt(userinput);
            if (loanServiceID == 1) {
                service = "reqLoan";
                Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                String loanLimit = cust.getLoanLimit();
                String loanNumber = cust.getActiveLoanRef();
                if (loanNumber.equalsIgnoreCase("") || loanNumber == null) {
                    result = result + "You qualify for Kes. " + loanLimit + ".\n";
                    result = result + "Please enter the loan amount:\n";
                    state++;
                } else {
                    result = result + "You have an active loan: " + loanNumber + ". ";
                    result = result + "Please pay-up first.\n\n";
                    result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                    state = 0;
                    sessionID = null;
                }
            } else if (loanServiceID == 2) {
                service = "repayLoan";

                // check if the customer has an active loan
                Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                String loanNumber = cust.getActiveLoanRef();

                if (loanNumber.equalsIgnoreCase("") || loanNumber == null) {
                    result = result + "You do not have an active loan.\n\n";
                    result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                    state = 0;
                    sessionID = null;
                } else {
                    result = result + "Select repayment mode:\n";
                    result = result + "1. Pay full amount\n";
                    result = result + "2. Pay partial amount\n";
                    state++;
                }
            } else if (loanServiceID == 3) {
                service = "loanBal";
                Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                String loanNumber = cust.getActiveLoanRef();
                if (loanNumber.equalsIgnoreCase("") || loanNumber == null) {
                    result = result + "You do not have an active loan.\n\n";
                    result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                } else {
                    result = result + LoanBalanceReq(customerMSISDN, loanNumber);
                }
                state = 0;
                sessionID = null;
            }
        } else {
            // SERVICE: REQUEST LOAN 
            if (service.equalsIgnoreCase("reqLoan")) {
                if (state == 2) {
                    loanamount = Integer.parseInt(userinput);
                    result = result + "Please select the loan term:\n1. One Month\n2. Three months";
                    state++;
                } else if (state == 3) {
                    loanterm = Integer.parseInt(userinput);
                    result = result + "Please confirm: Type 1:\n";
                    state++;
                } else if (state == 4) {
                    confirm = Integer.parseInt(userinput);
                    if (confirm == 1) {
                        BigDecimal loanamount_ = new BigDecimal(loanamount, mc);
                        int LoanTypeID = 1;
                        String response = ISOLoanRequest(customerMSISDN, loanamount_, loanterm, LoanTypeID);
                        if (response.equalsIgnoreCase("00")) {
                            result = " Your loan request for " + loanamount + "  for " + loanterm + " is being processed \n";
                            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                            state = 0;
                            sessionID = null;
                        } else if (response.equalsIgnoreCase("16")) {
                            Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                            String loanNumber = cust.getActiveLoanRef();
                            result = "You have an existing loan: Loan number " + loanNumber + "\n";
                            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                            state = 0;
                            sessionID = null;
                        } else if (response.equalsIgnoreCase("50")) {
                            result = "You have an existing loan application in the system.\n";
                            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                            state = 0;
                            sessionID = null;
                        } else {
                            result = "An error occured while processing your request.\n";
                            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                            state = 0;
                            sessionID = null;
                        }
                        sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
                        moUssdReq.setSession(sessionObject);
                    } else {
                        result = result + "You cancelled your loan request.\n0: Main menu\n00: Loans menu\n000: Logout";
                        sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
                        moUssdReq.setSession(sessionObject);
                        state = 0;
                        sessionID = null;
                    }
                }
            } else if (service.equalsIgnoreCase("loanBal")) {
                //
            } else if (service.equalsIgnoreCase("repayLoan")) {
                if (state == 2) {
                    paymentMode = Integer.parseInt(userinput);
                    if (paymentMode == 1) {
                        Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                        String loanNumber = cust.getActiveLoanRef();
                        String loanBal = getLoanBalanceAmount(customerMSISDN, loanNumber);
                        result = result + "Enter 1 to confirm payment of KES. " + loanBal + " for your loan\n";
                        state++;
                    } else if (paymentMode == 2) {
                        result = result + "Enter the amount to pay:\n";
                        state++;
                    }
                } else if (state == 3) {
                    if (paymentMode == 1) {
                        //result = result + "Your loan repayment is being processed.\n";
                        //result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        // CALL LOAN REAYMENT ISO PROCESSOR
                        Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                        String loanNumber = cust.getActiveLoanRef();
                        String FOSAAccountNumber = cust.getFOSAAccountNumber();
                        int loanBal = Integer.parseInt(getLoanBalanceAmount(customerMSISDN, loanNumber));
                        result = result + RequestLoanRepayment(customerMSISDN, loanNumber, loanBal, FOSAAccountNumber);
                        state = 0;
                        sessionID = null;
                    } else if (paymentMode == 2) {
                        repaymentAmount = Integer.parseInt(userinput);
                        result = result + "Enter 1 to confirm payment of KES." + repaymentAmount + " for your loan\n";
                        state++;
                    }
                } else if (state == 4) {
                    if (paymentMode == 2) {
                        int repayConfirm = Integer.parseInt(userinput);
                        if (repayConfirm == 1) {
                            // CALL LOAN REAYMENT ISO PROCESSOR
                            Customer cust = (Customer) sessionObject.get(SessionParamName.CUSTOMER_OBJECT);
                            String loanNumber = cust.getActiveLoanRef();
                            String FOSAAccountNumber = cust.getFOSAAccountNumber();
                            result = result + RequestLoanRepayment(customerMSISDN, loanNumber, repaymentAmount, FOSAAccountNumber);
                        } else {
                            result = result + "Your loan repayment has been cancelled.\n";
                            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        }
                        state = 0;
                        sessionID = null;
                    }
                }
            }
        }
        return result;
    }

    private Selection2 loanservices() {

        Selection2 loanservicemenu = new Selection2();
        loanservicemenu.setNodeID("mainmenu");

        Node2[] children = new Node2[3];

        Input2 reqloan = new Input2();
        reqloan.setTitle("request loan");
        reqloan.setInstruction("request loan service:");
        reqloan.setNodeID("loanservicemenu");
        LoanHandler reqloanHandler = new LoanHandler();
        reqloan.setHandler(reqloanHandler);

        Input2 repayLoan = new Input2();
        repayLoan.setTitle("Repay Loan");
        repayLoan.setNodeID("loanBal");
        repayLoan.setInstruction("Repay Loan service:");
        LoanHandler repayLoanHandler = new LoanHandler();
        repayLoan.setHandler(repayLoanHandler);

        Input2 loanBal = new Input2();
        loanBal.setTitle("Loan bal");
        loanBal.setNodeID("loanBal");
        loanBal.setInstruction("Loan bal service:");
        LoanHandler loanBalHandler = new LoanHandler();
        loanBal.setHandler(loanBalHandler);

        children[0] = reqloan;
        children[1] = repayLoan;
        children[2] = loanBal;

        loanservicemenu.setTitle("Select loan services:");
        loanservicemenu.setChildren(children);

        return loanservicemenu;
    }

    private static String ISOLoanRequest(String customerMSISDN, BigDecimal loanAmount, int RepaymentPeriod, int LoanTypeID) {
        String response = "01";

        IdGenerator randomGen = new IdGenerator();
        String formattedAmount = formatAmount(loanAmount);
        int transactionTypeID = 1; // Loan application        
        String processingCode = "400000";
        String ISOMTI = "0200";

        String transactionRef = "LR" + randomGen.generateId(5);
        try {
            ISOPackager p = new GenericPackager("cfg/packager/iso87ascii.xml");
            ISOMsg m = new ISOMsg();
            m.setPackager(p);
            //m.setHeader("ISO016000055".getBytes());
            m.setMTI(ISOMTI);
            m.set("2", customerMSISDN);
            m.set("3", processingCode);
            m.set("4", formattedAmount);
            m.set("9", Integer.toString(RepaymentPeriod));
            m.set("18", "0001"); // 18 ==lenth 4 ; transaction/loan type
            m.set("37", transactionRef);
            //m.set("42", "IDNUMBER"); //CustomerIDNumber);

            ISOChannel channel = new ASCIIChannel("localhost", 9800, p);

            channel.connect();
            channel.send(m);
            ISOMsg r = channel.receive();
            channel.disconnect();

            if (r.hasField(39)) {
                String field39 = (String) r.getValue(39);
                response = field39;
            }

        } catch (ISOException | IOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }

    private static String LoanBalanceReq(String customerMSISDN, String loanNumber) {
        String response = "";
        ISOMsg r = ISOLoanBalanceReq(customerMSISDN, loanNumber);
        String field39;

        try {
            field39 = (String) r.getValue(39);
            String field44 = (String) r.getValue(44);
            if (field39.equalsIgnoreCase("00")) {
                response = field44;
                response = "Your loan " + loanNumber + " has a balance of " + field44 + ".\n";
                response = response + "0. Main menu\n00: Loans menu\n000. Logout\n";
            } else if (field39.equalsIgnoreCase("19")) {
                response = "Your loan " + loanNumber + " has a been fully repaid.\n";
                response = response + "0. Main menu\n00: Loans menu\n000. Logout\n";
            } else {
                response = "An error occured. Please try again later.\n";
                response = response + "0. Main menu\n00: Loans menu\n000. Logout\n";
            }
        } catch (ISOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    private static String getLoanBalanceAmount(String customerMSISDN, String loanNumber) {
        String balance = "";
        ISOMsg r = ISOLoanBalanceReq(customerMSISDN, loanNumber);
        try {
            String field39 = (String) r.getValue(39);
            String field44 = (String) r.getValue(44);
            if (field39.equalsIgnoreCase("00")) {
                balance = field44;
            }
        } catch (ISOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    private static ISOMsg ISOLoanBalanceReq(String customerMSISDN, String loanNumber) {
        ISOMsg r = null;
        IdGenerator randomGen = new IdGenerator();
        String processingCode = "200000";
        String ISOMTI = "0200";
        String transactionRef = "LB" + randomGen.generateId(5);

        try {
            ISOPackager p = new GenericPackager("cfg/packager/iso87ascii.xml");
            ISOMsg m = new ISOMsg();
            m.setPackager(p);
            //m.setHeader("ISO016000055".getBytes());
            m.setMTI(ISOMTI);
            m.set("2", customerMSISDN);
            m.set("3", processingCode);
            m.set("37", transactionRef);
            m.set("42", loanNumber);

            ISOChannel channel = new ASCIIChannel("localhost", 9800, p);

            channel.connect();
            channel.send(m);
            r = channel.receive();
            channel.disconnect();
        } catch (ISOException | IOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    private static String RequestLoanRepayment(String customerMSISDN, String LoanRef, int repaymentAmount, String FOSAAccountNumber) {
        String result = "";
        MathContext mc = MathContext.DECIMAL64;
        BigDecimal repayAmount = new BigDecimal(repaymentAmount, mc);

        // check if fosa account has enough amount//
        BigDecimal FOSAAccountBalance = GetFOSAAccountBalance(customerMSISDN, FOSAAccountNumber);

        int res = FOSAAccountBalance.compareTo(repayAmount);

        if (res < 0) {
            // fosa balance is less than repay amount
            result = result + "Your do not have enough funds in your FOSA account. Please topup then try again.\n";
            result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
        } else {
            
            ISOMsg r = ISORequestLoanRepayment(customerMSISDN, LoanRef, repayAmount);

            try {
                if (r.hasField(39)) {
                    if ("00".equals(r.getValue(39))) {
                        result = result + "Your loan repayment is being processed.\n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                    } else {
                        result = result + "Your loan repayment request failed. Pleae try again later.\n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                    }
                } else {
                    result = result + "Your loan repayment request failed. Pleae try again later.\n";
                    result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                }
            } catch (ISOException ex) {
                Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private static BigDecimal GetFOSAAccountBalance(String customerMSISDN, String FOSAAccountNumber) {
        BigDecimal balance = null;
        MathContext mc = MathContext.DECIMAL64;
        ISOMsg r = ISOGetFOSAAccountBalance(customerMSISDN, FOSAAccountNumber);
        if (r.hasField(39)) {
            try {
                if ("00".equals(r.getValue(39))) {
                    String accountBalance = (String) r.getValue(44);
                    balance = new BigDecimal(accountBalance, mc);
                }
            } catch (ISOException ex) {
                Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return balance;
    }

    private static ISOMsg ISOGetFOSAAccountBalance(String customerMSISDN, String FOSAAccountNumber) {
        String processingCode = "210000";
        String ISOMTI = "0200";

        IdGenerator randomGen = new IdGenerator();
        String transactionRef = "AB" + randomGen.generateId(5);

        ISOPackager p;
        ISOMsg r = null;
        try {
            p = new GenericPackager("cfg/packager/iso87ascii.xml");
            ISOMsg m = new ISOMsg();
            m.setPackager(p);
            //m.setHeader("ISO016000055".getBytes());
            m.setMTI(ISOMTI);
            m.set("2", customerMSISDN);
            m.set("3", processingCode);
            m.set("37", transactionRef);
            m.set("42", FOSAAccountNumber);

            ISOChannel channel = new ASCIIChannel("localhost", 9800, p);

            channel.connect();
            channel.send(m);
            r = channel.receive();
            channel.disconnect();

        } catch (ISOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r;
    }

    private static ISOMsg ISORequestLoanRepayment(String customerMSISDN, String LoanRef, BigDecimal repaymentAmount) {
        String processingCode = "300000";
        String ISOMTI = "0200";
        String formattedAmount = formatAmount(repaymentAmount);

        IdGenerator randomGen = new IdGenerator();
        String transactionRef = "LP" + randomGen.generateId(5);
        
        String debitAccount = "1";
        String creditAccount = "1111111111";

        ISOPackager p;
        ISOMsg r = null;
        try {
            p = new GenericPackager("cfg/packager/iso87ascii.xml");
            ISOMsg m = new ISOMsg();
            m.setPackager(p);
            //m.setHeader("ISO016000055".getBytes());
            m.setMTI(ISOMTI);
            m.set("2", customerMSISDN);
            m.set("3", processingCode);
            m.set("4", formattedAmount);
            m.set("37", transactionRef);
            m.set("42", LoanRef);
            m.set("102", debitAccount);
            m.set("103", creditAccount);

            ISOChannel channel = new ASCIIChannel("localhost", 9800, p);

            channel.connect();
            channel.send(m);
            r = channel.receive();
            channel.disconnect();

        } catch (ISOException ex) {
            logger.info("ISORequestLoanRepayment : Error connecting to ISO Server" +  ex );
        } catch (IOException ex) {
            logger.info("ISORequestLoanRepayment : IO Error" + ex);
        }

        return r;
    }

    private static String formatAmount(BigDecimal loanAmount) {
        String formattedAmount = StringUtils.leftPad(loanAmount.toString(), 12, '0');
        return formattedAmount;
    }

}
