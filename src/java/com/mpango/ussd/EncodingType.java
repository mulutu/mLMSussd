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
public enum EncodingType {

    TEXT("0"), FLASH("64"), BINARY("245");

    private String code;

    private EncodingType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
