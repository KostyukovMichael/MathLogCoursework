#!/bin/bash

cd einstein/

mvn clean package > ../package_log.txt

java -jar target/einstein-1.0-SNAPSHOT.jar
