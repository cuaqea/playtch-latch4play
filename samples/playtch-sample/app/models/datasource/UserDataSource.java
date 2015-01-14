package models.datasource;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controllers.SecurityController;
import models.entities.*;
import play.Logger;
import play.api.libs.json.JsPath;
import play.libs.Json;
import play.mvc.Http;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import latchid.ObtainLatchId;

/**
 * Created by Nerea on 15/10/2014.
 */
public class UserDataSource {

    public static MongoClient mongoClient;
    public static DB db;
    static Config config = ConfigFactory.load("db");
    static Config configSecurity = ConfigFactory.load("application");

    /**
     * This method returns a MongoDB collection
     * @return A DBCollection specified by db.conf with mongo.host, mongo.port. mongo.database and mongo.usersCollection
     */
    public static DBCollection connectDB() {

        try {
            // Creates a new MongoClient using settings mongo.host and mongo.port specified inside db.conf
            mongoClient = new MongoClient( config.getString("mongo.host") , config.getInt("mongo.port"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // Select the database mongo.database
        DB db = mongoClient.getDB(config.getString("mongo.database"));

        // Returns the collection mongo.usersCollection
        DBCollection coll = db.getCollection(config.getString("mongo.usersCollection"));
        return coll;
    }

    /**
     * This method insert a new User in the collection
     * @param user User to be inserted
     * @return A new user
     */
    public static User insertIntoUser(User user) {

        // Creates the secretTokenExpiration date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // Get the period of life from application.conf (security.secretTokenExpirationLimit)
        calendar.add(Calendar.DATE, configSecurity.getInt("security.secretTokenExpirationLimit"));

        // Get the collection (conection to our mongo database)
        DBCollection coll = connectDB();

        // Create the query
        BasicDBObject query = new BasicDBObject().
        append("username", user.username).
        append("email", user.email).
        append("password", user.password).
        append("profile", JSON.parse(user.profile.toString())).
        append("latchAccountId", user.latchAccountId).
        append("secretToken", SecurityController.createSecretToken()).
        append("secretTokenExpiration", calendar.getTime());

        coll.insert(WriteConcern.SAFE,query);

        // Close conection
        mongoClient.close();

        // Returns the new user
        return user;
    }

    /**
     * Method to update the latchAccountId
     * @param username String with username to update
     * @param latchAccountId String with accountId to include
     */
    public static void updateLatchAccountId(String username, String latchAccountId) {
        DBCollection coll = connectDB();
        BasicDBObject query = new BasicDBObject().append("username", username);
        DBObject one = coll.findOne(query);

        if(one!=null) {
            BasicDBObject updateQuery = new BasicDBObject().append("$set", new BasicDBObject().append("latchAccountId", latchAccountId));
            coll.update(query, updateQuery);
        }

        mongoClient.close();
    }

    /**
     * This method get a user by username
     * @param username String username of the user we want to get
     * @return User specified by username or null if the user doesn't exist
     */
    public static User getUser(String username) {

        DBCollection coll = connectDB();
        BasicDBObject query = new BasicDBObject().append("username", username);
        DBObject one = coll.findOne(query);

        if (one != null) {
            mongoClient.close();
            User.Profile profile = new User.Profile();
            if (one.get("profile") != null){
                String profileStr = one.get("profile").toString();

                if (profile != null) {
                    profile = new Gson().fromJson(profileStr , User.Profile.class);
                }
            }
            return new User(String.valueOf(one.get("username")),
                    String.valueOf(one.get("email")),
                    String.valueOf(one.get("password")),
                    profile,
                    String.valueOf(one.get("latchAccountId")),
                    String.valueOf(one.get("secretToken")),
                    (Date) one.get("secretTokenExpiration"));
        }

        return null;
    }

    /**
     * Method to update the user secretToken
     * @param username String username of the user to update
     */
    public static void updateSecretToken(String username){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, configSecurity.getInt("security.secretTokenExpirationLimit"));

        DBCollection coll = connectDB();
        BasicDBObject query = new BasicDBObject().append("username", username);
        DBObject one = coll.findOne(query);

        if(one!=null) {
            BasicDBObject updateQuery = new BasicDBObject().append("$set", new BasicDBObject().
                    append("secretToken", SecurityController.createSecretToken()).
                    append("secretTokenExpiration", calendar.getTime()));
            coll.update(query, updateQuery);
        }

        mongoClient.close();
    }

    /**
     * Method to update User information
     * @param username String username of the user to edit
     * @param user User updated
     */
    public static void updateUser(String username, User user){
        DBCollection coll = connectDB();
        BasicDBObject query = new BasicDBObject().append("username", username);
        DBObject one = coll.findOne(query);

        if(one!=null) {
            BasicDBObject updateQuery = new BasicDBObject().append("$set", new BasicDBObject().
                    append("username", user.username).
                    append("email", user.email).
                    append("password",user.password).
                    append("profile",JSON.parse(user.profile.toString())));
            coll.update(query, updateQuery);
        }

        mongoClient.close();
    }

}
