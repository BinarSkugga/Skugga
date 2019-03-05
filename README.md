[![Build Status](https://travis-ci.com/BinarSkugga/Skugga.svg?branch=master)](https://travis-ci.com/BinarSkugga/Skugga)
# Skugga
Skugga is an HTTP API that helps Java developers make and publish simple yet efficient REST services.
It provides utilities to implement controllers and endpoints that connects easily and support
inheritance of behavior. Using simple annotations and basic implementations it can link your
data with your controllers, your http exchange and your session handlers.

## Install
### Maven
```xml
<repository>
	<id>skugga.repo</id>
	<url>https://raw.github.com/binarskugga/skugga/maven</url>
	<snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>
```
#### JDK 11
```xml
<dependency>
	<groupId>com.binarskugga</groupId>
	<artifactId>skugga</artifactId>
	<version>2.1</version>
</dependency>
```
#### JDK 8
```xml
<dependency>
	<groupId>com.binarskugga</groupId>
	<artifactId>skugga</artifactId>
	<version>2.1-j8</version>
</dependency>
```
