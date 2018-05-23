#!/bin/bash
# Maven path
export MVN_PATH=$HOME/servers/tool/apache-maven-3.5.3/bin/
export PATH=$PATH:$MVN_PATH
# Archetype
export MVN_ARCHETYPE_GROUP_ID=hpg
export MVN_ARCHETYPE_ARTIFACT_ID=hpg-archetype-spring5
export MVN_ARCHETYPE_VERSION=1.1-SNAPSHOT

# Glassfish containing folder
export usr_glassfish_home_prefix=$HOME/servers
# Deployed app archiving folder
export usr_glassfish_deployed_apps_home=glassfish-deployed-apps
# Webapp group ID
export usr_webapp_group_id=vienna.mendel
# Webapp artifact ID
export usr_webapp_artifact_id=mendel
# Webapp name
export usr_webapp_name=mendel
# Webapp version
export usr_webapp_version=1.0-SNAPSHOT