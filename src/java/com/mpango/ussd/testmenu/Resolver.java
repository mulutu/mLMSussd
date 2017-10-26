/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.testmenu;

/**
 *
 * @author jmulutu
 */
public class Resolver {

    private static Node foundNode = null;

    public static void main(String[] args) {
        Node treeRootNode = new Node(null);
        treeRootNode.setId("root");
        treeRootNode.setInstruction("Please select a service:");

        // add child to root node 
        Node NodeChild_1 = addChild(treeRootNode, "Node-child-1", "Loan services", "Please select loan service:", 'S', 1);

        // add child to the child node created above
        Node NodeChild_11 = addChild(NodeChild_1, "child-11", "Request loan", "Please select the Loan term", 'S', 1);

        Node NodeChild_12 = addChild(NodeChild_11, "child-111", "Select Loan Term", "", 'S', 1);
        Node NodeChild_121 = addChild(NodeChild_12, "child-1111", "1 month", "", 'I', 1);
        Node NodeChild_1212 = addChild(NodeChild_121, "child-11111", "", "Please enter amount in KES:", 'S', 1);
        addChild(NodeChild_1212, "child-111111", "", "Accept TNC: enter 1", 'S', 1);
        Node NodeChild_122 = addChild(NodeChild_12, "child-1112", "2 month", "", 'I', 2);
        addChild(NodeChild_122, "child-11112", "", "Please enter amount in KES:", 'S', 1);
        Node NodeChild_123 = addChild(NodeChild_12, "child-1113", "3 month", "", 'I', 3);
        addChild(NodeChild_123, "child-11113", "", "Please enter amount in KES:", 'S', 1);

        addChild(NodeChild_11, "child-112", "Loan rates", "View loan rates", 'I', 2);

        addChild(NodeChild_1, "child-12", "Repay Loan", "", 'S', 2);
        addChild(NodeChild_1, "child-13", "Loan Balance", "", 'S', 3);

        // add child to root node
        Node NodeChild_2 = addChild(treeRootNode, "Node-child-2", "My Account", "", 'S', 2);

        // add child to the child node created above
        addChild(NodeChild_2, "child-21", "Balance Inquiry", "", 'S', 1);
        addChild(NodeChild_2, "child-22", "Send Message", "", 'S', 2);

        printTree(treeRootNode, "-");

        System.out.println("----------------------------------------");

        printNode(treeRootNode, "child-21", "", "", "-");

        System.out.println("----------------------------------------");

        Node getnode = getNode(treeRootNode, "child-21");

        System.out.println("getnode >>> " + getnode.getInstruction() + getnode.getId());

        Node testNode = getNextNode(treeRootNode, "1");

        System.out.println("selected node " + testNode.getTitle());

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
