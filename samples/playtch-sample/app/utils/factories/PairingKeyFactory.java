package utils.factories;

import pairingkey.ObtainPairingKey;
import play.Logger;
import play.mvc.Http;

/**
 * Factory to get Pairing Key
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class PairingKeyFactory implements ObtainPairingKey {

    /**
     * This method returns a pairing key given in a form with an input called "key"
     * @param context
     * @return
     */
    @Override
    public String getPairingKey(Http.Context context) {
        // Returns the value of inputText "key" sended in the HTTP Request
        return context.request().body().asFormUrlEncoded().get("key")[0].toString();
    }

}
