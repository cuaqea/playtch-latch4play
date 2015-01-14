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

package actions;

import controllers.Latch.LatchController;
import pairingkey.ObtainPairingKey;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;


/**
 * Created by Enrique Ismael Mendoza Robaina on 13/01/15.
 */
public class LatchPairAction extends Action<LatchPair> {

    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable
    {

        // Get the reflexive class pairingKey from input notation param
        Class<? extends ObtainPairingKey> obtainPairingKeyClass = configuration.pairingKey();

        ObtainPairingKey opk;
        String pairingKey = null;

        // Check if programmer send a right pairingKey class
        if (obtainPairingKeyClass != null){
            opk = obtainPairingKeyClass.newInstance();
            pairingKey = opk.getPairingKey(context);
        }

        // Add argument "status" with the response from LatchController.pair()
        // If pairing was success, it returns the accountId (String),
        // if not, it returns null
        context.args.put("status", LatchController.pair(pairingKey));

        // To return control to main program, the call the delegate and pass context as argument
        return delegate.call(context);

    }
}