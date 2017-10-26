/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.session;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmulutu
 */
public class Session {

    private Map<ParamName, Object> map;
    private long lastAccessTime;

    public Session() {
        this.map = new HashMap();
        this.lastAccessTime = System.currentTimeMillis();
    }

    public Object get(ParamName key) {
        return this.map.get(key);
    }

    public void put(ParamName key, Object value) {
        if (value == null) {
            this.map.remove(key);
        } else {
            this.map.put(key, value);
        }
    }

    public long getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
