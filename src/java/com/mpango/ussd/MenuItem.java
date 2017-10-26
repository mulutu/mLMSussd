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
public class MenuItem {
    
    private int menuItemID;
    private String MenuText;
    private String nextMenuID;
    private String state;
    private String service;
    
    public MenuItem(){}

    /**
     * @return the menuItemID
     */
    public int getMenuItemID() {
        return menuItemID;
    }

    /**
     * @return the MenuText
     */
    public String getMenuText() {
        return MenuText;
    }

    /**
     * @return the nextMenuID
     */
    public String getNextMenuID() {
        return nextMenuID;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param menuItemID the menuItemID to set
     */
    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    /**
     * @param MenuText the MenuText to set
     */
    public void setMenuText(String MenuText) {
        this.MenuText = MenuText;
    }

    /**
     * @param nextMenuID the nextMenuID to set
     */
    public void setNextMenuID(String nextMenuID) {
        this.nextMenuID = nextMenuID;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

  
    
}
