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

import java.util.*;

import javax.validation.*;

import play.data.validation.Constraints.*;

public class Contact {
    
    @Required
    public String firstname;
    
    @Required
    public String lastname;
    
    public String company;
    
    @Valid
    public List<Information> informations;
    
    public Contact() {}
    
    public Contact(String firstname, String lastname, String company, Information... informations) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.informations = new ArrayList<Information>();
        for(Information information: informations) {
            this.informations.add(information);
        }
    }
    
    public static class Information {
        
        @Required
        public String label;
        
        @Email
        public String email;
        
        @Valid
        public List<Phone> phones;
        
        public Information() {}
        
        public Information(String label, String email, String... phones) {
            this.label = label;
            this.email = email;
            this.phones = new ArrayList<Phone>();
            for(String phone: phones) {
                this.phones.add(new Phone(phone));
            }
        }
        
        public static class Phone {
            
            @Required
            @Pattern(value = "[0-9.+]+", message = "A valid phone number is required")
            public String number;
            
            public Phone() {}
                        
            public Phone(String number) {
                this.number = number;
            }
            
        }
        
    }
    
}