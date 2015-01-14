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
 *
 * A default LatchIdFactory to get accountId
 */
public class LatchIdFactory implements ObtainLatchId{

    @Override
    public String getLatchId(Http.Context context) {
        return null;
    }

}
