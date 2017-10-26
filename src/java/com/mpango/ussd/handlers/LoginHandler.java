/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpango.ussd.handlers;

import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.menus.Handler2;

/**
 *
 * @author jmulutu
 */
public class LoginHandler implements Handler2 {

    @Override
    public String handler(MoUssdReq moUssdReq) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return " THIS IS a login process >> " ;
    }
    
}
