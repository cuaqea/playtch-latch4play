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

import models.entities.Contact;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.contact.*;

public class Contacts extends Controller {
    
    /**
     * Defines a form wrapping the Contact class.
     */ 
    final static Form<Contact> contactForm = form(Contact.class);
  
    /**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(form.render(contactForm));
    }
  
    public static Result edit() {
        Contact existingContact = new Contact(
            "Fake", "Contact", "Fake company",
            new Contact.Information(
                "Personal", "fakecontact@gmail.com", "01.23.45.67.89", "98.76.54.32.10"
            ),
            new Contact.Information(
                "Professional", "fakecontact@company.com", "01.23.45.67.89"
            ),
            new Contact.Information(
                "Previous", "fakecontact@oldcompany.com"
            )
        );
        return ok(form.render(contactForm.fill(existingContact)));
    }

    /**
     * Handle the form submission.
     * @return A Result
     */
    public static Result submit() {
        Form<Contact> filledForm = contactForm.bindFromRequest();
        
        if(filledForm.hasErrors()) {
            return badRequest(form.render(filledForm));
        } else {
            Contact created = filledForm.get();
            return ok(summary.render(created));
        }
    }
  
}