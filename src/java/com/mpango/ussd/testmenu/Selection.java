/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpango.ussd.testmenu;
import com.mpango.ussd.testmenu.Node;

/**
 *
 * @author jmulutu
 */
class Selection extends Node {
    Node[] children;
    String title;

    public Selection(Node parent) {
        super(parent);
    }
    
    //public Selection(String title, Node[] children){}
    
    public String renderr() {
         // create a ussd menu here appending title to 
         // menu items based on this.children
        System.out.println("render sdf dshf shdj fhs dkj");
        return "render vvvv";
    }
    public String processs(String input) {
        // get input and select from children node
        // call its render method
        System.out.println("process sdf dshf shdj fhs dkj");
        return renderr();
    }
}
