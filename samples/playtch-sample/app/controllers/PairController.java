package controllers;

import actions.LatchPair;
import actions.LatchUnpair;
import models.datasource.UserDataSource;
import models.entities.PairingKey;
import models.entities.User;
import play.Logger;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import utils.factories.LatchIdFromSessionFactory;
import utils.factories.PairingKeyFactory;
import views.html.login.*;
import views.html.latch.*;

/**
 * Controller for pairing actions
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class PairController extends Controller {

    /**
     * Defines a form wrapping the User class.
     */
    final static Form<PairingKey> pairingKeyForm = form(PairingKey.class);
    final static Form<User> loginForm = form(User.class);

    /**
     * Display a blank form.
     * @return The pairing/unpairing page
     */
    public static Result blank() {
        UserDataSource userDataSource = new UserDataSource();

        // Get user from session
        User user = userDataSource.getUser(session("username"));
        if (user != null){
            // Check if user is paired or not
            if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))){
                // If paired, show unpairing view
                return ok(views.html.latch.unpair.render());
            } else {
                // Else, show pairing view
                return ok(views.html.latch.pair.render(pairingKeyForm));
            }
        }

        // If user doesn't exist, show login view
        Form<User> filledForm = loginForm.bindFromRequest();
        return forbidden(views.html.login.login.render(loginForm));
    }

    /**
     * Handle the pair action.
     * User LatchPair notation to perform pairing. It needs the pairingKey from PairingKeyFactory
     * @return
     */
    @LatchPair(pairingKey = PairingKeyFactory.class)
    public static Result pair() {
        // Get the account id from notation LatchPair output argument "status"
        String accountId = (String) Http.Context.current().args.get("status");

        Form<PairingKey> filledForm = pairingKeyForm.bindFromRequest();

        // Check if pairing key is valid
        if(filledForm.field("key").valueOr("").isEmpty()) {
            filledForm.reject("key", "You need to specify a pairing key");
        }

        if(filledForm.hasErrors()) {
            // If there is any error in the form, show again pair view
            return badRequest(views.html.latch.pair.render(filledForm));
        } else {

            if (accountId != null) {
                // If accountId is not null, this is the right accountId that we have to store in the user database
                UserDataSource userDataSource = new UserDataSource();
                User user = userDataSource.getUser(session("username"));

                // Update accountId
                userDataSource.updateLatchAccountId(user.username, accountId);
                Logger.debug("Pairing success!");

                // Show the unpair view
                return ok(views.html.latch.unpair.render());
            } else {
                // Else, pairing was fail
                Logger.debug("<Error> Pairing fail");
                // Show again pair view
                return badRequest(views.html.latch.pair.render(filledForm));
            }
        }
    }

    /**
     * Handle the unpair action.
     * Use LatchUnpair notation to perform unpairing action. If it returns "ok", unpairing was ok, if it returns null,
     * unpairing was wrong
     * @return A Result view
     */
    @LatchUnpair(latchId = LatchIdFromSessionFactory.class)
    public static Result unpair() {
        // Get the unpairing status from notation output argument "status"
        String status = (String) Http.Context.current().args.get("status");

        Form<PairingKey> filledForm = pairingKeyForm.bindFromRequest();

        // Get the user from database
        UserDataSource userDataSource = new UserDataSource();
        User user = userDataSource.getUser(session("username"));

        if (user != null) {
            // Check the unpairing status
            if (status != null) {
                // If everything was ok, we clean the accountId from the user database
                userDataSource.updateLatchAccountId(user.username,"");

                // Show pair view
                return badRequest(views.html.latch.pair.render(filledForm));
            } else {
                // Else, if we had a mistake
                Logger.debug("<Error> Unpair fail");
                // Show again unpair view
                return badRequest(views.html.latch.unpair.render());
            }
        }
        // In other case, show unpair view
        return badRequest(views.html.latch.unpair.render());
    }

}