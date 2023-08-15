#!/bin/bash

# this script is a note of how to use the java agent and the commands needed
mvn clean package
cd example; make; cd ..

echo "------------------------------------------------------------------------"
java -javaagent:./target/java-io-capture-1.0-SNAPSHOT.jar -jar example/example.jar
