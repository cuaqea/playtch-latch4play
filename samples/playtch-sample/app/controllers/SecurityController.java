/*
 * Copyright (c) 2015. cuaQea SL
 *
 * This file is part of Playtch.
 *
 * Playtch is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * Playtch is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package controllers;

import play.mvc.Controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Controller for security issues
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class SecurityController extends Controller {

    private static SecureRandom random = new SecureRandom();

    /**
     * Method to create secret token
     * @return
     */
    public static String createSecretToken(){
        // This is another way of create a secretToken
        /*UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;*/

        // It returns a String with 32 characteres
        return new BigInteger(130, random).toString(32);
    }
}
