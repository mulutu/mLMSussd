/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.handlers.AccountHandler;
import com.mpango.ussd.handlers.LoanHandler;
import com.mpango.ussd.menus.Input2;
import com.mpango.ussd.menus.Node2;
import com.mpango.ussd.menus.Selection2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 *
 * @author jmulutu
 */
public class StartClass implements ServletContextListener {

    static Logger logger = Logger.getLogger(StartClass.class.getName());

    private static ServletContext ctx;

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //Context destroyed code here
        System.out.println("xxxxxxxxxxxxxxxxxxx---->>> closes all services from db...");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //Context initialized code here
        System.out.println("xxxxxxxxxxxxxxxxxxx---->>> iniatialize all services from db...");

        getMenus(servletContextEvent);
    }

    private void getMenus(ServletContextEvent servletContextEvent) {
        ctx = servletContextEvent.getServletContext();
        ctx.setAttribute("TESTMENU2", Menu2());
    }

    private void getMenusxxx(ServletContextEvent servletContextEvent) {
        logger.info("--------------------------Starting getMenus-----------------------------");

        try {

            Socket s = new Socket("localhost", 5555);

            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("CONFIG|MENUS");

            InputStream inFromServer = s.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            String data = in.readUTF();
            String delims = "[|]";
            String[] menuResponse = data.split(delims);

            String menuResponseCode = menuResponse[0];
            //String entireMenu = menuResponse[1];

            if (menuResponseCode.equalsIgnoreCase("00")) {
                logger.info(":: login success. getMenus");
                Properties prop = new Properties();

                System.out.println(">>>>> entireMenu <<<<< " + menuResponse);

                for (int index = 1; index < menuResponse.length; index++) {
                    String[] menuItem = menuResponse[index].split("[=]");
                    // add some properties
                    prop.put(menuItem[0], menuItem[1]);

                    System.out.println(menuResponse[index] + " ---menuitemxxx--" + index + "---" + menuItem[1]);
                }
                ctx = servletContextEvent.getServletContext();
                //ctx.setAttribute("TESTMENU", createMenuTree());
                ctx.setAttribute("TESTMENU2", Menu2());
                //ctx.setAttribute("SERVICEMENU", serviceMenu());
                //ctx.setAttribute("LOANSERVICEMENU", loanServiceMenu());
            }
            out.flush();
            out.close();
            //s.shutdownOutput();
            s.close();
        } catch (IOException ex) {
            logger.fatal(ex);            
        }
    }

    public static ServletContext getApplicationCntx() {
        return ctx;
    }

    private Selection2 Menu2() {
        Selection2 menu = new Selection2();
        menu.setNodeID("mainmenu");

        Input2 loanservices = new Input2();
        loanservices.setTitle("Loan services");
        loanservices.setInstruction("Select a loan service:");
        loanservices.setNodeID("loanservicemenu");
        LoanHandler loanHandler = new LoanHandler();
        loanservices.setHandler(loanHandler);

        /*Selection2 loanservices = new Selection2();
         loanservices.setTitle("loan service");
         //loanservices.setInstruction("Select loan service:");
         loanservices.setNodeID("loanservicemenu");
         //LoanHandler loanHandler = new LoanHandler();
         //loanservices.setHandler(loanHandler);        
         loanservices.setChildren(loanservices()); */
        Input2 accountservices = new Input2();
        accountservices.setTitle("Account service");
        accountservices.setInstruction("Select account service:");
        accountservices.setNodeID("accountservice");
        AccountHandler accountHandler = new AccountHandler();
        accountservices.setHandler(accountHandler);

        Node2[] children = new Node2[2];

        children[0] = loanservices;
        children[1] = accountservices;

        menu.setTitle("Select services:");
        menu.setChildren(children);

        return menu;
    }

    private Node2[] loanservices() {
        Node2[] children = new Node2[2];

        /*Selection2 reqloan = new Selection2();
         reqloan.setTitle("Request loan");
         reqloan.setNodeID("reqloan");
         reqloan.setChildren(reqLoanChildren());  */
        Input2 reqloan = new Input2();
        reqloan.setTitle("request loan");
        reqloan.setInstruction("request loan service:");
        reqloan.setNodeID("loanservicemenu");
        LoanHandler reqloanHandler = new LoanHandler();
        reqloan.setHandler(reqloanHandler);

        Input2 loanBal = new Input2();
        loanBal.setTitle("Loan bal");
        loanBal.setNodeID("loanBal");
        loanBal.setInstruction("Loan bal service:");
        LoanHandler loanBalHandler = new LoanHandler();
        loanBal.setHandler(loanBalHandler);

        children[0] = reqloan;
        children[1] = loanBal;

        return children;
    }

    private Node2[] reqLoanChildren() {
        Node2[] children = new Node2[2];

        Input2 onemonth = new Input2();
        onemonth.setTitle("onemonth");
        onemonth.setNodeID("onemonth");
        onemonth.setInstruction("onemonth service:");
        LoanHandler onemonthHandler = new LoanHandler();
        onemonth.setHandler(onemonthHandler);

        Input2 threemonth = new Input2();
        threemonth.setTitle("threemonth");
        threemonth.setNodeID("threemonth");
        threemonth.setInstruction("threemonth service:");
        LoanHandler threemonthHandler = new LoanHandler();
        threemonth.setHandler(threemonthHandler);

        children[0] = onemonth;
        children[1] = threemonth;

        return children;
    }

    private List<MenuItem> serviceMenu() {
        List<MenuItem> servieList = new ArrayList<>();

        MenuItem menuitem1 = new MenuItem();
        menuitem1.setMenuItemID(1);
        menuitem1.setMenuText("Loan services");
        menuitem1.setState("loanservice");
        menuitem1.setService("loanservice");
        servieList.add(menuitem1);

        MenuItem menuitem2 = new MenuItem();
        menuitem2.setMenuItemID(2);
        menuitem2.setMenuText("My Account");
        menuitem2.setState("accountservice");
        menuitem2.setService("accountservice");
        servieList.add(menuitem2);

        return servieList;
    }

    private List<MenuItem> loanServiceMenu() {
        List<MenuItem> servieList = new ArrayList<>();

        MenuItem menuitem1 = new MenuItem();
        menuitem1.setMenuItemID(1);
        menuitem1.setMenuText("Request Loan");
        menuitem1.setState("loanservice");
        menuitem1.setService("loanservice");
        servieList.add(menuitem1);

        MenuItem menuitem2 = new MenuItem();
        menuitem2.setMenuItemID(2);
        menuitem2.setMenuText("Loan Balance");
        menuitem2.setState("accountservice");
        menuitem2.setService("accountservice");
        servieList.add(menuitem2);

        return servieList;
    }

    private Node1 createMenuTree() {
        Node1 treeRootNode = new Node1(null);
        treeRootNode.setId("root");
        treeRootNode.setInstruction("Please select a service:");
        treeRootNode.setTitle("Please select a service: TITLE");
        treeRootNode.setMenuType("S");
        List<MenuItem> RootMenuList = new ArrayList<>();

        MenuItem rootmenuitem1 = new MenuItem();
        rootmenuitem1.setMenuItemID(1);
        rootmenuitem1.setMenuText("Loan services");
        rootmenuitem1.setNextMenuID("NodeChild_1");
        RootMenuList.add(rootmenuitem1);

        MenuItem rootmenuitem2 = new MenuItem();
        rootmenuitem2.setMenuItemID(1);
        rootmenuitem2.setMenuText("My Account");
        RootMenuList.add(rootmenuitem2);

        treeRootNode.setMenuList(RootMenuList);

        // add child to root node 
        Node1 NodeChild_1 = addChild2(treeRootNode, "NodeChild_1");
        NodeChild_1.setTitle("Loan services");
        NodeChild_1.setInstruction("Select the loan service");
        NodeChild_1.setMenuType("S");
        List<MenuItem> NodeChild_1_MenuList = new ArrayList<>();

        MenuItem NodeChild_1_menuitem1 = new MenuItem();
        NodeChild_1_menuitem1.setMenuItemID(1);
        NodeChild_1_menuitem1.setMenuText("Request loan");
        NodeChild_1_menuitem1.setNextMenuID("NodeChild_11");
        NodeChild_1_MenuList.add(NodeChild_1_menuitem1);

        MenuItem NodeChild_1_menuitem2 = new MenuItem();
        NodeChild_1_menuitem2.setMenuItemID(1);
        NodeChild_1_menuitem2.setMenuText("Repay Loan");
        NodeChild_1_MenuList.add(NodeChild_1_menuitem2);

        NodeChild_1.setMenuList(NodeChild_1_MenuList);

        Node1 NodeChild_11 = addChild2(NodeChild_1, "NodeChild_11");
        NodeChild_11.setTitle("Loan Term");
        NodeChild_11.setInstruction("Please select the loan term:");
        NodeChild_11.setMenuType("S");
        NodeChild_11.setRequireInput(true);
        NodeChild_11.setInputName("loanTerm");
        List<MenuItem> NodeChild_11_MenuList = new ArrayList<>();

        MenuItem NodeChild_11_menuitem1 = new MenuItem();
        NodeChild_11_menuitem1.setMenuItemID(1);
        NodeChild_11_menuitem1.setMenuText("One Month");
        NodeChild_11_menuitem1.setNextMenuID("NodeChild_111");
        NodeChild_11_MenuList.add(NodeChild_11_menuitem1);

        MenuItem NodeChild_11_menuitem2 = new MenuItem();
        NodeChild_11_menuitem2.setMenuItemID(1);
        NodeChild_11_menuitem2.setMenuText("Two Months");
        NodeChild_11_menuitem2.setNextMenuID("NodeChild_111");
        NodeChild_11_MenuList.add(NodeChild_11_menuitem2);

        NodeChild_11.setMenuList(NodeChild_11_MenuList);

        Node1 NodeChild_111 = addChild2(NodeChild_11, "NodeChild_111");
        NodeChild_111.setTitle("Loan Amount");
        NodeChild_111.setInstruction("Please enter the Loan amount:");
        NodeChild_111.setMenuType("I");
        NodeChild_111.setRequireInput(true);
        NodeChild_111.setInputName("loanAmount");

        return treeRootNode;

    }

    private Node1 createMenuTree_WORKING() {
        Node1 treeRootNode = new Node1(null);
        treeRootNode.setId("root");
        treeRootNode.setInstruction("Please select a service:");
        treeRootNode.setTitle("Please select a service: TITLE");
        treeRootNode.setMenuType("S");

        // add child to root node 
        Node1 NodeChild_1 = addChild(treeRootNode, "Node-child-1", "Loan services", "Please select loan service:", "S", 1, null);

        Node1 NodeChild_11 = addChild(NodeChild_1, "child-11", "Request loan", "Please select the Loan Term:", "S", 1, null);

        Node1 NodeChild_1111 = addChild(NodeChild_11, "child-1111", "One Month", " 1. One Month Loan", "I", 1, null);
        NodeChild_1111.setRequireInput(true);
        NodeChild_1111.setInputName("loanTerm");
        Node1 NodeChild_11111 = addChild(NodeChild_1111, "child-11111", "Please enter the Loan amount:", "Please enter amount-inst", "I", 1, null);
        NodeChild_11111.setRequireInput(true);
        NodeChild_11111.setInputName("loanAmount");

        Node1 NodeChild_111111 = addChild(NodeChild_11111, "child-111111", "Confirm loan request", "Confirm loan request", "E", 1, "loanrequest");
        NodeChild_111111.setRequireInput(true);
        NodeChild_111111.setInputName("acceptTC");

        Node1 NodeChild_1112 = addChild(NodeChild_11, "child-1112", "Three Month", " 2. Three Month Loan", "E", 1, "loanrequest");
        NodeChild_1112.setRequireInput(true);
        NodeChild_1112.setInputName("loanTerm");
        Node1 NodeChild_11121 = addChild(NodeChild_1112, "child-11121", "Please enter the Loan amount:", "Please enter amount-inst", "I", 1, null);
        NodeChild_11121.setRequireInput(true);
        NodeChild_11121.setInputName("loanAmount");

        Node1 NodeChild_111211 = addChild(NodeChild_11121, "child-111211", "Confirm loan request", "Confirm loan request", "E", 1, "loanrequest");
        NodeChild_111211.setRequireInput(true);
        NodeChild_111211.setInputName("acceptTC");

        Node1 NodeChild_12 = addChild(NodeChild_1, "child-12", "Repay Loan", "Repay Loan", "I", 2, null);
        Node1 NodeChild_13 = addChild(NodeChild_1, "child-13", "Loan Balance", "Loan Balance", "I", 3, null);

        // add child to root node
        Node1 NodeChild_2 = addChild(treeRootNode, "Node-child-2", "My Account", "My Account", "S", 2, null);
        Node1 NodeChild_21 = addChild(NodeChild_2, "child-21", "Account Balance", "Account Balance", "E", 1, null);
        Node1 NodeChild_22 = addChild(NodeChild_2, "child-22", "Account Statement", "Account Statement", "E", 2, null);
        Node1 NodeChild_23 = addChild(NodeChild_2, "child-23", "Account Type", "Account Type", "E", 3, null);

        return treeRootNode;

    }

    private Node1 createMenuTree2() {
        Node1 treeRootNode = new Node1(null);
        treeRootNode.setId("root");
        treeRootNode.setInstruction("Please select a service:");
        treeRootNode.setTitle("Please select a service: TITLE");
        treeRootNode.setMenuType("S");

        // add child to root node 
        Node1 NodeChild_1 = addChild(treeRootNode, "Node-child-1", "Loan services", "Please select loan service:", "S", 1, null);
        Node1 NodeChild_11 = addChild(NodeChild_1, "child-11", "Request loan", "Please select the Loan term", "S", 1, null);

        NodeChild_11.setRequireInput(true);
        NodeChild_11.setInputName("loanTerm");
        Node1 NodeChild_111 = addChild(NodeChild_11, "child-111", "One Month", " 1. 1 Month MOJA XXX", "I", 1, "loanrequest");
        NodeChild_111.setRequireInput(true);
        NodeChild_111.setInputName("loanAmount");
        Node1 NodeChild_1111 = addChild(NodeChild_111, "child-1111", "Enter amount the month TTT", "Please enter amount TTT", "I", 2, "loanrequest");
        NodeChild_1111.setRequireInput(true);
        NodeChild_1111.setInputName("acceptTC");
        Node1 NodeChild_11111 = addChild(NodeChild_1111, "child-11111", "Confirm loan request", "Confirm loan request", "I", 2, "loanrequest");

        Node1 NodeChild_112 = addChild(NodeChild_11, "child-112", "Two months", " 2. 2 Months", "I", 2, "loanrequest");
        NodeChild_112.setRequireInput(true);
        NodeChild_112.setInputName("loanAmount");
        Node1 NodeChild_1112 = addChild(NodeChild_112, "child-1112", "Enter amount 2 months", "Please enter amount", "I", 2, "loanrequest");

        addChild(NodeChild_1, "child-12", "Repay Loan", "", "S", 2, null);
        addChild(NodeChild_1, "child-13", "Loan Balance", "", "S", 3, null);

        // add child to root node
        Node1 NodeChild_2 = addChild(treeRootNode, "Node-child-2", "My Account", "", "S", 2, "myaccount");

        // add child to the child node created above
        Node1 NodeChild_21 = addChild(NodeChild_2, "child-21", "Balance Inquiry", "", "S", 1, null);
        addChild(NodeChild_21, "child-211", "swali tu ", "", "S", 2, null);

        addChild(NodeChild_2, "child-22", "Send Message", "", "S", 2, null);

        /*printTree(treeRootNode, "-");

         System.out.println("----------------------------------------");

         printNode(treeRootNode, "root", "", "", "-");
        
         Node testNode = getNode( treeRootNode, "1");
        
         System.out.println("selected node " + testNode.getTitle()); */
        return treeRootNode;

    }

    private static void printNode(Node1 node, String nodeID, String serviceCode, String input, String appender) {
        if (nodeID.equalsIgnoreCase("root")) {
            System.out.println(node.getId());
            System.out.println(node.getInstruction());
            for (Node1 each : node.getChildren()) {
                System.out.println(each.getMenuID() + ". " + each.getTitle());
            }
        } else {
            for (Node1 each : node.getChildren()) {
                if (each.getId().equals(nodeID)) {
                    System.out.println(each.getId());
                    System.out.println(each.getInstruction());
                    int children_ = each.getChildren().size();
                    if (children_ > 0) {
                        for (Node1 kid : each.getChildren()) {
                            System.out.println(kid.getMenuID() + ". xxxxxxx " + kid.getTitle() + ". xxxxxxx " + kid.getId() + ". xxxxxxx " + kid.getInstruction());
                        }
                    } else {
                        System.out.println(each.getMenuID() + ". " + each.getTitle());
                    }
                } else {
                    printNode(each, nodeID, "", "333", appender);
                }
            }
        }
    }

    private static Node1 addChild2(Node1 parent, String id) {
        Node1 node = new Node1(parent);
        node.setId(id);
        parent.getChildren().add(node);
        return node;
    }

    private static Node1 addChild(Node1 parent, String id, String title, String instruction, String menuType, int menuID, String handler) {
        Node1 node = new Node1(parent);
        node.setId(id);
        node.setMenuID(menuID);
        node.setTitle(title);
        node.setInstruction(instruction);
        parent.getChildren().add(node);

        node.setMenuType(menuType);

        node.setHandler(handler);
        return node;
    }

    private static void printTree(Node1 node, String appender) {
        System.out.println(node.getMenuID() + appender + node.getId() + appender + node.getTitle() + appender + node.getInstruction());
        for (Node1 each : node.getChildren()) {
            printTree(each, appender + appender);
        }
    }
}
