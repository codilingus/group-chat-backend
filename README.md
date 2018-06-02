# group-chat-backend

Group-chat-backend is application created in IntelliJ using Spring.

### Installation
#### Import project
Before running project make sure that user has installed Java 8 and Maven 3 - check with command:
```
java -version
mvn --version
```

There are two ways to import and run project:
##### a) TL;DR - running the application without IntelliJ
```
git clone https://github.com/codilingus/group-chat-backend.git
cd group-chat-backend
mvn spring-boot:run
```

##### b) using IntelliJ
- clone repository
```
git clone https://github.com/codilingus/group-chat-backend.git
```
- import project using IntelliJ
- run GroupChatBackendApplication.class

#### Database
Database is created by Spring in-memory. It is started everytime the project is opened.
To kill the database close project. Note that all content will be cleared.
