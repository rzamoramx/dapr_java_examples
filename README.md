# Dapr Java examples
Code snippets, hows, etc. for Dapr + Java

## Overview for simple orders system example

This example demonstrate how to use most common Dapr components (State store and pubsub) with Java and show benefits (simplification) of building microservices on this runtime. There are 3 services:

- Purchase agent: Receives orders and send it using pubsub (Dapr component) to fulfillment service, also it retrieves the order from the order state service. 
- Order state: Save and retrieve orders, using state store (Dapr component).
- Fulfillment: It receives the order from pubsub, process it and save it.

## Flows

### Create an order

1. The purchase agent receives an order (responds with a unique ID) and send it to the fulfillment service using pubsub. (Async)
2. The fulfillment service receives the order from pubsub and process it, then call State order service to save it. (gRPC)
3. The order state service receives the order and save it. uses State store component (Redis). 

### Get an order

1. The purchase agent receives a request to get an order and call the order state service to get it. (gRPC)
2. The order state service receives the request and get the order from state store (Redis) and return it.

## Dependencies

- [Docker] It is for Dapr
- [Dapr] Running locally
- [Java 19] Java 19 JDK installed
- [Maven] :)
- [Http client] A http client (Postman, Insomnia, Curl, etc.)

## How to install

#### Note: you need to have initialized Dapr before (dapr init command)

    mvn clean install
    
## How to run

One terminal for each service


Address to /path/to/cloned/repo/dapr-java-examples/orderstate/target

    dapr run --app-id orderstate --app-protocol grpc --app-port 50051 -- java -jar orderstate-1.0-SNAPSHOT.jar -p 50051

Address to /path/to/cloned/repo/dapr-java-examples/fulfillment/target
    
    dapr run --app-id fulfillment --app-protocol http --app-port 8083 -- java -jar fulfillment-1.0-SNAPSHOT.jar

Address to /path/to/cloned/repo/dapr-java-examples/purchaseagent/target
    
    dapr run --app-id purchaseagent --app-protocol http --app-port 8081 -- java -jar purchaseagent-1.0-SNAPSHOT.jar


## How to test

#### To create an order:

Do a POST to http://localhost:8081/orders/ with the following body:

```json
{
  "client": "Rodri oh baby oh baby",
  "product": "Foo bar",
  "quantity": 1000,
  "price": 100000999.00
}
```

Response:

```json
{
  "status": "OK",
  "message": "Order received",
  "orderId": "01b60731-fd4c-4f5a-b88b-0ef818af538b"
}
```

#### To get an order:

Do a GET to http://localhost:8081/orders/{orderId}

