/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.processors.LoanProcessor;
import com.google.gson.Gson;
import com.mpango.ussd.SdpException;
import com.mpango.ussd.StatusCodes;
import com.mpango.ussd.MoUssdListener;
import com.mpango.ussd.UssdRequestSender;
import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.MtUssdReq;
import com.mpango.ussd.MtUssdResp;
import com.mpango.ussd.PropertyLoader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jmulutu 94771122336
 */
public class MainMenu_WORKING implements MoUssdListener {

    static final Logger logger = Logger.getLogger(MainMenu_WORKING.class.getName());

    //hardcoded values
    private static final String EXIT_SERVICE_CODE = "000";
    private static final String BACK_SERVICE_CODE = "999";
    private static final String INIT_SERVICE_CODE = "*678#";
    private static final String REQUEST_SENDER_SERVICE = "http://localhost:7000/ussd/send";
    private static final String PROPERTY_KEY_PREFIX = "menu.level.";
    private static final String USSD_OPERATION_MT_CONT = "CON";
    private static final String USSD_OPERATION_MT_FIN = "END";

    //menu state saving for back button
    private List<Integer> menuState = new ArrayList<Integer>();
    private List<String> menuState2 = new ArrayList<String>();
    private List<Node1> menuState3 = new ArrayList<Node1>();
    private String sessionID = "";
    private String CustomerMSISDN = "";
    private Properties USSDMenu = null;
    private List<MenuItem> USSDMenu2 = null;
    private boolean isRegisteredCustomer = false;
    private int serviceCode = 0;
    private String currentState = "landing";
    private Node1 currentStateNode;
    private static Node1 foundNode = null;
    private static String menu = null;
    private static Node1 menuTree = null;
    private String userInput = "";
    private static String USSDOperation = "CON";
    //private List<UssdSession> menuStateSession = new ArrayList<UssdSession>();

    private List<Input1> inputVariables = new ArrayList<Input1>();

    //service to send the request
    private UssdRequestSender ussdMtSender;

    @Override
    public void init() {
        try {
            ussdMtSender = new UssdRequestSender(new URL(REQUEST_SENDER_SERVICE));
            //USSDMenu = (Properties) StartClass.getApplicationCntx().getAttribute("TESTMENU");
            menuTree = (Node1) StartClass.getApplicationCntx().getAttribute("TESTMENU");
            //clearMenuState();
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", e);
        }
    }

