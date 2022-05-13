## Authentication Service

This is an authentication microservice built using Spring Boot. It is packaged as a Docker image. The service registers
new users, authenticates users and issues JWT tokens for authenticated users.

It runs with Consul for microservice registration and discovery, a RabbitMQ exchange for message publishing and a
MySQL database.

### Download an image of the application
   ```
    docker pull ghcr.io/fredrik-philippe-vimbayi/auth-microservice:latest
   ```

### Step-by-step Instructions
1. Create a network
    ```
    docker network create <network-name>
    ``` 
2. Run a MySQL database container on the created network
   ```
   docker run -d --name mysql --network <network-name> -e MYSQL_ROOT_PASSWORD=root -e 'MYSQL_ROOT_HOST=%' 
   -e MYSQL_DATABASE=test -e MYSQL_USER=username -e MYSQL_PASSWORD=password mysql:8.0.29
   ```
3. Run a Consul container on the created network
    ```
    docker run -d -p 8500:8500 -p 8600:8600/udp --name=consul consul --network <network-name>
    agent -server -ui -node=server-1 -bootstrap-expect=1 -client='0.0.0.0'
    ```
4. Create an RSA public - private key pair
    1. Save them in a `.env` file as below
        ```
        PUBLIC_KEY=a-public-key
        PRIVATE_KEY=a-private-key
        ```
    2. Save the key pair in a safe place


5. Run this authentication microservice as a named container on the created network
    1. If the public - private key is saved in a `.env` file
        ```
        docker run -d --network <network-name> --name authentication -p 8080:8080 
        --env-file .env ghcr.io/fredrik-philippe-vimbayi/auth-microservice:latest
        ``` 
    2. Run the microservice container with the `-e` (environmental) variable flag
       ```
        docker run -d --network <network-name> --name authentication -p 8080:8080 -e SERVER_PORT=8080
        -e PRIVATE_KEY=a-private-key ghcr.io/fredrik-philippe-vimbayi/auth-microservice:latest
        ``` 

### Advanced

- use a custom username & password combination when creating the MySQL container. Then use the same username -
  password combination to run this authentication service:

    ```
    docker run -d --network <network-name> --name authentication -p 8080:8080 -e DB_USER=<username>
    --env-file .env -e DB_PASSWORD=<password> ghcr.io/fredrik-philippe-vimbayi/auth-microservice:latest
    ``` 

- run the application on a specific port number by providing a **SERVER_PORT** environmental variable. This can be
  added to the `.env` file (as below) or by using the `-e` flag when running the container.
  ```
  SERVER_PORT=0
  ```
    * A port number of **0** assigns a random port number to the application and several containers of the application
      can be run as a cluster.

### Endpoints

| HTTP | Path          | Information         | Status Code | Response Body |
|------|---------------|---------------------|-------------|---------------|
| POST | /register     | Register a new user | 201         | -             |
| POST | /authenticate | Authenticate a user | 201         | JWT token     |


#### Example of Register / Authenticate Request Body:
```
  {
    "username": "janedoe@mail.com",
    "password": "XXXX"
  }
```
* username must be a valid email

#### Example of Authenticate Response Body:
```
{
"access_token": "a-signed-jwt-token",
"token_type": "Bearer",
"expires_in": 72000
}
```
______________________________________________________________________________________________________________________________________________________________________________       
Contributors: [Fredrik](https://github.com/ErikssonF), [Phillipe](https://github.com/Philippevial),
& [Vimbayi](https://github.com/Vimbayinashe)
