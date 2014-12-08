#!/bin/bash

export VCS_PATH=https://raw.github.com/wws2003/AutoSpringInfra/master
export VCS_CERTIFICATE_FILE=github_certificate.pem
export TOMCAT_LIB_PATH=$HOME/servers/apache-tomcat-6.0.41/lib
export TOMCAT_WEBAPP_PATH=$HOME/servers/apache-tomcat-6.0.41/webapps

function getFileFromVCS() {
	echo "Getting $1 from the internet. You can edit it later"
	remotePath=$VCS_PATH/$1
	echo "Going to get $remotePath"
	wget --ca-certificate=$VCS_CERTIFICATE_FILE -O $2/$1 $remotePath
}

function cleanAll() {
    rm -f *~
    rm -rf .bck
    rm -rf src
    rm -rf target
    rm -f pom.xml
}

function clean() {
	rm -f *~
	rm -rf .bck
	mkdir .bck
	mv -f src target pom.xml .bck
}

function createFolders() {
	mkdir src
	mkdir src/main src/test src/repo
	mkdir src/main/java src/main/webapp
	mkdir -p src/main/java/com/techburg/autospring/controller
	mkdir src/main/webapp/WEB-INF src/main/webapp/resources 
	mkdir src/main/webapp/WEB-INF/views src/main/webapp/WEB-INF/spring-conf src/main/webapp/WEB-INF/lib
	mkdir src/main/webapp/resources/css src/main/webapp/resources/js src/main/webapp/resources/images 
}

function createConfigFiles() {
	getFileFromVCS pom.xml .
	getFileFromVCS web.xml src/main/webapp/WEB-INF
	getFileFromVCS springmvc-conf.xml src/main/webapp/WEB-INF/spring-conf
}

function createSrcFiles() {
	getFileFromVCS HelloController.java src/main/java/com/techburg/autospring/controller
	getFileFromVCS hello.jsp src/main/webapp/WEB-INF/views
}

function importDependenciesToLocalRepo() {
	mkdir -p src/repo/javax/servlet/servlet-api/2.5
	cp $TOMCAT_LIB_PATH/servlet-api.jar src/repo/javax/servlet/servlet-api/2.5
	mkdir -p src/repo/javax/servlet/jsp/jsp-api/2.1
	cp $TOMCAT_LIB_PATH/jsp-api.jar src/repo/javax/servlet/jsp/jsp-api/2.1
}

function createProject() {
    createFolders;
    createConfigFiles;
    createSrcFiles;
    importDependenciesToLocalRepo;

    mvn2 package

    rm -rf $TOMCAT_WEBAPP_PATH/autospring.war
    rm -rf $TOMCAT_WEBAPP_PATH/autospring
    cp target/autospring.war $TOMCAT_WEBAPP_PATH
}

function showArgumentsNumberErrorMessage() {
    echo "Wrong number of arguments. Usage: auto_spring.sh clean or auto_spring.sh build";
}

function showArgumentsErrorMessage() {
    echo "Wrong arguments. Usage: auto_spring.sh clean or auto_spring.sh build";
}

if [ "$#" -ne 1 ]; then
    showArgumentsNumberErrorMessage;
    exit 1;
else
    if [ "$1" == "clean" ]; then
        cleanAll;
        exit 0;
    fi
    if [ "$1" == "build" ]; then
        clean;
        createProject;
        exit 0;
    fi
    showArgumentsErrorMessage;
    exit 1;
fi

