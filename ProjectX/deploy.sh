export CATALINA_HOME=/Users/wws2003/servers/apache-tomcat-6.0.41
rm -rf $CATALINA_HOME/webapps/autospring*
echo "Removed webapp folder in catalina home!"
mvn2 package -Dmaven.test.skip=true
cp target/autospring.war $CATALINA_HOME/webapps/
echo "Copied artifact to catalina home!"
