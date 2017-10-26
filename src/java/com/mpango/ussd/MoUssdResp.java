/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

/**
 *
 * @author jmulutu
 */
public class MoUssdResp {

    public MoUssdResp() {
    }

    public String getMNO_RESPONSE_SESSION_STATE() {
        return MNO_RESPONSE_SESSION_STATE;
    }

    public void setMNO_RESPONSE_SESSION_STATE(String MNO_RESPONSE_SESSION_STATE) {
        this.MNO_RESPONSE_SESSION_STATE = MNO_RESPONSE_SESSION_STATE;
    }

    public String getPAGE_STRING() {
        return PAGE_STRING;
    }

    public void setPAGE_STRING(String PAGE_STRING) {
        this.PAGE_STRING = PAGE_STRING;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String toString() {
        return (new StringBuilder()).append("MoUssdResp{").append("statusCode='").append(statusCode).append('\'').append(", statusDetail='").append(statusDetail).append('\'').append('}').toString();
    }

    private String statusCode;
    private String statusDetail;

    private String PAGE_STRING;
    private String MNO_RESPONSE_SESSION_STATE;
}
