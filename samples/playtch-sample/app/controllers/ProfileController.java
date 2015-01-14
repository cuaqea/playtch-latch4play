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
import models.entities.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import static play.data.Form.form;

import utils.factories.LatchIdFactory;
import views.html.profile.*;
import views.html.index;

import java.util.Date;

/**
 * Controller for profile
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class ProfileController extends Controller {

    /**
     * Defines a form wrapping the Profile class.
     */
    final static Form<User> profileForm = form(User.class);

    static Config config = ConfigFactory.load("application");

    /**
     * Display a blank form.
     */
    public static Result blank() {
        UserDataSource userDataSource = new UserDataSource();
        User user = userDataSource.getUser(session("username"));
        if (user != null){
            return ok(form.render(profileForm.fill(user)));
        }
        // User doesn't exist, so logout and go to index
        Logger.error("User '"+session("username")+"' doesn't exist");
        /*session().clear();
        return ok(index.render());*/

        return ok(form.render(profileForm.fill(new User()))); // In case you want to show clear profile form page
    }

    public static Result edit() {
        UserDataSource userDataSource = new UserDataSource();
        User user = userDataSource.getUser(session("username"));
        if (user != null){
            return ok(form.render(profileForm.fill(user)));
        }
        return ok(form.render(profileForm.fill(null)));
    }

    /**
     * Handle the form submission.
     * Use LatchCheckOperationStatus notation to check if actual user can perform that action.
     * @return A result view
     */
    @LatchCheckOperationStatus(value = "EDITOPERATIONID", latchId = LatchIdFactory.class)
    public static Result submit() {
        // Get status from the Notation output argument and assign that value to isLatchOn
        Boolean isLatchOn = (Boolean) Http.Context.current().args.get("status");

        Form<User> filledForm = profileForm.bindFromRequest();

        // Check password
        if(filledForm.field("password").valueOr("").isEmpty()) {
            filledForm.reject("password", "Enter a valid password");
        }

        if(filledForm.hasErrors()) {
            // If there is some error, show form view
            return badRequest(form.render(filledForm));
        } else {
            if(isLatchOn){
                // If latch operation status is on
                User created = filledForm.get();

                // Get user from database by username
                // Note: username is the name of a form field
                UserDataSource userDataSource = new UserDataSource();
                User newUser = userDataSource.getUser(created.username);

                // This is a silly check.
                // If logged username and form username are equals, or we can't find the new username in our database,
                // we update the user.
                if (created.username.equals(session("username")) || newUser == null){
                    userDataSource.updateUser(session("username"),created);
                    // Update the username in the session
                    session("username",created.username);
                    return ok(summary.render(created));
                }
                // In other case, we show the error
                filledForm.error("Username '"+created.username+"' is already taken");
                filledForm.reject("username", "Username '"+created.username+"' is already taken");
                return badRequest(form.render(filledForm));
            }
            // If latch operation is locked
            Logger.debug("<Error> Latch is OFF");
            // Set form error message
            filledForm.error("-");
            filledForm.reject("username", "Error while editing your profile");
            // Show form view
            return badRequest(form.render(filledForm));
        }
    }
}
