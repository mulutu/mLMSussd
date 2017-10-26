/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.menus;

import com.mpango.ussd.MoUssdReq;

/**
 *
 * @author jmulutu
 */
public class Input2 implements Node2 {

    private String nodeID;
    private String title;
    private String instruction;
    private Handler2 handler;
    private Node2 parent;

    public Input2() {
    }

    public Input2(Node2 parent) {
        this.parent = parent;
    }

    @Override
    public String render() {
        return getInstruction() + "\n";
    }

    @Override
    public String process(MoUssdReq moUssdReq) {
        System.out.println(" input2 --> " + getTitle() + " >>> user input" );
        //this.handler.handler(input);
        return getHandler().handler(moUssdReq);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * @return the handler
     */
    public Handler2 getHandler() {
        return handler;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(Handler2 handler) {
        this.handler = handler;
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

}
