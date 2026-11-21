#!/bin/bash

cd einstein/
mvn clean package > /dev/null
java -jar target/einstein-1.0-SNAPSHOT.jar
