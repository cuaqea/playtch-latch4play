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
