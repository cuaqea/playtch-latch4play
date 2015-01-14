package actions;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controllers.Latch.LatchController;
import latchid.ObtainLatchId;
import play.Logger;
import play.api.Play;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;


/**
 * Created by Enrique Ismael Mendoza Robaina on 12/01/15.
 */
public class LatchCheckOperationStatusAction extends Action<LatchCheckOperationStatus> {

    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable
    {

        // Get the reflexive class latchId
        Class<? extends ObtainLatchId> obtainLatchIdClass = configuration.latchId();

        ObtainLatchId oli;
        String accountId = null;

        // Check if programmer send a right latchId class
        if (obtainLatchIdClass != null){
            // If yes, get an instance of that class
            oli = obtainLatchIdClass.newInstance();
            // Get the accountId returned by extended method getLatchId.
            // We have to send as a param the Http.Context of the calling.
            accountId = oli.getLatchId(context);
        }

        // Check the status for the latch operation.
        // We pass as first argument the id of the latch operation and as second argument, the accountId
        if (LatchController.checkLatchOperationStatus(configuration.value(), accountId).equals("on")){
            // If Latch server returns "on", we set output argument "status" to boolean true
            context.args.put("status", true);
            System.out.println("[playtch] operation '"+configuration.value()+"' status for accountId '"+accountId+"' is 'ON'");
        } else {
            // If Latch server returns "off", it means user can't do that operation so we return "status"=false
            context.args.put("status", false);
            System.out.println("[playtch] operation '"+configuration.value()+"' status for accountId '"+accountId+"' is 'OFF'");
        }

        // To return control to main program, the call the delegate and pass context as argument
        return delegate.call(context);
    }
}