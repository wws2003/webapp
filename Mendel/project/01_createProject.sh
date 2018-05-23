#!/bin/bash
# Set environment variables
echo ""
source 00_setEnv.sh
echo "---------------Set up enviroment variables----------"
echo "Exporting {usr_glassfish_home_prefix} = "${usr_glassfish_home_prefix}
echo "Exporting {usr_glassfish_deployed_apps_home} = "${usr_glassfish_deployed_apps_home}
echo "Exporting {usr_webapp_group_id} = "${usr_webapp_group_id}
echo "Exporting {usr_webapp_artifact_id} = "${usr_webapp_artifact_id}
echo "Exporting {usr_webapp_name} = "${usr_webapp_name}
echo "Exporting {usr_webapp_version} = "${usr_webapp_version}
echo "----------------------------------------------------"
echo ""

# Generate project from standard archetype
echo "---------------Generate project from jar file archetype----------"

# ----Install to local repository
mvn install:install-file \
	-Dfile=./setup/jar/${MVN_ARCHETYPE_ARTIFACT_ID}-${MVN_ARCHETYPE_VERSION}.jar \
	-DgeneratePom=true \
	-DgroupId=${MVN_ARCHETYPE_GROUP_ID} \
	-DartifactId=${MVN_ARCHETYPE_ARTIFACT_ID} \
	-Dversion=${MVN_ARCHETYPE_VERSION} \
	-Dpackaging=jar \
	-U

# ----Creare project from installed archetype
mvn archetype:generate \
	-DarchetypeGroupId=${MVN_ARCHETYPE_GROUP_ID} \
	-DarchetypeArtifactId=${MVN_ARCHETYPE_ARTIFACT_ID} \
	-DarchetypeVersion=${MVN_ARCHETYPE_VERSION} \
	-DgroupId=${usr_webapp_group_id}  \
	-DartifactId=${usr_webapp_artifact_id} \
	-Dversion=${usr_webapp_version} \
	--batch-mode

# Build to test
echo "---------------First build to test----------"
cd ${usr_webapp_artifact_id}
mvn clean package
