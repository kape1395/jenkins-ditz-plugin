#!/bin/bash

JOB_DIR=work/jobs/test1
WS=$JOB_DIR/workspace

if [[ -f pom.xml ]] ; then
    mkdir -vp $WS/module/target
    mkdir -vp $WS/module/bugs
    cp -vr src/test/resources/bugs-01/* $WS/module/bugs/
    cp -vr target/surefire-reports $WS/module/target/
    echo "This is content of built artifact" > $WS/module/target/test1.jar
else
    echo -ne "# ERROR, this script sould be run from the project's root folder.\n"
fi
