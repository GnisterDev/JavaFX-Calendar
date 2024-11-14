#! /bin/bash

# Install dependencies
sudo apt update
sudo apt install openjdk-21-jdk screen -y

# Set java version to 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Compile project
mvn clean install compile -DskipTests

# Run rest API in background
screen -dmS restAPI mvn exec:java

# Run javaFX ui
mvn javafx:run
