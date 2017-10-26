/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jmulutu
 */
public class Session1 {

    private List<String> menuState2;
    private Map<ParamName, Object> map;
    private long lastAccessTime;

    public Session1() {
        this.menuState2 = new ArrayList<String>();
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * @return the menuState2
     */
    public List<String> getMenuState2() {
        return menuState2;
    }

    /**
     * @return the lastAccessTime
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * @param menuState2 the menuState2 to set
     */
    public void setMenuState2(List<String> menuState2) {
        this.menuState2 = menuState2;
    }

    /**
     * @param lastAccessTime the lastAccessTime to set
     */
    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void put(String state) {
        menuState2.add(state);
    }
    
    public String getCurrentState() {
        String result = "";
        if (menuState2.size() > 0) {
            result = menuState2.get(menuState2.size() - 1);
        }
        return result;
    }
    
    public void clearMenuState() {
        menuState2.clear();
    }

}
