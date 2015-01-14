package utils.factories;

import latchid.ObtainLatchId;
import models.datasource.UserDataSource;
import models.entities.User;
import play.mvc.Http;

/**
 * Factory to get accountId from form
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class LatchIdFactory implements ObtainLatchId {

    /**
     * Method that returns an accountId getting it from the database
     * @param context Http.Context HTTP request that includes the username to get the user form database
     * @return String with accountId if user is paired or null if not
     */
    @Override
    public String getLatchId(Http.Context context) {
        // Get "username" from context
        String userId = context.request().body().asFormUrlEncoded().get("username")[0].toString();
        UserDataSource userDataSource = new UserDataSource();

        // Get full User by username
        User user = userDataSource.getUser(userId);

        if (user != null) {
            // Check if user is paired
            if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))) {
                // If paired, return its accountId
                return user.latchAccountId;
            }
        }
        // If not, returns null
        return null;
    }

}
