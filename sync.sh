#!/bin/env bash
/home/cfly/apache-maven-3.0.5/bin/mvn compile -f /home/cfly/feedc/pom.xml
/home/cfly/apache-maven-3.0.5/bin/mvn exec:java -Dexec.mainClass=org.caofei.feedc.App -f /home/cfly/feedc/pom.xml -Dexec.arguments='http://cnbeta.com/backend.php,http://t66y.com/rss.php?fid=7'