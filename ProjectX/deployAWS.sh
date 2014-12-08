export RESOURCES=src/main/resources
export AWS_RESOURCES=aws/resources
export TARGET=target
export ARTIFACT=$TARGET/autospring.war

#Backup local resource
cat $RESOURCES/hibernate.cfg.xml > $AWS_RESOURCES/hibernate.cfg.xml
cat $RESOURCES/locations.properties > $AWS_RESOURCES/locations.properties

#Build with resource proper for aws
cat $AWS_RESOURCES/aws_hibernate.cfg.xml > $RESOURCES/hibernate.cfg.xml
cat $AWS_RESOURCES/aws_locations.properties > $RESOURCES/locations.properties
mvn2 package -Dmaven.test.skip=true

#Clean webapp folder on AWS and back up file
ssh -i ~/Downloads/myec2key.pem ubuntu@54.201.201.43 "rm -rf \$HOME/servers/apache-tomcat-6.0.41/webapps/autospring; rm -f \$HOME/bck/*"

#Backup war file
ssh -i ~/Downloads/myec2key.pem ubuntu@54.201.201.43 "cp \$HOME/servers/apache-tomcat-6.0.41/webapps/autospring.war \$HOME/bck"

#Copy artifact to aws
scp -i ~/Downloads/myec2key.pem $ARTIFACT ubuntu@54.201.201.43:/home/ubuntu/servers/apache-tomcat-6.0.41/webapps/

#Restore local resource
cat $AWS_RESOURCES/hibernate.cfg.xml > $RESOURCES/hibernate.cfg.xml
cat $AWS_RESOURCES/locations.properties > $RESOURCES/locations.properties

#Clean
rm -rf $TARGET