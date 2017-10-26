/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.testmenu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmulutu
 */
public class Node implements I_Node {

    private String id;
    private int menuID;
    private String title;
    private String instruction;
    private Handler handler;
    private final List<Node> children = new ArrayList<>(); // nodes stemming fom this node
    private final List<String> menuList = new ArrayList<>(); // the list to be displayed to user
    private Input userInput;
    private final Node parent;

    public Node(Node parent) {
        this.parent = parent;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(Input input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
