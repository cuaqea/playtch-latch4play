# PLAYTCH - LATCH4PLAY #
A LATCH PLAY FRAMEWORK PLUGIN

## INTRODUCTION ##
Playtch is a plugin/module for Play Framework projects that wanted to implement in an easy way Latch (http://latch.elevenpaths.com)

Playtch includes a dummy application presenting a typical web where a user can sign up, login and edit its profile.
https://github.com/cuaqea/playtch-latch4play/tree/master/samples/playtch-sample

## PREREQUISITES ##

* Play Framework 2.2.2 or above.

* Read API documentation (https://latch.elevenpaths.com/www/developers/doc_api).

* To get the "Application ID" and "Secret", (fundamental values for integrating Latch in any application), it’s necessary to register a developer account in Latch's website: https://latch.elevenpaths.com. On the upper right side, click on "Developer area".

* [WARNING] Install CACertificate: If is the first time you are using Latch, you need to add a specific certification CA (http://www.startssl.com/certs/ca.pem) to your java keystore. For example in Mac OS:
```
	sudo keytool -import -noprompt -trustcacerts -alias CACertificate -file ca.pem -keystore "/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit
```

## USING THE PLUGIN ##
### Add plugin to your project ###
A. Publish the plugin locally. If you want to publish local this plugin, you have to exec play and type this commands
```
	clean
	publishLocal
```

B. Using in your Play Framework project (RECOMMENDED)
You need to add the following to your Play build.sbt
```

     resolvers += Resolver.url(
            "Playtch Repository",
            url("http://cuaqea.github.io/playtch-latch4play/releases/")
        )(Resolver.ivyStylePatterns)


     libraryDependencies ++= Seq(
       // other dependencies
       ... % ... % ...
       //
       "com.cuaqea.playtch" % "playtch-latch4play_2.10" % "1.0"
     )

```

In both cases, you need to specify your appId and secretKey in your project application.conf file like this:
```
    latch.appId = "YOUR_APP_ID"
    latch.secretKey = "YOU_APP_SECRET_KEY"
```

### Using in your code ###
There are two ways of using Playtch in your source code:
* Calling LatchController directly

* Using notations (RECOMMENDED)
  * @LachCheckStatus: to check a Latch application status
  * @LatchCheckOperationStatus: to check a specific Latch operation
  * @LatchPair: to perform pairing
  * @LatchUnpair: to perform unpairing

### Examples ###
#### 1. Latching an app ####
* Imports
```
    ...
    import actions.LatchCheckStatus;
    import utils.factories.LatchIdFactory;
    ...
```
* Latching your method
```
    @LatchCheckStatus(value = "LATCH_APPLICATION_ID", latchId = LatchIdFactory.class)
    public static Result submit() {
        ...

        // Get status from the Notation output argument and assign that value to isLatchOn
        Boolean isLatchOn = (Boolean) Http.Context.current().args.get("status");

        ...
        // Check user authentication

        ...

        // If user is authenticated and latch status is on
        if (isLatchOn) {
            // User is allowed to perform Login
            // Show user logged view
        }

        ...

        // User can't login
        // Show an error in your login view
    }
```
Note:
you need to pass to the notation two arguments:
* value: String with your Latch operation id
* latchId: a class that implements a method call getLatchId. See the following example:
```
    ...
    import latchid.ObtainLatchId;
    ...

    public class LatchIdFactory implements ObtainLatchId {

        @Override
        public String getLatchId(Http.Context context) {
            // Get "username" from context. For example if we get from a form with an inputText called "username"
            // and knowing that our User model has got a field called latchAccountId where we store the accountId returned
            // after pairing:
            String userId = context.request().body().asFormUrlEncoded().get("username")[0].toString();
            UserDataSource userDataSource = new UserDataSource();

            // Get full User by username
            User user = userDataSource.getUser(userId);

            if (user != null) {
                // Check if user is paired
                if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))) {
                    // If paired, return its accountId
                    return user.latchAccountId;
                }
            }
            // If not, returns null
            return null;
        }
    }
```

#### 2. Latching an operation: Login ####
* Imports
```
    ...
    import actions.LatchCheckOperationStatus;
    import utils.factories.LatchIdFactory;
    ...
```
* Latching your method
```
    @LatchCheckOperationStatus(value = "LATCH_LOGIN_OPERATION_ID", latchId = LatchIdFactory.class)
    public static Result submit() {
        ...

        // Get status from the Notation output argument and assign that value to isLatchOn
        Boolean isLatchOn = (Boolean) Http.Context.current().args.get("status");

        ...
        // Check user authentication

        ...

        // If user is authenticated and latch status is on
        if (isLatchOn) {
            // User is allowed to perform Login
            // Show user logged view
        }

        ...

        // User can't login
        // Show an error in your login view
    }
```
Note:
you need to pass to the notation two arguments:
* value: String with your Latch operation id
* latchId: a class that implements a method call getLatchId. See the following example:
```
    ...
    import latchid.ObtainLatchId;
    ...

    public class LatchIdFactory implements ObtainLatchId {

        @Override
        public String getLatchId(Http.Context context) {
            // Get "username" from context. For example if we get from a form with an inputText called "username"
            // and knowing that our User model has got a field called latchAccountId where we store the accountId returned
            // after pairing:
            String userId = context.request().body().asFormUrlEncoded().get("username")[0].toString();
            UserDataSource userDataSource = new UserDataSource();

            // Get full User by username
            User user = userDataSource.getUser(userId);

            if (user != null) {
                // Check if user is paired
                if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))) {
                    // If paired, return its accountId
                    return user.latchAccountId;
                }
            }
            // If not, returns null
            return null;
        }
    }
```
### 3. Pairing ###
* Imports
```
    ...
    import actions.LatchPair;
    import utils.factories.PairingKeyFactory;
    ...
```
* Pairing method
```
    @LatchPair(pairingKey = PairingKeyFactory.class)
    public static Result pair() {
        // Get the account id from notation LatchPair output argument "status"
        String accountId = (String) Http.Context.current().args.get("status");
	...

        if (accountId != null) {
            // If accountId is not null, this is the right accountId that we have to store in the user database
            UserDataSource userDataSource = new UserDataSource();
            User user = userDataSource.getUser(session("username"));

            // Update accountId
            userDataSource.updateLatchAccountId(user.username, accountId);
            Logger.debug("Pairing success!");

            // Show the unpair view
            return ok(views.html.latch.unpair.render());
        } else {
            // Else, pairing was fail
            Logger.debug("<Error> Pairing fail");
            // Show again pair view
            return badRequest(views.html.latch.pair.render(filledForm));
        }
    }
```
* PairingKeyFactory implementation
```
...
import pairingkey.ObtainPairingKey;
...

public class PairingKeyFactory implements ObtainPairingKey {

    /**
     * This method returns a pairing key given in a form with an input called "key"
     * @param context
     * @return
     */
    @Override
    public String getPairingKey(Http.Context context) {
        // Returns the value of inputText "key" sended in the HTTP Request
        return context.request().body().asFormUrlEncoded().get("key")[0].toString();
    }

}
```

### 4. Unpairing ###
* Imports
```
    ...
    import actions.LatchUnpair;
    import utils.factories.LatchIdFromSessionFactory;
    ...
```
* Unpairing method
Note: in that case, the user needs to be logged so we are going to get the accountId by the user session. So we have implemented another factory called LatchIdFromSessionFactory
```
    @LatchUnpair(latchId = LatchIdFromSessionFactory.class)
    public static Result unpair() {
    	...
        // Get the unpairing status from notation output argument "status"
        String status = (String) Http.Context.current().args.get("status");
	...
        // Check the unpairing status
        if (status != null) {
            // If everything was ok, we clean the accountId from the user database

            // Show pair view
            return badRequest(views.html.latch.pair.render(filledForm));
        } else {
            // Else, if we had a mistake
            Logger.debug("<Error> Unpair fail");
            // Show again unpair view
            return badRequest(views.html.latch.unpair.render());
        }
    }
```
* Implemented factory LatchIdFromSessionFactory
```
...
import latchid.ObtainLatchId;
...

public class LatchIdFromSessionFactory implements ObtainLatchId {

    /**
     * Method that returns an accountId getting it from the database. It use sessions.
     * @param context Http.Context HTTP request that includes the username to get the user form database
     * @return String with accountId if user is paired or null if not
     */
    @Override
    public String getLatchId(Http.Context context) {
        // Get username from session included inside context
        String userId = context.session().get("username");
        UserDataSource userDataSource = new UserDataSource();

        // Retrieve User information by username
        User user = userDataSource.getUser(userId);

        // Check if user is paired
        if (!(user.latchAccountId.equals("null") || user.latchAccountId.equals(""))){
            // If paired, return accountId
            return user.latchAccountId;
        }
        // Else, returns null
        return null;
    }
}
```
