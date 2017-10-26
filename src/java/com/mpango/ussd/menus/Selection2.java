/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.menus;

import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.session.Session;
import com.mpango.ussd.session.SessionParamName;

/**
 *
 * @author jmulutu
 */
public class Selection2 implements Node2 {

    private String nodeID;
    private Node2 parent;
    private Node2[] children;
    private String title;
    private String selectedNodeID;
    //private Input2 currentnode;

    public Selection2() {
    }

    public Selection2(Node2 parent) {
        this.parent = parent;
    }

    @Override
    public String render() {
        String menu = "";
        // create a ussd menu here appending title to 
        // menu items based on this.children
        menu = menu + getTitle() + "\n";

        Node2[] children = this.getChildren();

        int menuPosition = 1;
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Input2) {
                Input2 menuitem = (Input2) children[i];
                menu = menu + menuPosition + ". " + menuitem.getTitle() + "\n";
            } else if (children[i] instanceof Selection2) {
                Selection2 menuitem = (Selection2) children[i];
                menu = menu + menuPosition + ". " + menuitem.getTitle() + "\n";
            }
            menuPosition++;
        }

        return menu;
    }

    @Override
    public String process(MoUssdReq moUssdReq) {
        // get input and select from children node
        // call its render method
        String output = "";
        Session sessionObject = moUssdReq.getSession();
        String currentState = (String) moUssdReq.getSession().get(SessionParamName.CURRENT_STATE);
        Input2 currentnode = (Input2) sessionObject.get(SessionParamName.CURRENT_NODE);
        String input = moUssdReq.getMessage();

        if (currentnode == null) {
            int selectedMenu = Integer.parseInt(input);
            Node2[] children = this.getChildren();
            for (int i = 0; i < children.length; i++) {
                int menuPosition = i + 1;
                if (selectedMenu == menuPosition) {
                    Input2 menuitem = (Input2) children[i];
                    output = output + menuitem.process(moUssdReq);
                    sessionObject.put(SessionParamName.CURRENT_NODE, menuitem);
                    moUssdReq.setSession(sessionObject);
                    System.out.println(" >> currentnode >> is null >> node title >> " + menuitem.getTitle());
                }
            }
        } else {
            output = output + currentnode.process(moUssdReq);
            System.out.println(" >> currentnode >> is not null >> node title >> " + currentnode.getTitle());
        }
        return output;
    }

    /**
     * @return the children
     */
    public Node2[] getChildren() {
        return children;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public Node2 getParent() {
        return parent;
    }

    public void setParent(Node2 parent) {
        this.parent = parent;
    }

    /**
     * @return the nodeID
     */
    public String getNodeID() {
        return nodeID;
    }

    /**
     * @param nodeID the nodeID to set
     */
    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * @return the selectedNodeID
     */
    public String getSelectedNodeID() {
        return selectedNodeID;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Node2[] children) {
        this.children = children;
    }

    /**
     * @param selectedNodeID the selectedNodeID to set
     */
    public void setSelectedNodeID(String selectedNodeID) {
        this.selectedNodeID = selectedNodeID;
    }

    /**
     * @param children the children to set
     */
    /*public void setChildren(Node2[] children) {
     this.setChildren(children);
     }*/
}
