package utils.factories;

import latchid.ObtainLatchId;
import models.datasource.UserDataSource;
import models.entities.User;
import play.mvc.Http;

/**
 * Factory to get AccountId from session
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class LatchIdFromSessionFactory implements ObtainLatchId {

    /**
     * Method that returns an accountId getting it from the database. It use sessions.
     * @param context Http.Context HTTP request that includes the username to get the user form database
     * @return String with accountId if user is paired or null if not
     */
    @Override
    public String getLatchId(Http.Context context) {
        // Get username from session included inside context
        String userId = context.session().get("username");
        UserDataSource userDataSource = new UserDataSource();

        // Retrieve User information by username
        User user = userDataSource.getUser(userId);

        // Check if user is paired
        if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))){
            // If paired, return accountId
            return user.latchAccountId;
        }
        // Else, returns null
        return null;
    }

}
