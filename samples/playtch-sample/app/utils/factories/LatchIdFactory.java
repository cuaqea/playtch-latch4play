/*
 * Copyright (c) 2015. cuaQea SL
 *
 * This file is part of Playtch.
 *
 * Playtch is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * Playtch is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

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
