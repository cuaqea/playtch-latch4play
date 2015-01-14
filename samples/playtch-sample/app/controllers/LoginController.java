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

package controllers;

import actions.LatchCheckOperationStatus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.datasource.UserDataSource;
import models.entities.PairingKey;
import models.entities.User;
import play.Logger;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import utils.factories.LatchIdFactory;
import views.html.*;
import views.html.login.*;
import views.html.latch.*;

import java.util.Date;

/**
 * Controller for login
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class LoginController extends Controller {
    
    /**
     * Defines a form wrapping the User class.
     */ 
    final static Form<User> loginForm = form(User.class, User.All.class);
    final static Form<PairingKey> pairingKeyForm = form(PairingKey.class);

    static Config config = ConfigFactory.load("application");

    /**
     * Display a blank form with login.
     * @return
     */
    public static Result blank() {
        return ok(login.render(loginForm));
    }

    /**
     * Handle the form submission for login.
     * User the notation LatchCheckOperationStatus to know if user is allowed to perform this action or not
     * First argument of notation is the latch operation id and the second is the accountId getting with LatchIdFactory
     * @return A Result
     */
    @LatchCheckOperationStatus(value = "LOGINOPERATIONID", latchId = LatchIdFactory.class)
    public static Result submit() {
        Form<User> filledForm = loginForm.bindFromRequest();

        // Get status from the Notation output argument and assign that value to isLatchOn
        Boolean isLatchOn = (Boolean) Http.Context.current().args.get("status");

        // Check if valid password
        if(filledForm.field("password").valueOr("").isEmpty()) {
            filledForm.reject("password", "Enter a valid password");
        }
        
        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().username.equals("admin") || filledForm.get().username.equals("guest")) {
                filledForm.reject("username", "This username is already taken");
            }
        }
        
        if(filledForm.hasErrors()) {
            Logger.debug("Error with form");
            // If there is any error in the form, return a badRequest
            return badRequest(login.render(filledForm));
        } else {
            // If not, get the user from database by username field
            UserDataSource userDataSource = new UserDataSource();
            User user = userDataSource.getUser(filledForm.get().username);
            if (user != null){
                // If everything is ok, we store user data in session
                session("username", user.username);
                session("email", user.email);
                session("latchAccountId", user.latchAccountId);
                // Check user authentication
                if (user.password.equals(filledForm.get().password)){
                    // If user is authenticated and latch status is on
                    if (isLatchOn) {
                        // Check secretToken
                        Date today = new Date();
                        if (user.secretTokenExpiration == null || user.secretTokenExpiration.before(today)){
                            Logger.debug("<Warning> Update secretToken because it was past");
                            userDataSource.updateSecretToken(user.username);
                        }
                        // Show the main page. In that case, PairController.blank() that show the pairing/unpairing view
                        return PairController.blank();
                    }
                }
            }
            Logger.debug("<Error> User can't login");

            // If user doesn't exist or latch is off, we clean session
            session().clear();

            // Show error next to username field
            filledForm.reject("username","¡Error with login credentials!");

            // Return user to the login view
            return unauthorized(login.render(filledForm));
        }
    }

    /**
     * Handle logout
     * @return Index page
     * */
    public static Result logout(){
        session().clear();
        return ok(index.render());
    }
}