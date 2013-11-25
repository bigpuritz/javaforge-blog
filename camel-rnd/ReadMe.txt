Camel Component Project
=======================

This project is a example of a simple Camel component.

To build this project use

    mvn install

For more help see the Apache Camel documentation:

    http://camel.apache.org/writing-components.html
    

Creating new camel component project using maven archetype
--------------------------------------
    
mvn archetype:generate \
	-DarchetypeGroupId=org.apache.camel.archetypes \
	-DarchetypeArtifactId=camel-archetype-component \
	-DarchetypeVersion=2.12.1 \
	-DgroupId=net.javaforge.blog.camel \
	-DartifactId=camel-rnd \
	-Dname=Rnd \
	-Dscheme=rnd    