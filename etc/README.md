#testing project for REST service testing with RestEasy.

#1. configure maven
export M2_HOME=/opt/apache-maven-3.3.3
export M2=$M2_HOME/bin
export PATH=$M2:$PATH

#2. version match between JBoss server and client (ReasEasy version 3.0.10 selected)


#3. When deploy the new war artifact from maven, it is assumed that the user in the management group has following credentials:
    administrator/Password1! (in pom.xml). You need change it to your Jboss credentials. If you have not set up a user for the management console,
    you can do it by running add-user.bat in the JBoss_Home/bin directory.
    
#4. If you got http 404 error when running mvn wildfly:deploy, it could be caused by the initial deployment failure of the war artifact to the JBoss server. You can do it manually for the first time. 