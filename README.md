Maven GWT WTP integration plugin
And sample archetype

Archetype installation:

cd org.maven.archetype.gdt.m2eclipse-gdt-webapp/
mvn clean install

Plugin build:

cd org.maven.ide.eclipse.gdt/

mvn clean install


The archetype will allows you to bootstrap a project.

Usage sample:

mvn archetype:generate -DarchetypeGroupId=org.maven.archetype.gdt -DarchetypeArtifactId=m2eclipse-gwt-webapp -DarchetypeVersion=1.0.0 -DgroupId=test -DartifactId=test -Dversion=1.0.0 -DclassPrefix=Test -Dname=MyName -DgwtModule=MyModule

