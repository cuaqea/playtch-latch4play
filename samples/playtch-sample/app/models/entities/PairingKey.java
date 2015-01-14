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

package models.entities;

import play.data.validation.Constraints.*;

import javax.validation.Valid;

public class PairingKey {

    @Required
    @MinLength(value = 6)
    public String key;

    public PairingKey() {}

    public PairingKey(String key) {
        this.key = key;
    }
    
}