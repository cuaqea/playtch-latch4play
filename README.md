# PLAYTCH - LATCH4PLAY.
 A LATCH PLAY FRAMEWORK PLUGIN #

### INTRODUCTION ###
Playtch is a plugin/module for Play Framework projects that wanted to implement in an easy way Latch (http://latch.elevenpaths.com)

Playtch includes a dummy application presenting a typical web where a user can sign up, login and edit its profile.
https://github.com/cuaqea/playtch-latch4play/tree/master/samples/playtch-sample

### PREREQUISITES ###

* Play Framework 2.2.2 or above.

* Read API documentation (https://latch.elevenpaths.com/www/developers/doc_api).

* To get the "Application ID" and "Secret", (fundamental values for integrating Latch in any application), it’s necessary to register a developer account in Latch's website: https://latch.elevenpaths.com. On the upper right side, click on "Developer area".

* [WARNING] Install CACertificate: If is the first time you are using Latch, you need to add a specific certification to your java keystore. For example in Mac OS:
```
	sudo keytool -import -noprompt -trustcacerts -alias CACertificate -file ca.pem -keystore "/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit
```

### USING THE PLUGIN ###
#### Add plugin to your project ####
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

#### Using in your code ####
There are two ways of using Playtch in your source code:
* Calling LatchController directly

* Using notations (RECOMMENDED)
  * @LachCheckStatus: to check a Latch application status
  * @LatchCheckOperationStatus: to check a specific Latch operation
  * @LatchPair: to perform pairing
  * @LatchUnpair: to perform unpairing

#### Examples ####
##### Login #####
---
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

