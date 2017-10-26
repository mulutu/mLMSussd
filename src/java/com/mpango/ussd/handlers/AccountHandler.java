/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.handlers;

import com.mpango.ussd.IdGenerator;
import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.menus.Handler2;
import com.mpango.ussd.menus.Input2;
import com.mpango.ussd.menus.Node2;
import com.mpango.ussd.menus.Selection2;
import com.mpango.ussd.processors.LoanProcessor;
import com.mpango.ussd.session.Session;
import com.mpango.ussd.session.SessionParamName;
import java.io.IOException;
import java.math.BigDecimal;
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
public class AccountHandler implements Handler2 {

    private int state = 0;
    private int loanamount = 0;
    private int loanterm = 0;
    private int confirm = 0;
    private String sessionID = null;
    private String service = "";

    @Override
    public String handler(MoUssdReq moUssdReq) {
        String result = "";
        String userinput = moUssdReq.getMessage();
        Session sessionObject = moUssdReq.getSession();
        String customerMSISDN = (String) sessionObject.get(SessionParamName.CUSTOMER_MSISDN);
        String newSessionID = (String) sessionObject.get(SessionParamName.SESSION_ID);
        Selection2 USSDMenuFinal = acoountservices();

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
        System.out.println("LoanHandler >> state -->" + state + " sessionID >> " + sessionID + " newSessionID >> " + newSessionID);

        if (state == 0) {
            result = result + USSDMenuFinal.render();
            state++;
        } else if (state == 1) {
            int loanServiceID = Integer.parseInt(userinput);
            if (loanServiceID == 1) {
                service = "acctBalFOSA";
                result = result + "Your FOSA account balance is KES. 23,456. Check SMS for details.\n";
                result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                state = 0;
            } else if (loanServiceID == 2) {
                service = "acctSttmFOSA";
                result = result + "Your account statement has been sent via SMS.\n";
                result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                state = 0;
            }
            sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
            moUssdReq.setSession(sessionObject);
        } 
        
        /*else if (state == 2) {
            if (service.equalsIgnoreCase("acctBal")) {
                loanamount = Integer.parseInt(userinput);
                result = result + "Please select the loan term:\n1. One Month\n2. Three months";
                state++;
            } else if (service.equalsIgnoreCase("loanBal")) {
                String loanNumber = userinput;
                result = result + "Your loan " + loanNumber + " has a balance of 00000.";
                result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                state = 0;
            }
        } else if (state == 3) {
            if (service.equalsIgnoreCase("reqLoan")) {
                loanterm = Integer.parseInt(userinput);
                result = result + "Please confirm: Type 1:\n";
                state++;
            }
        } else if (state == 4) {
            if (service.equalsIgnoreCase("reqLoan")) {
                confirm = Integer.parseInt(userinput);
                if (confirm == 1) {
                    BigDecimal loanamount_ = new BigDecimal(loanamount);
                    int LoanTypeID = 1;
                    String response = ISORequest(customerMSISDN, loanamount_, loanterm, LoanTypeID);
                    if (response.equalsIgnoreCase("00")) {
                        result = " Your loan request for " + loanamount + "  for " + loanterm + " is being processed \n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        state = 0;
                    } else if (response.equalsIgnoreCase("16")) {
                        result = "You have an existing loan.\n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        state = 0;
                    } else if (response.equalsIgnoreCase("50")) {
                        result = "You have an existing loan application in the system.\n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        state = 0;
                    } else {
                        result = "An error occured while processing your request.\n";
                        result = result + "0. Main menu\n00: Loans menu\n000. Logout\n";
                        state = 0;
                    }
                    sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
                    moUssdReq.setSession(sessionObject);
                } else {
                    result = result + "You cancelled your loan request.\n0: Main menu\n00: Loans menu\n000: Logout";
                    sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
                    moUssdReq.setSession(sessionObject);
                    state = 0;
                }
            }
        } */
        return result;
    }

    private Selection2 acoountservices() {

        Selection2 loanservicemenu = new Selection2();
        loanservicemenu.setNodeID("mainmenu");

        Node2[] children = new Node2[2];

        Input2 reqloan = new Input2();
        reqloan.setTitle("Account balance");
        reqloan.setInstruction("Account balance service:");
        reqloan.setNodeID("loanservicemenu");
        AccountHandler reqloanHandler = new AccountHandler();
        reqloan.setHandler(reqloanHandler);

        Input2 loanBal = new Input2();
        loanBal.setTitle("Mini statement");
        loanBal.setNodeID("loanBal");
        loanBal.setInstruction("Mini statement service:");
        AccountHandler loanBalHandler = new AccountHandler();
        loanBal.setHandler(loanBalHandler);

        children[0] = reqloan;
        children[1] = loanBal;

        loanservicemenu.setTitle("Select loan services:");
        loanservicemenu.setChildren(children);

        return loanservicemenu;
    }

    private static String ISORequest(String customerMSISDN, BigDecimal loanAmount, int RepaymentPeriod, int LoanTypeID) {
        String response = "01";

        IdGenerator randomGen = new IdGenerator();
        String formattedAmount = formatAmount(loanAmount);
        int transactionTypeID = 1; // Loan application        
        String processingCode = "400000";
        String ISOMTI = "0200";

        if (transactionTypeID == 1) {
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

            } catch (ISOException ex) {
                Logger.getLogger(AccountHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AccountHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    private static String formatAmount(BigDecimal loanAmount) {
        String formattedAmount = StringUtils.leftPad(loanAmount.toString(), 12, '0');
        return formattedAmount;
    }

}
