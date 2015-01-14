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

import pairingkey.ObtainPairingKey;
import play.Logger;
import play.mvc.Http;

/**
 * Factory to get Pairing Key
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class PairingKeyFactory implements ObtainPairingKey {

    /**
     * This method returns a pairing key given in a form with an input called "key"
     * @param context
     * @return
     */
    @Override
    public String getPairingKey(Http.Context context) {
        // Returns the value of inputText "key" sended in the HTTP Request
        return context.request().body().asFormUrlEncoded().get("key")[0].toString();
    }

}
