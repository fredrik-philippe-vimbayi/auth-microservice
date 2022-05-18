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
1. Create a volume
   ```
   docker volume create <volume-name>
   ```
2. Create a network
    ```
    docker network create <network-name>
    ```
3. Run a MySQL database container on the network
   ```
   docker run -d --name authdb --network <network-name> -v <volume-name>:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root
   -e 'MYSQL_ROOT_HOST=%' -e MYSQL_DATABASE=test -e MYSQL_USER=user -e MYSQL_PASSWORD=password -p 3308:3306 mysql:8.0.29

   ```
4. Run a Consul container on the network
    ```
    docker run -d -p 8500:8500 -p 8600:8600/udp --name=consul --network <network-name> consul
    agent -server -ui -node=server-1 -bootstrap-expect=1 -client='0.0.0.0'
    ```
5. Run a RabbitMQ container on the network
   ```
   docker run -d --name rabbit --network <network-name> -p 15672:15672 -p 5672:5672 rabbitmq:3-management
   ``` 
6. Get an RSA public - private key pair (from us)
    ```
    PUBLIC_KEY=your-public-key
    PRIVATE_KEY=your-private-key
    ```
7. Add configuration to Consul config
   * Open Consul's UI on http://localhost:8500
   * Create a new `.yml` file in the **Key/Value** sub-menu with the following folder structure    
     **/config/authentication/data**
   * Save your configurations
   ```
   spring:
     cloud:
       consul:
         discovery:
           register: true
           prefer-ip-address: true
           instance-id: ${spring.application.name}:${spring.cloud.client.hostname}:${random.int[1,999999]}
     host: consul
     jpa:
       hibernate:
         ddl-auto: update
     datasource:
       url: jdbc:mysql://authdb:3306/test?allowPublicKeyRetrieval=true&useSSL=false
       username: user
       password: password
     rabbitmq:
       host: rabbit
       port: 5672
   server:
     port: 8080
     error:
       include-message: always
   key:
     private: your-private-key
   ```

8. Run the microservice on a fixed port number. Port `8080` is exposed by default.
   ```
   docker run -d --network <network-name> --name authentication -p 8080:8080
   ghcr.io/fredrik-philippe-vimbayi/auth-microservice:latest
   ```

### Advanced

Edit and customize settings in Consul Config:

* server port
* datasource url
* datasource username
* datasource password

**Note**: A server port number of **0** assigns a random port number to the application and several containers of the
microservice can be run as a cluster.

### Endpoints

| HTTP | Path          | Information         | Status Code | Response Body |
|------|---------------|---------------------|-------------|---------------|
| POST | /register     | Register a new user | 201         | -             |
| POST | /authenticate | Authenticate a user | 200         | JWT token     |

#### Example of a Register / Authenticate Request Body:
```
  {
    "username": "janedoe@mail.com",
    "password": "XXXX"
  }
```

**Note:** username must be a valid email

#### Example of an Authenticate Response Body:

```
  {
    "access_token": "a-signed-encoded-jwt-token-made",
    "token_type": "Bearer",
    "expires_in": 72000
  }
```

### Exchange Configuration

A message containing the unique username is published when a new user is registered.

Exchange name: **auth_message_exchange**   
Routing key: **user.new**
______________________________________________________________________________________________________________________________________________________________________________       
Contributors: [Fredrik](https://github.com/ErikssonF), [Philippe](https://github.com/Philippevial),
& [Vimbayi](https://github.com/Vimbayinashe)
