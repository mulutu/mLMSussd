/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.MoUssdListener;
import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.MtUssdReq;
import com.mpango.ussd.MtUssdResp;
import com.mpango.ussd.PropertyLoader;
import com.mpango.ussd.SdpException;
import com.mpango.ussd.StatusCodes;
import com.mpango.ussd.UssdRequestSender;
import com.mpango.ussd.menus.Customer;
import com.mpango.ussd.menus.Selection2;
import com.mpango.ussd.session.Session;
import com.mpango.ussd.session.SessionParamName;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmulutu 94771122336
 */
public class MainMenu implements MoUssdListener {

    static final Logger logger = Logger.getLogger(MainMenu.class.getName());

    //hardcoded values
    private static final String EXIT_SERVICE_CODE = "000";
    private static final String MAIN_MENU_SERVICE_CODE = "0";
    private static final String BACK_SERVICE_CODE = "999";
    private static final String INIT_SERVICE_CODE = "*678#";
    private static final String REQUEST_SENDER_SERVICE = "http://localhost:7000/ussd/send";
    private static final String PROPERTY_KEY_PREFIX = "menu.level.";
    private static final String USSD_OPERATION_MT_CONT = "CON";
    private static final String USSD_OPERATION_MT_FIN = "END";

    //menu state saving for back button
    private String sessionID = "";
    private String CustomerMSISDN = "";
    private static Selection2 USSDMenuFinal = null;
    private Session sessionObject = null;

    @Override
    public void init() {
        logger.info("--------------------------Starting init()-----------------------------");
        USSDMenuFinal = (Selection2) StartClass.getApplicationCntx().getAttribute("TESTMENU2");
    }

