#!/bin/env bash
/home/cfly/apache-maven-3.0.5/bin/mvn complie -f /home/cfly/feedc/pom.xml
/home/cfly/apache-maven-3.0.5/bin/mvn exec:java -Dexec.mainClass=org.caofei.feedc.App -f /home/cfly/feedc/pom.xml