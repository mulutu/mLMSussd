/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpango.ussd.session;

/**
 *
 * @author jmulutu
 */
public enum SessionParamName implements ParamName {
    CUSTOMER_OBJECT, SESSION_ID, CURRENT_STATE, CUSTOMER_MSISDN, USSD_OPERATION, IS_CUSTOMER, CURRENT_NODE,CONTEXT_NAME,  TRANSACTION_ID,  USER_INFO_BEAN,  SERVICE_REQUEST_OBJECT,  TRANSACTION_ID_LIST,  ACCESSED_MENU_STACK,  MOBEE_CUSTOMER_OBJECT,  INTERNAL_TRANSACTION_ID,  DECISION_INFO_BEAN,  DO_MOBEE_VAS_PARAM,  ACC_OPEN_CONFORMATION_DETAILS_MAP,  OPTIONAL_PARAMTER_POSITION_IN_LIST,  PARAM_MAP,  OPTIONAL_PARAM_LIST,  SERVICE_ID,  SELF_REG_PARAM_MAP,  SELF_PIN_RESET_MAP;
    private SessionParamName() {}
}
