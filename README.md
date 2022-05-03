# README

This app is an exercice about implementing an API on an existing app called poseidon.

## Technical:

1. Framework: Spring Boot v2.6.6
2. Java 11
3. Thymeleaf
4. Bootstrap v.4.3.1


## Before use

1. Read Database Documentation
2. Read application.properties documentation
3. How to launch documentation

## Database documentation

### Main database : poseidon

This app uses a MySQL DB. Here are some information about how to install this DB.<br>

1. Download and install MySQL : https://dev.mysql.com/downloads/installer/
2. Record carefully your RootUsername and password to this DB.
3. Open a command prompt :
    1. type in and press ENTER: mysql -u 'yourRootUsername' -p
    2. write down your password when asked and press ENTER
    3. type in and press ENTER : CREATE DATABASE poseidon;
    4. type in and press ENTER : exit
    5. locate both data.sql and schema.sql. They are normally inside resources file of this project.
    6. Go back to your prompt and type in + Enter :
        1. mysql -u username -p database_name < {full path to schema.sql}
    7. You are now good to go, and have correctly initialised your database

### Test database : poseidontest

1. Open a command prompt where createTestUser.sql is located (normally in tests resources) :
    1. type in and press ENTER: mysql -u 'yourRootUsername' -p
    2. write down your password when asked and press ENTER
    3. type in and press ENTER : CREATE DATABASE poseidontest;
    4. type in and press ENTER : exit
    5. type in : source createTestUser.sql
    6. press ENTER.
    7. Enjoy !

## Application.properties documentation

This file is located in src/main/resources.<br>
It is used to give properties to the spring boot application. In this project there is already one called
application.example.properties.

1. If you want to use this app through its sources files:
    1. Duplicate application.example.properties and rename it application.properties. Make sur it is located in
       src/main/resources
    2. add the correct values for each property :
        1. spring.datasource.username
        2. spring.datasource.password
        3. spring.datasource.url
        4. spring.jpa.properties.hibernate.dialect (use org.hibernate.dialect.MySQL8Dialect by default if you use a MySQL DB)
        5. All the entries concerning ### OAuth2.0 ###
2. If you use the jar file:
    1. Create a /config directory in the root directory where the jar is located.
    2. add application.properties in this /config directory
    3. follow steps above to configure application.properties

Enjoy !

## How To launch
- If you want to launch the app, you can launch the sources with your favorite IDE. Or you can launch the .jar file with your command prompt.
- Then use your internet browser and go to 'localhost:8080' to get into the app (tested on firefox).
    - the 8080 port can be changed into the application.properties mentioned above.
    - Here is the admin account by default in the app :
        - For admin --> id : admin / pwd : admin
        - For user --> id : user / pwd : user