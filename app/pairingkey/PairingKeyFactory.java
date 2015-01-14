package pairingkey;

import play.mvc.Http;

/**
 * Created by anquegi on 09/01/15.
 * A default PairingKeyFactory to get pairingKey
 */
public class PairingKeyFactory implements ObtainPairingKey{

    @Override
    public String getPairingKey(Http.Context context) {
        return null;
    }

}
