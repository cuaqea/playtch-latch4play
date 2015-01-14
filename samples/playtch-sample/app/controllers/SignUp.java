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

import models.datasource.UserDataSource;
import models.entities.User;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.signup.*;

/**
 * Controller for signup
 * @author Enrique Ismael Mendoza Robaina (enriquemendozarobaina@gmail.com)
 */
public class SignUp extends Controller {
    
    /**
     * Defines a form wrapping the User class.
     */ 
    final static Form<User> signupForm = form(User.class, User.All.class);
  
    /**
     * Display a blank form.
     * @return A Result with default signupForm page
     */
    public static Result blank() {
        return ok(form.render(signupForm));
    }

    /**
     * Display a form pre-filled with an existing account.
     * @return A signupForm filled
     */
    public static Result edit() {
        User existingUser = new User(
            "fakeuser", "fake@gmail.com", "secret"
        );
        return ok(form.render(signupForm.fill(existingUser)));
    }

    /**
     * Handle the form submission. Check if fields are right or not.
     * @return A Result with a resume of new user information if everything went ok, or the form if not
     */
    public static Result submit() {
        Form<User> filledForm = signupForm.bindFromRequest();
        
        // Check accept conditions
        if(!"true".equals(filledForm.field("accept").value())) {
            filledForm.reject("accept", "You must accept the terms and conditions");
        }
        
        // Check repeated password
        if(!filledForm.field("password").valueOr("").isEmpty()) {
            if(!filledForm.field("password").valueOr("").equals(filledForm.field("repeatPassword").value())) {
                filledForm.reject("repeatPassword", "Password don't match");
            }
        }
        
        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().username.equals("admin") || filledForm.get().username.equals("guest")) {
                filledForm.reject("username", "This username is already taken");
            }
        }
        
        if(filledForm.hasErrors()) {
            return badRequest(form.render(filledForm));
        } else {
            UserDataSource userDataSource = new UserDataSource();
            // Insert new user in the database
            User created = userDataSource.insertIntoUser(filledForm.get());
            return ok(summary.render(created));
        }
    }
  
}