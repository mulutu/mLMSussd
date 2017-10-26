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
public enum OperationType {

    MT_INIT("mt-init"), MT_CONT("mt-cont"), MT_FIN("mt-fin"), MO_INIT("mo-init"), MO_CONT("mo-cont");

    private String name;

    private OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
