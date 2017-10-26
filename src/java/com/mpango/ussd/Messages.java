/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmulutu
 */
public final class Messages {

    public static final String BUNDLE_NAME = "com.mpango.ussd.ussdmenu"; //$NON-NLS-1$
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
    private static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

    private Messages() {
    }

    /**
     * <p>
     * Resolve a message by a key and argument replacements.
     * </p>
     *
     * @see MessageFormat#format(String, Object...)
     * @param key the message to look up
     * @param arguments optional message arguments
     * @return the resolved message
     *
     */
    public static String getMessage(final String key, final Object... arguments) {
        try {
            if (arguments != null) {
                return MessageFormat.format(resourceBundle.getString(key), arguments);
            }
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ALL, "Message key not found: " + key);
            return "999. Back";
        }
    }
}
