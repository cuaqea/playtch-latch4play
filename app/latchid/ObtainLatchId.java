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

package latchid;

import play.mvc.Http;

/**
 * Created by anquegi on 09/01/15.
 * Interface ObtainLatchId that implements a method getLatchId
 */
public interface ObtainLatchId {

    /**
     * This method is used by notation to get the accountId for a user that is specified in the HTTP request.
     * Example of implementation:
     * You have to define in your user model a field to store the accountId. In that example, imagine we have a form
     * where a user write its username and password.
     * To get the username from this form, we use this:
     * String userId = context.request().body().asFormUrlEncoded().get("username")[0].toString();
     *
     * And imagine we have created a field called latchAccountId in our user model. We have a UserDataSource that
     * implements a method called getUser(String userId) that returns a User by giving a userId
     *
     * So our getLatchId implementation could be:
     *
     * public String getLatchId(Http.Context context) {
     *     String userId = context.request().body().asFormUrlEncoded().get("username")[0].toString();
     *     UserDataSource userDataSource = new UserDataSource();
     *     User user = userDataSource.getUser(userId);
     *
     *     if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))){
     *         return user.latchAccountId;
     *     }
     *     return null;
     * }
     *
     * @param context Http.Context is the context of the HTTP request
     * @return String with accountId for a given user in the context param or null if user has not got an accountId
     */
    public String getLatchId(Http.Context context);

}
