# Grocery Booking Application

Grocery Booking Application provide api's for new user registration,
user login authentication to get jwt token and api's to purchase and manage
 groceries added to db.

### Steps to start Grocery Booking Application

> Prerequisites Installations :  Java 17, Maven, Git Client  

> Optional : Docker, Docker Compose, PostgreSQL

1. clone the project using below command (make sure you have the git client installed otherwise you can directly download the project as zip and extract it).

    ```bash 
    git clone https://github.com/shubham-wankhede/qp-assessment.git

2. once you project cloned or extracted build/install the project using below command.

   >  important : Before you do mvn install make sure you have PostgreSQL is runing if not please start and you can stop it once your install is complete. (You can follow approach 2 to see how to start postgresql from docker)

    ```bash
   mvn clean install 

3. once you build your project you can take three approaches to run your application.

#### Approach (1) : On Local System
1. make sure you have PostgreSQL DB installed with **grocery_store** database created
2. run the java file inside GroceryBookingApplication **java/main/com/gb/GroceryBookingApplication.java** considering you already have java 17 installed in your system.


#### Approach (2) : Using Dockerfile
1. if PostgreSQL installed locally start it or use below docker command to start the PostgreSQL container

    ```bash
    docker run -p 5432:5432 -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -e POSTGRES_DB=grocery_store postgres:latest 

2. Build and run GroceryBookingApplication as container

    Build GroceryBookingApplication image

    ```bash
    docker build -t grocery-app .
    ```
3. get local machine ip address and replace it below
    
    command to get ip `mac >  ipconfig getifaddr en0` and
    `windows >  ipconfig`

    replace : `{ip-addres} : ip address obtained from above`

4. run container of image

    ```bash
    docker run -p 8088:8088  -e SPRING_DATASOURCE_URL=jdbc:postgresql://{ip-addres}:5432/grocery_store  -e SPRING_DATASOURCE_USERNAME=username -e  SPRING_DATASOURCE_PASSWORD=password grocery-app
    ```
#### Approach (3) : Using Docker Compose ## SIMPLEST

1. if you have docker installed the run below docker-compose file ad that's all
   > important : make sure your 8088 and 5432 port is free and there is no other processes running on it.

    ```bash
   docker-compose up -d
   ```
      
   > Very Application is stared by hitting hello api `http://localhost:8088/api/v1/hello`

### Testing Grocery Booking Api

 Grocery Booking Api documentation is provided through openApi document and swagger ui alternatively you can use the postman collection provided inside the `resources` folder in root.
 
 once your application is started use below url to access the swagger ui.
   ```
   http://localhost:8088/swagger-ui.html
   ```
   ```
   Steps to Access Grocery Purchase and Grocery Fetch Api
   
   1. create user using user registration api `/api/v1/users/register`
   2. login using login api to get jwt token `/api/v1/auth/login`
   3. use the jwt token obtained in login and use it with request header `Authorization Bearer jwtToken`
   ```

for all authorized request you have to pass the jwt token to access them

> important : grocery management api `(/api/v1/admin/groceries)` are secured for admin user hence create user with admin to access them

> important : grocery purchase and grocery fetch api `(/api/v1/groceries)`  can be accessed with normal user
