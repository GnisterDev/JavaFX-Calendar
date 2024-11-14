# Installation and Setup Guide

This guide provides steps to set up the development environment in Eclipse Che

### 1. Install dependencies

Follow these steps to install Java 21 and other dependencies on your system or within the Eclipse Che environment. Install it with the following commands:

```bash
sudo apt update
sudo apt install openjdk-21-jdk screen
```

### 2. Set JAVA_HOME to Java 21

After installing Java 21, configure the JAVA_HOME environment variable to point to the Java 21 installation:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### 3. Build the project

After setting up Java 21, build the project:

```bash
mvn clean install compile
```

### 4. Run the rest API

The rest api must run in the background for the client to work.

```bash
screen -dmS restAPI mvn exec:java
```

### 5. Run the javaFX app

```bash
mvn javfx:run
```
