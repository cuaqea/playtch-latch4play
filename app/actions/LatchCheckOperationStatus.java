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

import latchid.LatchIdFactory;
import latchid.ObtainLatchId;
import play.mvc.With;

import java.lang.annotation.*;


/**
 * Created by Enrique Ismael Mendoza Robaina on 12/01/15.
 */

@With(LatchCheckOperationStatusAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface LatchCheckOperationStatus {

    /**
     * value indicates a Latch operation id
     */
    public String value() default " ";

    /**
     * latchId is a reflexive class where you have to declare a method that returns a user accountId returned when he
     * or she paired the account with Latch
     */
    public Class<? extends ObtainLatchId> latchId() default LatchIdFactory.class;

}