#!/bin/sh
# ##################################################################
# Powered by Jason
# ##################################################################
java -jar ../lib/app-quartz-0.0.1-SNAPSHOT.jar &
#PATH_TO_JAR=/usr/local/wwd/app-quartz/app-quartz/lib/app-quartz-0.0.1-SNAPSHOT.jar
#java -jar $PATH_TO_JAR 2>&1 > /usr/local/wwd/app-quartz/app.log &
echo $! > /usr/local/wwd/app-quartz/app-quartz.pid    