/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.processors;

import com.mpango.ussd.IdGenerator;
import com.mpango.ussd.Input1;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
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
public class LoanProcessor {

    public LoanProcessor() {
    }

    public static String processLoanRequest(List<Input1> inputVariables, String customerMSISDN) {
        String result = "";
        int loanTerm = 1;
        int loanAmount = 0;
        int acceptTC = 0;

        for (int i = 0; i < inputVariables.size(); i++) {
            Input1 input = inputVariables.get(i);

            String inputName = input.getInputName();
            String inputValue = input.getInputValue();

            if (inputName.equalsIgnoreCase("acceptTC")) {
                acceptTC = Integer.parseInt(inputValue);
            }
            if (inputName.equalsIgnoreCase("loanTerm")) {
                loanTerm = Integer.parseInt(inputValue);
            }
            if (inputName.equalsIgnoreCase("loanAmount")) {
                loanAmount = Integer.parseInt(inputValue);
            }
        }

        if (acceptTC == 1) {
            BigDecimal loanAmount_ = new BigDecimal(loanAmount);
            int LoanTypeID = 1;
            String response = ISORequest(customerMSISDN, loanAmount_, loanTerm, LoanTypeID);

            if (response.equalsIgnoreCase("00")) {
                result = " Your loan request for " + loanAmount + "  for " + loanTerm + " is being processed \n";
                result = result + "0. Main menu\n000. Logout\n";
            } else if (response.equalsIgnoreCase("16")) {
                result = "You have an existing loan.\n";
                result = result + "0. Main menu\n000. Logout\n";
            } else {
                result = "An error occured while processing your request.\n";
                result = result + "0. Main menu\n000. Logout\n";
            }
        } else {
            result = "You cancelled your loan request.\n";
            result = result + "0. Main menu\n000. Logout\n";
        }
        return result;
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
                Logger.getLogger(LoanProcessor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LoanProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    private static String formatAmount(BigDecimal loanAmount) {
        String formattedAmount = StringUtils.leftPad(loanAmount.toString(), 12, '0');
        return formattedAmount;
    }

}
