package latchid;

import play.mvc.Http;

/**
 * Created by anquegi on 09/01/15.
 *
 * A default LatchIdFactory to get accountId
 */
public class LatchIdFactory implements ObtainLatchId{

    @Override
    public String getLatchId(Http.Context context) {
        return null;
    }

}
