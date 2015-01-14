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

package pairingkey;

import play.mvc.Http;

/**
 * Created by anquegi on 09/01/15.
 * Interface ObtainPairingKey that implements a method getPairingKey
 */
public interface ObtainPairingKey {

    /**
     * This method is used by notation to get the pairingKey that is specified in the HTTP request.
     * Example of implementation:
     * Imagine we have a form where a user write the pairingKey in a inputText element called "key".
     * To get the value for "key" from this form, we use this:
     * context.request().body().asFormUrlEncoded().get("key")[0].toString()
     *
     * So our getPairingKey implementation could be:
     *
     * public String getPairingKey(Http.Context context) {
     *     return context.request().body().asFormUrlEncoded().get("key")[0].toString();
     * }
     *
     * @param context Http.Context is the context of the HTTP request
     * @return String with accountId for a given user in the context param or null if user has not got an accountId
     */
    public String getPairingKey(Http.Context context);

}
