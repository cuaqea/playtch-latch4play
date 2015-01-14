package actions;

import controllers.Latch.LatchController;
import latchid.ObtainLatchId;
import pairingkey.ObtainPairingKey;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;


/**
 * Created by Enrique Ismael Mendoza Robaina on 13/01/15.
 */
public class LatchUnpairAction extends Action<LatchUnpair> {

    /**
     * This method do de unpair action for a Latch accountId given by a ObtainLatchId.class
     * @param context This is the input context for the HTTP request done by the main program
     * @return Http.Context with the input context and adds a new argument call status with the unpair action status
     * @throws Throwable
     */
    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable
    {

        // Get the reflexive class latchId from input notation param
        Class<? extends ObtainLatchId> obtainPLatchIdClass = configuration.latchId();

        ObtainLatchId oli;
        String latchId = null;

        // Check if programmer send a right latchId class
        if (obtainPLatchIdClass != null){
           oli = obtainPLatchIdClass.newInstance();
            latchId = oli.getLatchId(context);
        }

        // Add argument "status" with the response from LatchController.unpair()
        // If unpairing was success, it returns "ok",
        // if not, it returns null
        context.args.put("status", LatchController.unpair(latchId));

        // To return control to main program, the call the delegate and pass context as argument
        return delegate.call(context);

    }
}