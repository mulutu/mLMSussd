/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpango.ussd;

import com.mpango.ussd.*;

/**
 *
 * @author jmulutu
 */
public class Input1{
    private String inputName;
    private String inputValue;
    
    private Node1 node;

    public Input1() {}

    /**
     * @return the inputName
     */
    public String getInputName() {
        return inputName;
    }

    /**
     * @return the inputValue
     */
    public String getInputValue() {
        return inputValue;
    }

    /**
     * @return the node
     */
    public Node1 getNode() {
        return node;
    }

    /**
     * @param inputName the inputName to set
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    /**
     * @param inputValue the inputValue to set
     */
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Node1 node) {
        this.node = node;
    }

   

    
}
