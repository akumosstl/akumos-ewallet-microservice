## Spring-boot Microservices environment

Spring-boot microservices environment example.

### Stack

- java 11
- spring-boot
- service registry (eureka)
- api gateway (spring cloud gateway)
- kafka
- kafdrop (kafka UI)
- openSearch * (not implemented yet, need more RAM locally to implement)
- MySql
- Maven
- Docker
- Windows SO
- Postman

### Compile

Navigate till root folder project and run:

```
mvn clean install

```

### Run Locally

Navigate till root folder project and run:


```
start_all_local_microservices.bat

```

### Run with docker

Navigate till root folder project and run:

```
start_all_docker_microservices.bat

```
### URIs

Eureka Server:

[http://localhost:8761](http://localhost:8761)

Kafdrop UI:

[http://localhost:9000](http://localhost:9000)

Swagger:

[http://localhost:9191/auth-api/swagger-ui.html](http://localhost:9191/auth-api/swagger-ui.html)

[http://localhost:8282/account-api/swagger-ui.html](http://localhost:8282/account-api/swagger-ui.html)

[http://localhost:8280/transaction-api/swagger-ui.html](http://localhost:8280/transaction-api/swagger-ui.html)

[http://localhost:8284/event-api/swagger-ui.html](http://localhost:8284/event-api/swagger-ui.html)

## Using

Open the Postman application and import the collection from:

```
./postman/java-cloud.postman_collection.json

```

Or call endpoints through any http client following this bellow roadmap:

**Signup**
```
http://localhost:8180/auth-api/v1/auth/signup

```

##To obtain access_token,

**Make an HTTP POST Request to**
```
http://localhost:8180/auth-api/v1/auth/signin
```
```
{
  "username": "apedrina",
  "password": "Xp@800823"
}
```

##To create an account on the application,

**Make an HTTP POST Request to**
```
http://localhost:8180/account-api/v1/account/create
```
```
{
    "surname":"Ambrosia",
    "firstName":"Mariah",
    "lastName":"Angelina",
    "email":"angelina.mariah@gmail.com",
    "phone":"14997741213"
}
```

##TO FUND WALLET ACCOUNT

```
Post the below request body to http://localhost:8180/transaction-api/v1/wallet/fund
```
```
{
    "walletAddress": "b6451434-b25b-4a78-907c-43322f792cf6",
    "amount": 20000
}
```

##TO WITHDRAW FROM WALLET ACCOUNT

```
Post the below request body to http://localhost:8180/transaction-api/v1/wallet/withdraw
```
```
{
    "walletAddress": "3f56d52f-8b7f-4e27-947c-79f35c1114e6",
    "amount": 6000
}
```
##TO TRANSFER FUNDS FROM ONE WALLET TO WALLET

```
Post the below request body to http://localhost:8180/transaction-api/v1/wallet/transfer
```
```
{
    "sourceWalletAddress": "b6451434-b25b-4a78-907c-43322f792cf6",
    "amount": 15000,
    "destinationWalletAddress":"4b0a0cf7-b605-421e-8a21-fcec645926b0"
}
```


