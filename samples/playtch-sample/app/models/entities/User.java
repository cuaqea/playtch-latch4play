package models.entities;

import javax.validation.*;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import play.data.validation.Constraints.*;

import java.io.Serializable;
import java.util.Date;

public class User{

    public interface All {}

    @Required(groups = {All.class})
    @MinLength(value = 4, groups = {All.class})
    public String username;

    @Email(groups = {All.class})
    public String email;

    @Required(groups = {All.class})
    @MinLength(value = 6, groups = {All.class})
    public String password;

    @Valid
    public Profile profile;

    public String latchAccountId;

    public String secretToken;
    public Date secretTokenExpiration;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, String latchAccountId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.latchAccountId = latchAccountId;
    }

    public User(String username,
                String email,
                String password,
                String latchAccountId,
                String secretToken,
                Date secretTokenExpiration)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.latchAccountId = latchAccountId;
        this.secretToken = secretToken;
        this.secretTokenExpiration = secretTokenExpiration;
    }

    public User(String username, String email, String password, Profile profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public User(String username,
                String email,
                String password,
                Profile profile,
                String latchAccountId,
                String secretToken,
                Date secretTokenExpiration)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.latchAccountId = latchAccountId;
        this.secretToken = secretToken;
        this.secretTokenExpiration = secretTokenExpiration;
    }

    public static class Profile {

        //private static final long serialVersionUID = 1L;

        @Required(groups = {All.class})
        public String country;

        public String address;

        @Min(value = 18, groups = {All.class}) @Max(value = 100, groups = {All.class})
        public Integer age;

        public Profile() {}

        public Profile(String country, String address, Integer age) {
            this.country = country;
            this.address = address;
            this.age = age;
        }

        public String toString(){
            return "{\"country\":\""+this.country+"\",\"address\":\""+this.address+"\",\"age\":\""+this.age+"\"}";
        }
    }

}