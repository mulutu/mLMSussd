/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.GenericSender;
import com.mpango.ussd.SdpException;
import com.mpango.ussd.MtUssdReq;
import com.mpango.ussd.MtUssdResp;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jmulutu
 */
public class UssdRequestSender {

    private URL sdpUrl;

    public UssdRequestSender(URL sdpUrl) {
        this.sdpUrl = sdpUrl;
    }

    public MtUssdResp sendUssdRequest(MtUssdReq smsReq, HttpServletResponse resp)
            throws SdpException {
        try {
            GenericSender1<MtUssdReq, MtUssdResp> ussdRequestSender = new GenericSender1(this.sdpUrl);
            return (MtUssdResp) ussdRequestSender.sendRequest(smsReq, resp, MtUssdResp.class);
        } catch (Exception e) {
            throw new SdpException(e);
        }
    }
}
