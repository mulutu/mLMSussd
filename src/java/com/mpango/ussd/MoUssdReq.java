/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.mpango.ussd.session.Session;

/**
 *
 * @author jmulutu
 */
public class MoUssdReq {   

    public MoUssdReq() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getUssdOperation() {
        return ussdOperation;
    }

    public void setUssdOperation(String ussdOperation) {
        this.ussdOperation = ussdOperation;
    }

    public String getVlrAddress() {
        return vlrAddress;
    }

    public void setVlrAddress(String vlrAddress) {
        this.vlrAddress = vlrAddress;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String toString() {
        return (new StringBuilder()).append("MoUssdReq{").append("applicationId='").append(applicationId).append('\'').append(", version='").append(version).append('\'').append(", sessionId='").append(sessionId).append('\'').append(", ussdOperation='").append(ussdOperation).append('\'').append(", sourceAddress='").append(sourceAddress).append('\'').append(", vlrAddress='").append(vlrAddress).append('\'').append(", message='").append(message).append('\'').append(", encoding='").append(encoding).append('\'').append(", requestId='").append(getRequestId()).append('\'').append('}').toString();
    }

    private String version;
    private String applicationId;
    private String sessionId;
    private String ussdOperation;
    private String sourceAddress;
    private String vlrAddress;
    private String message;
    private String encoding;
    private String requestId;
    private Session session;

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }
}
