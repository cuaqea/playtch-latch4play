### PLAYTCH: LATCH4PLAY. A LATCH PLAY FRAMEWORK PLUGIN ###

#### INTRODUCTION ####
Playtch is a plugin/module for Play Framework projects that wanted to implement in an easy way Latch (http://latch.elevenpaths.com)

Playtch includes a dummy application presenting a typical web where a user can sign up, login and edit its profile.
cuaqea/playtch-dummy-application

#### PREREQUISITES ####

* Play Framework 2.2.2 or above.

* Read API documentation (https://latch.elevenpaths.com/www/developers/doc_api).

* To get the "Application ID" and "Secret", (fundamental values for integrating Latch in any application), it’s necessary to register a developer account in Latch's website: https://latch.elevenpaths.com. On the upper right side, click on "Developer area".

* [WARNING] Install CACertificate: If is the first time you are using Latch, you need to add a specific certification to your java keystore. For example in Mac OS:
```
	sudo keytool -import -noprompt -trustcacerts -alias CACertificate -file ca.pem -keystore "/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit
```

#### USING THE PLUGIN ####

* Publish the plugin locally
```
	play
	clean
	publishLocal
```

* Using in your Play Framework project
Add this line in your build.sbt file
```
	  "com.cuaqea.playtch" %% "playtch-latch4play" % "1.0"
```