    @Override
    public MoUssdResp onReceivedUssd(MoUssdReq moUssdReq) {
        MoUssdResp responseee = new MoUssdResp();
        try {
            responseee = processRequest(moUssdReq);
        } catch (SdpException e) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", e);
        }
        return responseee;
    }

    private MoUssdResp processRequest(MoUssdReq moUssdReq) throws SdpException {
        if (sessionObject == null) {
            sessionID = moUssdReq.getSessionId();
            sessionObject = new Session();
        }
        if (moUssdReq.getMessage().equals(INIT_SERVICE_CODE)) {
            sessionID = moUssdReq.getSessionId();
            sessionObject.put(SessionParamName.SESSION_ID, sessionID);
            String[] srcAddress = moUssdReq.getSourceAddress().split(":");
            CustomerMSISDN = srcAddress[1];

            String customerDetails = getCustomerDetails(CustomerMSISDN);
            
            System.out.println(" customerDetails --> " + customerDetails);

            if (isOurCustomer(customerDetails)) {
                Customer custObject = getCustomerObject(customerDetails);
                sessionObject.put(SessionParamName.CUSTOMER_OBJECT, custObject);
                sessionObject.put(SessionParamName.CURRENT_STATE, "LANDING");
                sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
                sessionObject.put(SessionParamName.CUSTOMER_MSISDN, CustomerMSISDN);
                sessionObject.put(SessionParamName.IS_CUSTOMER, true);
            } else {
                sessionObject.put(SessionParamName.CURRENT_STATE, "REGISTER");
                sessionObject.put(SessionParamName.USSD_OPERATION, "END");
                sessionObject.put(SessionParamName.CUSTOMER_MSISDN, CustomerMSISDN);
                sessionObject.put(SessionParamName.IS_CUSTOMER, false);
            }
            sessionObject.put(SessionParamName.CURRENT_NODE, null);
        }
        if (moUssdReq.getMessage().equals(MAIN_MENU_SERVICE_CODE)) {
            sessionObject.put(SessionParamName.CURRENT_STATE, "SHOWMENULIST");
            sessionObject.put(SessionParamName.USSD_OPERATION, "CON");
            sessionObject.put(SessionParamName.CUSTOMER_MSISDN, CustomerMSISDN);
            sessionObject.put(SessionParamName.IS_CUSTOMER, true);
            sessionObject.put(SessionParamName.CURRENT_NODE, null);
        }
        moUssdReq.setSession(sessionObject);
        final MoUssdResp newMenu = createRequest(moUssdReq);
        return newMenu;
    }

    private MoUssdResp createRequest(MoUssdReq moUssdReq) {
        final MoUssdResp request = new MoUssdResp();
        String menuContent = buildNextMenuContent(moUssdReq);
        request.setPAGE_STRING(menuContent);
        String USSDoperartion = (String) moUssdReq.getSession().get(SessionParamName.USSD_OPERATION);
        request.setMNO_RESPONSE_SESSION_STATE(USSDoperartion); // CON/END
        return request;
    }

    private String buildNextMenuContent(MoUssdReq moUssdReq) {
        String result = "";
        String currentState = (String) moUssdReq.getSession().get(SessionParamName.CURRENT_STATE);
        Session sessionObjectCust = moUssdReq.getSession();
        if (currentState.equalsIgnoreCase("REGISTER")) {
            result = result + "Hi, you are not registered to use this service. Please visit the nearest branch to register. \n";
            sessionObject = null;
        }else if (currentState.equalsIgnoreCase("LANDING")) {
            Customer cust = (Customer) sessionObjectCust.get(SessionParamName.CUSTOMER_OBJECT);
            String custFName = cust.getFirstName();
            result = result + "Hi " + custFName + ", welcome to Demo SACCO. Please enter your PIN to proceed: \n";
            sessionObjectCust.put(SessionParamName.CURRENT_STATE, "LOGIN");
            moUssdReq.setSession(sessionObjectCust);
        } else if (currentState.equalsIgnoreCase("LOGIN")) {
            String PIN = moUssdReq.getMessage();
            Customer cust = (Customer) sessionObjectCust.get(SessionParamName.CUSTOMER_OBJECT);
            String custPIN = cust.getPIN();
            result = result + loginCustomer(custPIN, PIN);
        } else if (currentState.equalsIgnoreCase("PROCESSMENUSELECTION")) {
            result = result + USSDMenuFinal.process(moUssdReq);
        } else if (currentState.equalsIgnoreCase("SHOWMENULIST")) {
            result = result + USSDMenuFinal.render();
            sessionObjectCust.put(SessionParamName.CURRENT_STATE, "PROCESSMENUSELECTION");
        }
        return result;
    }

    private String loginCustomer(String custPIN, String PIN) {
        String result = "";
        if (custPIN.equalsIgnoreCase(PIN)) {
            result = result + USSDMenuFinal.render();
            sessionObject.put(SessionParamName.CURRENT_STATE, "PROCESSMENUSELECTION");
        } else {
            result = result + "Wrong PIN. Please enter correct PIN: \n";
            sessionObject.put(SessionParamName.CURRENT_STATE, "LOGIN");
        }
        return result;
    }

    private Customer getCustomerObject(String customerDetails) {
        Customer customer = new Customer();

        String delims = "[|]";
        String[] customerData = customerDetails.split(delims);

        //String loginResponseDesc = loginResponse[1];
        int customerID = Integer.parseInt(customerData[2]);
        String fname = customerData[3];
        String mname = customerData[4];
        String lname = customerData[5];
        String loanLimit = customerData[6];
        String IDNumber = customerData[7];
        //Date joinDate = Date.loginResponse[8];
        String gender = customerData[9];
        //String email = loginResponse[10];
        String activeLoanRef = customerData[11];
        String PIN = customerData[12];
        String FOSAAccNum = customerData[13];

        customer.setCustomerID(customerID);
        customer.setID_NUMBER(IDNumber);
        customer.setFirstName(fname);
        customer.setMiddleName(mname);
        customer.setLastName(lname);
        //customer.setEmail(email);
        customer.setGender(gender);
        customer.setLoanLimit(loanLimit);
        customer.setActiveLoanRef(activeLoanRef);
        customer.setPIN(PIN);
        customer.setFOSAAccountNumber(FOSAAccNum);

        return customer;
    }

    private boolean isOurCustomer(String customerDetails) {
        boolean result = false;
        String delims = "[|]";
        String[] loginResponse = customerDetails.split(delims);
        String loginResponseCode = loginResponse[0];
        if (loginResponseCode.equalsIgnoreCase("00")) {
            result = true;
        }
        return result;
    }

    private String getCustomerDetails(String MSISDN) {
        String customerDetails = "";
        try {
            Socket s = new Socket("localhost", 5555);
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("ISCUST|" + MSISDN);
            InputStream inFromServer = s.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            customerDetails = in.readUTF();
            out.flush();
            out.close();
            //s.shutdownOutput();
            s.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", ex);
        }
        return customerDetails;
    }

}
