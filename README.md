Maven GWT WTP integration plugin
And sample archetype

Allow to maintain sync the generated I18N, css, Async class with their sources (respectively I18N bundles, css files, Remte Services classes). 

Plugin build:

cd org.maven.ide.eclipse.gdt/

mvn clean install

Use the update site to install the configurator plugin into your eclipse. 



Archetype installation:

cd org.maven.archetype.gdt.m2eclipse-gdt-webapp/
mvn clean install


The archetype will allows you to bootstrap a project.

Usage sample:

mvn archetype:generate -DarchetypeGroupId=org.maven.archetype.gdt -DarchetypeArtifactId=m2eclipse-gwt-webapp -DarchetypeVersion=1.0.0 -DgroupId=test -DartifactId=test -Dversion=1.0.0 -DclassPrefix=Test -Dname=MyName -DgwtModule=MyModule




