/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmulutu
 */
public class Node1 implements I_Node1 {

    private String id;
    private int menuID;
    private String title;
    private String instruction;
    private String handler;
    private List<Node1> children = new ArrayList<>();
    private List<MenuItem> menuList = new ArrayList<>();
    private Node1 parent;
    private Boolean requireInput = false;
    private String inputName;
    private Input1 input;
    private String inputType = "numbers";
    private String menuType = "S";    

    public Node1(Node1 parent) {
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

    public List<Node1> getChildren() {
        return children;
    }

    public Node1 getParent() {
        return parent;
    }

    @Override
    public void render() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(Input1 input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the handler
     */
    public String getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<Node1> children) {
        this.children = children;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Node1 parent) {
        this.parent = parent;
    }


    /**
     * @return the inputName
     */
    public String getInputName() {
        return inputName;
    }

    /**
     * @return the input
     */
    public Input1 getInput() {
        return input;
    }


    /**
     * @param inputName the inputName to set
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    /**
     * @param input the input to set
     */
    public void setInput(Input1 input) {
        this.input = input;
    }

    /**
     * @return the inputType
     */
    public String getInputType() {
        return inputType;
    }

    /**
     * @param inputType the inputType to set
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * @return the requireInput
     */
    public Boolean getRequireInput() {
        return requireInput;
    }

    /**
     * @param requireInput the requireInput to set
     */
    public void setRequireInput(Boolean requireInput) {
        this.requireInput = requireInput;
    }

    /**
     * @return the menuType
     */
    public String getMenuType() {
        return menuType;
    }

    /**
     * @param menuType the menuType to set
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    /**
     * @return the menuList
     */
    public List<MenuItem> getMenuList() {
        return menuList;
    }

    /**
     * @param menuList the menuList to set
     */
    public void setMenuList(List<MenuItem> menuList) {
        this.menuList = menuList;
    }


}
