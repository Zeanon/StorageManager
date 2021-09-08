
# StorageManager
StorageManager - A Library to store data in a better way.

Want to do more than just use simple .yml files store data?<br>
Are you looking for a powerful yet easy to use, expendable library to store data in files?<br>

**Then this library is just right for you.**

### Table of contents:
-1. [A few bits of information] <br>
-2. [Supported file types] <br>
-3. [How to setup] <br>
-4. [Usage]coming soon <br>
-5. [Real world examples][Wiki] <br>



StorageManager is extremely fast & good at **storing data reliably**! <br>
It also supports **nested objects**!<br>
StorageManager is licensed under the GNU GLP3.

If you have any ideas to add or issues just open a issue page. I will do my best to help.<br>
<br>

### Supported file types
Json<br>
ThunderFile<br>
Toml<br>
Yaml<br>
<br>

### How to setup

#### Maven

Just add the Jars of the modules you need as a local dependency(https://maven.apache.org/plugins/maven-install-plugin/)
	

3. Important! Use a shade plugin to make sure that the library is shaded into your final .jar file when your
plugin is compiled. 
Make sure, that every module is shaded into the same base folder(like 'de.zeanon.test.shaded.storagemanagercore' and 'de.zeanon.test.shaded.thunderfilemanager').
You always need the Core for everything to work, the other ones are optional, depending on the languages you want to support.

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-shade-plugin</artifactId>
	<version>3.1.0</version>
	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>shade</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<relocations>
			<relocation>
				<pattern>de.zeanon</pattern>
				<shadedPattern>yourpackage.yourname.storage</shadedPattern>
			</relocation>
		</relocations>
		<createDependencyReducedPom>false</createDependencyReducedPom>
                <minimizeJar>true</minimizeJar>
	</configuration>
</plugin>
```	   
<br>
<br>

**Library's used**

StorageManager uses a powerful combination of libraries to provide best usability: 

>MIT-org.json Copyright (c) 2002 JSON.org <br>
>YAMLBEANS - Copyright (c) 2008 Nathan Sweet, Copyright (c) 2006 Ola Bini <br>
