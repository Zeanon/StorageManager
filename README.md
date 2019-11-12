
# StorageManager
StorageManager - A Library to store data in a better way.

Do you want to save your config files easily and **independently** from Bukkit or BungeeCord?<br>
Want to do more than just use simple .yml files store data?<br>
Are you looking for a powerful "bukkitlike" (Very similar to Bukkit config) library to store data in files?<br>

**Then this library is just right for you.**

I was looking for a library that I could use to store data with Bukkit like methods 
without being depended on Bukkit/BungeeCord. But there was nothing out there so I decided to write my own library.
Of course there are a few libraries with bukkitlike methods but no one has the features that I need.
ThunderBolt-2 for example only supports Json files but does not support nested objects.
Now I'am publishing this library because I think libraries of high quality should be publicly available for everyone
Now it is here: **StorageManager**!<br>
<br>

### Table of contents:
-1. [A few bits of information] <br>
-2. [Supported file types] <br>
-3. [How to setup] <br>
-4. [Usage]coming soon <br>
-5. [Real world examples][Wiki] <br>



StorageManager is extremely fast & good at **storing data reliably**! <br>
It also supports **nested objects**!<br>
Like bukkit it has a contains check - for both JSON and YAML.
StorageManager is licensed under the Apache2.0 license, which means that
you can also **use it in private projects** that are not open source.

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

1. Place this in your repository-section: 
```xml
<!-- TheWarKing Repo -->
<repository>
    <id>thewarking-public</id>
    <url>http://thewarking.de:8081/repository/PluginUtils/</url>
</repository>
```

2. Place this in your dependency-section:
```xml
<!-- StorageManager by Zeanon-->
<dependency>
<groupId>de.zeanon</groupId>
    <artifactId>storagemanager</artifactId>
    <version>1.1.8</version>
    <scope>compile</scope>
</dependency>
```
	

3. Important! Use a shade plugin to make sure that the library is shaded into your final .jar file when your
plugin is compiled. 
The relocation is optional but heavily recommended.

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
		<createDependencyReducedPom>false</createDependencyReducedPom>
		<relocations>
			<relocation>
				<pattern>de.zeanon</pattern>
				<shadedPattern>yourpackage.yourname.storage</shadedPattern>
			</relocation>
		</relocations>
	</configuration>
</plugin>
```	   


**Library's used**

StorageManager uses a powerful combination of libraries to provide best usability: 

>MIT-org.json Copyright (c) 2002 JSON.org <br>
>YAMLBEANS - Copyright (c) 2008 Nathan Sweet, Copyright (c) 2006 Ola Bini <br>
