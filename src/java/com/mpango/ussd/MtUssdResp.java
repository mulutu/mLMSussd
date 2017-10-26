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
public class MtUssdResp {

    public MtUssdResp() {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return (new StringBuilder()).append("MtUssdResp{").append("requestId='").append(requestId).append('\'').append(", version='").append(version).append('\'').append(", timeStamp='").append(timeStamp).append('\'').append(", statusCode='").append(statusCode).append('\'').append(", statusDetail='").append(statusDetail).append('\'').append('}').toString();
    }

    private String version;
    private String requestId;
    private String timeStamp;
    private String statusCode;
    private String statusDetail;
}
