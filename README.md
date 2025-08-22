# Backend for Viteezy

* Dropwizard
* Spring
* REST

# Requirements
- Java 11
- Maven 3

# Install
```
mvn clean package
```

# Start
```
java -jar target/backend-1.0-SNAPSHOT.jar server config.yml
```

# Create a /data directory on macOS
```
vi /etc/synthetic.conf
data	System/Volumes/Data/data
```
(spacing after data should be a tab, otherwise it doesn't work, and reboot)