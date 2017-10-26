/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.menus;

import com.mpango.ussd.handlers.AccountHandler;
import com.mpango.ussd.handlers.LoanHandler;
import com.mpango.ussd.testmenu.*;

/**
 *
 * @author jmulutu
 */
public class Resolver2 {

    private static Node foundNode = null;

    public static void main(String[] args) {
        Selection2 menus = Menu2();
        
        System.out.println("MAIN MENU: " + menus.getTitle());
        
        Node2[] children = menus.getChildren();
        
        int menuPosition = 1;
        for( int i=0; i < children.length; i++ ){
            Input2 menuitem = (Input2)children[i];            
            System.out.println(menuPosition + ". " + menuitem.getTitle());
            menuPosition++;
        }

    }
    
    private static Selection2 Menu2(){
        Selection2 menu = new Selection2();        
        
        Input2 loanservices = new Input2();
        loanservices.setTitle("loan service");
        loanservices.setInstruction("Select loan service:");
        LoanHandler loanHandler = new LoanHandler();
        loanservices.setHandler(loanHandler);
        
        Input2 accountservices = new Input2();
        accountservices.setTitle("account service");
        accountservices.setInstruction("Select account service:");
        AccountHandler accountHandler = new AccountHandler();
        loanservices.setHandler(accountHandler);
        
        Node2[] children = new Node2[2];
        
        children[0] = loanservices;
        children[1] = accountservices;
        
        menu.setTitle("Select services:");
        menu.setChildren(children);
        
        
        return menu;
    }

    private static Node getNode(Node parentNode, String nodeID) {
        if (nodeID.equalsIgnoreCase("root")) {
            foundNode = parentNode;
        } else {
            for (Node each : parentNode.getChildren()) {
                if (each.getId().equalsIgnoreCase(nodeID)) {
                    foundNode = each;
                } else {
                    Node node_ = getNode(each, nodeID);
                }
            }
        }
        return foundNode;
    }

    private static Node getNextNode(Node currentNodeID, String userInput) {
        Node selectedNode = null;
        int selectedMenu = Integer.parseInt(userInput);
        int count = 1;
        for (Node each : currentNodeID.getChildren()) {
            if (count == selectedMenu) {
                selectedNode = each;
            }
            count++;
        }
        return selectedNode;
    }

    private static void printNode(Node node, String nodeID, String serviceCode, String input, String appender) {
        if (nodeID.equalsIgnoreCase("root")) {
            System.out.println(node.getId());
            System.out.println(node.getInstruction());
            for (Node each : node.getChildren()) {
                System.out.println(each.getMenuID() + ". " + each.getTitle());
            }
        } else {
            for (Node each : node.getChildren()) {
                if (each.getId().equals(nodeID)) {
                    System.out.println(each.getId());
                    System.out.println(each.getInstruction());
                    int children_ = each.getChildren().size();
                    if (children_ > 0) {
                        for (Node kid : each.getChildren()) {
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

    private static Node addChild(Node parent, String id, String title, String instruction, char type, int menuID) {
        Node node = new Node(parent);
        node.setId(id);
        node.setMenuID(menuID);
        node.setTitle(title);
        node.setInstruction(instruction);
        parent.getChildren().add(node);
        return node;
    }

    private static void printTree(Node node, String appender) {
        System.out.println(node.getMenuID() + appender + node.getId() + appender + node.getTitle() + appender + node.getInstruction());
        for (Node each : node.getChildren()) {
            printTree(each, appender + appender);
        }
    }
}
