#testing project for REST service testing with JBoss/RestEasy.


#1. configure maven
export M2_HOME=/opt/apache-maven-3.3.3

export M2=$M2_HOME/bin

export PATH=$M2:$PATH

#2. install JBoss wildfly 8.2.1.Final

#3. Testing:
    running JBoss in standalone mode
    JBoss_Home/bin/standalone.sh
    
    then run
    mvn wildfly:deploy
