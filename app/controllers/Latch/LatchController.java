package controllers.Latch;

import play.Play;
import play.mvc.Controller;
import com.elevenpaths.latch.Latch;
import com.elevenpaths.latch.LatchResponse;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.Logger;


/**
 * Created by Enrique Ismael Mendoza Robaina on 12/01/15.
 */
public class LatchController extends Controller{

    /**
     * Return a Latch object
     * */

    /**
     * This method returns a Latch instance for a given Latch application by appId and secretKey. It takes this values
     * from main project application.conf
     *    - latch.appId: Latch application id
     *    - latch.secretKey: Latch application secret key
     * @return A Latch instance for the appId
     */
     public static Latch getLatch(){
        //System.out.println("LATCHAPPID: " + Play.application().configuration().getString("latch.appId"));
        //System.out.println("LATCHSECRETKEY: "+ Play.application().configuration().getString("latch.secretKey"));
        return new Latch(Play.application().configuration().getString("latch.appId"),
                         Play.application().configuration().getString("latch.secretKey"));
    }

    /**
     * This method check the status for a Latch application with a specific user
     * @param appId A latch application id
     * @param accountId A latched user accountId
     * @return A String with "on" in case user with accountId allows this app or "off" if not
     */
    public static String checkLatchStatus(String appId, String accountId){

        // Check if accountId is set and if is not null
        if (accountId != null && !(accountId.equals("null") || accountId.equals(""))){
            // Get a Latch instance
            Latch latch = LatchController.getLatch();
            if (latch != null) {
                // If it was created, check the status for given accountId
                LatchResponse response = latch.status(accountId);

                if (response.getData() != null) {
                    String status = response.
                            getData().
                            get("operations").
                            getAsJsonObject().
                            get(appId).
                            getAsJsonObject().
                            get("status").getAsString();

                    if (!status.equals("") || status != null || !status.isEmpty()) {
                        // If server response is not null, we return the value
                        return status;
                    }
                } else {
                    // We can decide to block user if Latch server is off: return "off";
                    // But we are going to be allowed
                }
            } else {
                // We can decide to block user if Latch server is off: return "off";
                // But we are going to be allowed
            }
        }
        // If any of the previous cases where rejected, it returns "on" to allow application
        return "on";
    }

    /**
     * This method check the status for a Latch operation with a specific user
     * @param operationId A latch operation id
     * @param accountId A latched user accountId
     * @return A String with "on" in case user with accountId allows this operation or "off" if not
     */
    public static String checkLatchOperationStatus(String operationId, String accountId){

        // Check if accountId is set and if is not null
        if (accountId != null && !(accountId.equals("null") || accountId.equals(""))){
            Latch latch = LatchController.getLatch();
            if (latch != null) {
                LatchResponse response = latch.operationStatus(accountId, operationId);

                if (response.getData() != null) {
                    String status = response.
                            getData().
                            get("operations").
                            getAsJsonObject().
                            get(operationId).
                            getAsJsonObject().
                            get("status").getAsString();

                    if (!status.equals("") || status != null || !status.isEmpty()) {
                        // If server response is not null, we return the value
                        return status;
                    }
                } else {
                    // We can decide to block user if Latch server is off: return "off";
                    // But we are going to be allowed
                }
            } else {
                // We can decide to block user if Latch server is off: return "off";
                // But we are going to be allowed
            }
        }
        // If any of the previous cases where rejected, it returns "on" to allow operation
        return "on";
    }

    /**
     * Handle the pairing action
     * @param pairingKey The pairing key given by user to pair this app with its Latch account
     * @return A String with accountId if successfully paired or null if not
     */
    public static String pair(String pairingKey) {

        // If it the first time you are using this plugin, you have to add a certificate to your java keystore
        // REMEMBER: sudo keytool -import -noprompt -trustcacerts -alias CACertificate -file ca.pem -keystore "/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit

        // Get a Latch instance
        Latch latch = LatchController.getLatch();

        // Call the pair action with the given pairingKey
        LatchResponse response = latch.pair(pairingKey);

        if (response != null && response.getError() == null) {
            // If Latch server has not return an error
            if (response.getData() != null) {
                // And if data is not null, we get the accountId
                String accountId = response.getData().get("accountId").getAsString();
                System.out.println("[playtch] Pairing success!");

                return accountId;
            } else {
                System.out.println("<Error> [playtch] Pairing fail... Latch data is null :(");
                return null;
            }
        } else {
            int errorCode = response.toJSON().get("error").getAsJsonObject().get("code").getAsInt();
            String errorMessage = response.toJSON().get("error").getAsJsonObject().get("message").getAsString();
            System.out.println("<Error> [playtch] Pairing error... (code: "+errorCode+") "+errorMessage +" :(");
        }

        // In case of latch returns an error or doesn't answer to our call, we return null
        return null;
    }

    /**
     * Handle the unpairing action
     * @param accountId String value with the accountId returned for that user in a previous pairing operation
     * @return String "ok if unpairing was ok or null if not"
     */
    public static String unpair(String accountId) {
        LatchResponse response = LatchController.getLatch().unpair(accountId);

        if (response != null && response.getError() == null) {
            System.out.println("[playtch] Unpairing success!");
            return "ok";
        } else {
            System.out.println("<Error> [playtch] Unpairing fail... :(");
            return null;
        }
    }



}
