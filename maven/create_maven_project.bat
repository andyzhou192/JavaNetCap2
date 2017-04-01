@rem create maven project start
@echo disk=%1
@echo dir=%2
@echo groupId=%3
@echo artifactId=%4
@%1
@cd %2
@mvn archetype:generate -DgroupId=%3 -DartifactId=%4 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
@rem create maven project end