    /**
     * Receive requests
     *
     * @param moUssdReq
     */
    @Override
    public MoUssdResp onReceivedUssd(MoUssdReq moUssdReq) {
        System.out.println("-----onReceivedUssd >>>> processRequest --------- ");
        MoUssdResp responseee = new MoUssdResp();
        try {
            responseee = processRequest(moUssdReq);
        } catch (SdpException e) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", e);
        }
        return responseee;
    }

    /**
     * Build the response based on the requested service code
     *
     * @param moUssdReq
     */
    private MoUssdResp processRequest(MoUssdReq moUssdReq) throws SdpException {

        if (menuState2.size() > 0) {
            System.out.println(" CURRENT STATE >>>>> " + menuState2.get(menuState2.size() - 1));
        }

        for (int i = 0; i < menuState2.size(); i++) {
            String state = menuState2.get(i);
            System.out.println("State: >>> " + state);
        }


        /*//exit request - session destroy
         if (moUssdReq.getMessage().equals(EXIT_SERVICE_CODE)) {
         terminateSession(moUssdReq);
         return;//completed work and return
         }

         //back button handling
         if (moUssdReq.getMessage().equals(BACK_SERVICE_CODE)) {
         backButtonHandle(moUssdReq);
         return;//completed work and return
         } */
        //get current service code
        if (moUssdReq.getMessage().equals(INIT_SERVICE_CODE)) {
            sessionID = moUssdReq.getSessionId();
            String[] srcAddress = moUssdReq.getSourceAddress().split(":");
            CustomerMSISDN = srcAddress[1];
            if (isCustomer(CustomerMSISDN)) {
                isRegisteredCustomer = true;
                currentState = "landing";
                USSDOperation = "CON";
            }
            clearMenuState();
        } else {
            String[] srcAddress = moUssdReq.getSourceAddress().split(":");
            String MSISDN = srcAddress[1];
            String currentSessionID = moUssdReq.getSessionId();
            userInput = moUssdReq.getMessage();

            if (isRegisteredCustomer && sessionID.equals(currentSessionID) && MSISDN.equals(CustomerMSISDN)) {
                if (currentState.equalsIgnoreCase("landing")) {
                    String PIN = moUssdReq.getMessage();
                    if (!loginCustomer(MSISDN, PIN)) {
                        currentState = "wrongPIN";
                    } else {
                        currentState = "root";
                    }
                } else {
                    Node1 currentNode = getNode(menuTree, currentState);
                    String menuType = currentNode.getMenuType();
                    Node1 nextNode = null;
                    
                    if (menuType.equalsIgnoreCase("S")) {
                        System.out.println("currentNode >>>" + currentNode.getTitle() + " >>> MENUTYPE S ----->>>>>>>>>>>>>>>>>>>>>>>>> ");
                        nextNode = getNextNode(currentNode, userInput);
                    } else {
                        System.out.println("currentNode >>>" + currentNode.getTitle() + " >>> MENUTYPE OTHER ----->>>>>>>>>>>>>>>>>>>>>>>>> ");
                        nextNode = getNextNode(currentNode, null);
                    }

                    if (nextNode != null) {
                        currentState = nextNode.getId();
                        System.out.println(" NEXT NODE IS NOT NULL >>>>>>>>> " + currentState);

                        if (nextNode.getRequireInput()) {
                            String inputName = nextNode.getInputName();
                            String inputValue = userInput;

                            Input1 currentInput = new Input1();
                            currentInput.setInputName(inputName);
                            currentInput.setInputValue(inputValue);
                            currentInput.setNode(nextNode);

                            System.out.println(" getRequireInput --> nodeID: " + nextNode.getId() + "  INPUTNAME-->" + nextNode.getInputName() + " input value: " + inputValue);

                            inputVariables.add(currentInput);
                        }

                    } else {
                        System.out.println(" NEXT NODE IS  NULL >>>>>>>>> ");
                        // no next state; so process input and generate menu on teh fly
                        currentState = "processInput";
                        System.out.println(" >>>>>>>>>>>>>>>>>-------no next state---------->>>>>>>>>>>>>>>>>>>>>>>>> ");
                    }
                }
            } else {
                // if not registered redirect to register page
                currentState = "landing";
                clearMenuState();
            }
        }

        //System.out.println("-->>buildNextMenuContent2(menuTree, currentState) ...>>> " + buildNextMenuContent2(menuTree, currentState));
        //create request to display user
        final MoUssdResp newMenu = createRequest(moUssdReq, buildNextMenuContent2(menuTree, currentState, userInput));
        //final MoUssdResp newMenu = createRequest(moUssdReq, "cccchj dfhjdf gjkdfhkj gjdfg dfhgd", USSD_OPERATION_MT_CONT);
        //sendRequest(request, resp);
        //record menu state

        //menuState3.add(currentStateNode);
        menuState2.add(currentState);
        //menuState.add(currentState);
        return newMenu;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private String processUserInput(Node1 node, String userInput) {
        String result = " processUserInput ..... ";
        String inputType = node.getInputType();

        System.out.println("processUserInput ---> nodeID ->" + node.getTitle());

        if (inputType.equalsIgnoreCase("numbers")) {
            if (isNumeric(userInput)) {
                int input_ = Integer.parseInt(userInput);
                String handler = node.getHandler();
                if (handler.equalsIgnoreCase("loanrequest")) {

                    /*Input1 loanAmnt = new Input1();
                     loanAmnt.setInputName(node.getInputName());
                     loanAmnt.setInputValue(userInput);
                     loanAmnt.setNode(node);

                     inputVariables.add(loanAmnt); */
                    result = LoanProcessor.processLoanRequest(inputVariables, CustomerMSISDN);

                    USSDOperation = "END";
                }
            } else {
                result = " please enter a valid number";
                USSDOperation = "CON";
            }
        } else {
            String input_ = userInput;
        }
        return result;
    }

    private String buildNextMenuContent2(Node1 parentNode, String nodeID, String userInput) {
        System.out.println("buildNextMenuContent2 ---> SERCHING FOR next node  -->" + nodeID + " ");
        String result = "";
        Node1 nextNode = getNode(parentNode, nodeID);

        if (nodeID.equalsIgnoreCase("landing")) {
            result = "Welcome. Please enter PIN: \n";
        } else if (nodeID.equalsIgnoreCase("processInput")) {
            result = processUserInput(nextNode, userInput);
        } else {
            int menuNumber = 1;
            int children = nextNode.getChildren().size();
            if (children > 0) {
                String menuType = nextNode.getMenuType();
                if (menuType.equalsIgnoreCase("S")) {
                    result = nextNode.getInstruction() + "\n";
                }
                for (Node1 each : nextNode.getChildren()) {
                    if (menuType.equalsIgnoreCase("S")) {
                        result = result + menuNumber + " " + each.getTitle() + "\n";
                    } else {
                        result = result + each.getInstruction() + "\n";
                    }
                    menuNumber++;
                }
            } else {
                /*//result = nodeToGet.getInstruction();
                 result = nextNode.getInstruction() + " ||||| no menu available";
                 if (nextNode.getMenuType().equalsIgnoreCase("E")) {
                 //result = processUserInput(nextNode, userInput);
                 USSDOperation = "END";
                 }*/

                result = buildNextMenuContent2(parentNode, "processInput", userInput);
            }
        }
        System.out.println("buildNextMenuContent2 ---> building new menu -->" + nodeID + " >> NEW MENU >> " + result);
        return result;
    }

    private Node1 getNextNode(Node1 currentNode, String userInput) {
        System.out.println(">>>>> getNextNode >>>> current node" + currentNode.getId() + " >>>> userInput >>> " + userInput);
        Node1 selectedNode = null;

        int selectedMenu = 1;
        int children = currentNode.getChildren().size();
        int count = 1;
        if (children > 0) {
            if (userInput != null) {
                selectedMenu = Integer.parseInt(userInput);
            }
            for (Node1 each : currentNode.getChildren()) {
                if (count == selectedMenu) {
                    selectedNode = each;
                    System.out.println(">>>>> getNextNode >>>> NEXT node" + currentNode.getId() + " >>>> userInput >>> " + userInput + " >>> found >> " + each.getId());
                }
                count++;
            }
        } else {
            System.out.println(">>>>> getNextNode >>>> current node" + currentNode.getId() + " >>>> userInput >>> " + userInput + " ZERO CHILDREN");
        }

        return selectedNode;
    }

    private static Node1 getNode(Node1 parentNode, String nodeID) {
        if (nodeID.equalsIgnoreCase("root")) {
            foundNode = parentNode;
        } else {
            for (Node1 each : parentNode.getChildren()) {
                if (each.getId().equalsIgnoreCase(nodeID)) {
                    foundNode = each;
                } else {
                    Node1 node_ = getNode(each, nodeID);
                }
            }
        }
        return foundNode;
    }

    /**
     * Build next menu based on the service code
     *
     * @param selection
     * @return menuContent
     */
    private String buildNextMenuContent(int selection) {
        String menuContent = "";

        //build menu contents
        //menuContent = getText(selection);
        /*System.out.println("buildNextMenuContent ----- >>> CALL >>>> getText >>> SELECTION >>>> " + selection);

        if (getText(selection) != null) {
            menuContent = getText(selection);
        } else {
            menuContent = "compute menus on the fly";
        } */
        return menuContent;
    }

    /**
     * Build request object
     *
     * @param moUssdReq - Receive request object
     * @param menuContent - menu to display next
     * @param ussdOperation - operation
     * @return MtUssdReq - filled request object
     */
    private MoUssdResp createRequest(MoUssdReq moUssdReq, String menuContent) {
        final MoUssdResp request = new MoUssdResp();
        //request.setApplicationId(moUssdReq.getApplicationId());
        //request.setEncoding(moUssdReq.getEncoding());
        request.setPAGE_STRING(menuContent);
        request.setMNO_RESPONSE_SESSION_STATE(USSDOperation); // CON/END
        //request.setPassword("password");
        //request.setSessionId(moUssdReq.getSessionId());
        //request.setUssdOperation(ussdOperation);
        //request.setVersion(moUssdReq.getVersion());
        //request.setDestinationAddress(moUssdReq.getSourceAddress());
        return request;
    }

    /**
     * load a property from ussdmenu.properties
     *
     * @param key
     * @return value
     */
    private String getText(int key) {
        String menuText = null;

        System.out.println(" >>>> getText  :: KEY >> " + key);

       /* for (int i = 0; i < USSDMenu2.size(); i++) {
            MenuItem menuItem = USSDMenu2.get(i);
            int menuID = menuItem.getMenuID();

            if (menuID == key) {
                menuText = menuItem.getMenuText();
                System.out.println(" >>>> Fetch the menu item ---->> FOUND >> " + menuID + " :: KEY " + key);
            } else {
                //menuText = "error with menu item. ";
                System.out.println(" >>>> Fetch the menu item ---->> ERROR >> " + menuID + " :: KEY " + key);
            }

        } */

        return menuText;
    }

    /**
     * Request sender
     *
     * @param request
     * @return MtUssdResp
     */
    private MtUssdResp sendRequest(MtUssdReq request, HttpServletResponse resp) throws SdpException {
        //sending request to service
        MtUssdResp response = null;

        System.out.println(request.getMessage() + " :::: jdfhgkjdfg dfkjhgdf  ");
        try {
            response = ussdMtSender.sendUssdRequest(request, resp);
        } catch (SdpException e) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", e);
            throw e;
        }

        //response status
        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        if (StatusCodes.SuccessK.equals(statusCode)) {
            logger.info("MT USSD message successfully sent");
        } else {
            logger.info("MT USSD message sending failed with status code ["
                    + statusCode + "] " + statusDetails);
        }
        return response;
    }

    /**
     * Clear history list
     */
    private void clearMenuState() {
        logger.info("clear history List");
        menuState.clear();
    }

    /**
     * Terminate session
     *
     * @param moUssdReq
     * @throws SdpException
     */
    private void terminateSession(MoUssdReq moUssdReq) throws SdpException {
        //final MtUssdReq request = createRequest(moUssdReq, "", USSD_OPERATION_MT_FIN);
        //sendRequest(request);
    }

    /**
     * Handlling back button with menu state
     *
     * @param moUssdReq
     * @throws SdpException
     */
    private void backButtonHandle(MoUssdReq moUssdReq) throws SdpException {
        String lastMenuVisited = "0";

        //remove last menu when back
        if (menuState2.size() > 0) {
            menuState2.remove(menuState2.size() - 1);
            lastMenuVisited = menuState2.get(menuState2.size() - 1);
        }

        //create request and send
        //final MtUssdReq request = createRequest(moUssdReq, buildBackMenuContent(lastMenuVisited), USSD_OPERATION_MT_CONT);
        //sendRequest(request);
        //clear menu status
        if (lastMenuVisited.equals("0")) {
            clearMenuState();
            //add 0 to menu state ,finally its in main menu
            menuState2.add("0");
        }

    }

    /**
     * Create service code to navigate through menu and for property loading
     *
     * @param moUssdReq
     * @return serviceCode
     */
    //private byte getServiceCode(MoUssdReq moUssdReq) {
    private String getServiceCode(MoUssdReq moUssdReq) {
        //byte serviceCode = 0;
        String serviceCode2 = "0";
        serviceCode2 = moUssdReq.getMessage();

        /*try {
         serviceCode = Byte.parseByte(moUssdReq.getMessage());            
         } catch (NumberFormatException e) {
         return serviceCode;
         } */
        //create service codes for child menus based on the main menu codes
        /*if (menuState.size() > 0 && menuState.get(menuState.size() - 1) != 0) {
         String generatedChildServiceCode = "" + menuState.get(menuState.size() - 1) + serviceCode;
         serviceCode = Byte.parseByte(generatedChildServiceCode);
         }*/
        //create service codes for child menus based on the main menu codes
        if (menuState2.size() > 0 && menuState2.get(menuState2.size() - 1) != "0") {
            String generatedChildServiceCode = "" + menuState2.get(menuState2.size() - 1) + serviceCode2;
            serviceCode2 = generatedChildServiceCode;
        }

        return serviceCode2;
    }

    /**
     * Build back menu based on the service code
     *
     * @param selection
     * @return menuContent
     */
    private String buildBackMenuContent(String selection) {
        String menuContent = "";
        try {
            //build menu contents
            // menuContent = getText(selection);
        } catch (MissingResourceException e) {
            //back to main menu
            // menuContent = getText("0");
        }
        return menuContent;
    }

    private boolean loginCustomer(String MSISDN, String PIN) {
        boolean loginSuccess = false;
        logger.info("--------------------------Starting loginCustomer-----------------------------");
        try {
            Socket s = new Socket("localhost", 5555);

            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("ISCUST|" + MSISDN);

            InputStream inFromServer = s.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            String data = in.readUTF();
            String delims = "[|]";
            String[] loginResponse = data.split(delims);

            String loginResponseCode = loginResponse[0];
            //String entireMenu = menuResponse[1];

            if (loginResponseCode.equalsIgnoreCase("00")) {
                loginSuccess = true;
                logger.info("---- loginCustomer = true ---------");
            }

            out.flush();
            out.close();
            //s.shutdownOutput();
            s.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "---- ERROR ---------{0}", ex);
        }
        return loginSuccess;
    }

    private boolean isCustomer(String MSISDN) {
        boolean isCust = false;
        logger.info("--------------------------Starting isCust-----------------------------");
        try {
            Socket s = new Socket("localhost", 5555);

            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("ISCUST|" + MSISDN);

            InputStream inFromServer = s.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            String data = in.readUTF();
            String delims = "[|]";
            String[] loginResponse = data.split(delims);

            String loginResponseCode = loginResponse[0];
            //String entireMenu = menuResponse[1];

            if (loginResponseCode.equalsIgnoreCase("00")) {
                isCust = true;
                logger.info("---- isCust = true ---------");
            }
            out.flush();
            out.close();
            //s.shutdownOutput();
            s.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unexpected error occurred{0}", ex);
        }
        return isCust;
    }

}
