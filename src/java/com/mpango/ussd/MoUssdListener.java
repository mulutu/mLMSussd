/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.MoUssdReq;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jmulutu
 */
public abstract interface MoUssdListener {

    public abstract void init();

    public abstract MoUssdResp onReceivedUssd(MoUssdReq paramMoUssdReq);
}
