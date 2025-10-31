# PromoQuoter Microservice

PromoQuoter is a Spring Boot microservice that calculates cart pricing, applies promotions, reserves inventory, and confirms orders with concurrency-safe logic and idempotency support.

---

## Getting Started

### Prerequisites
- Java 22
- Maven
---

## Run with Maven
```bash
mvn spring-boot:run
```

## Run with Gradle
```bash
./gradlew bootRun
```

The service will start on:  
http://localhost:8081

---

## Sample Data Setup

### 1. Create Products
```bash
curl --location 'http://localhost:8082/api/v1/products' \
--header 'Content-Type: application/json' \
--data '[
    {
        "name": "Laptop",
        "category": "ELECTRONICS",
        "price": 1000.00,
        "stock": 10
    },
    {
        "name": "Notebook",
        "category": "ELECTRONICS",
        "price": 50.00,
        "stock": 100
    },
    {
        "name": "T-Shirt",
        "category": "CLOTHING",
        "price": 799.00,
        "stock": 100
    }
]'
```

### 2. Create Promotions
```bash
curl --location 'http://localhost:8082/api/v1/promotions' \
--header 'Content-Type: application/json' \
--data '[
    {
        "type": "PERCENT_OFF_CATEGORY",
        "priority": 1,
        "configJson": "{\"category\":\"ELECTRONICS\",\"percent\":10}"
    },
    {
        "type": "BUY_X_GET_Y",
        "priority": 1,
        "configJson": "{\"productId\":\"48747518-ac51-438e-a3ba-11e85a9c2aa6\",\"buyQty\":2,\"freeQty\":1}"
    }
]'
```

---

## Cart Operations

### 3. Quote Cart
```bash
curl --location 'http://localhost:8081/api/v1/cart/quote' --header 'Content-Type: application/json' --data '{
  "items": [
    {
      "productId": "7377312d-0181-4413-9acd-5256ada0b277",
      "quantity": 7
    }
  ],
  "customerSegment": "REGULAR"
}'
```

### 4. Confirm Order
```bash
curl --location 'http://localhost:8081/api/v1/cart/confirm' --header 'Content-Type: application/json' --header 'Idempotency-Key: test-key-12345' --data '{
  "items": [
    {
      "productId": "7377312d-0181-4413-9acd-5256ada0b277",
      "quantity": 1
    }
  ]
}'
```

---

## Key Features
- Concurrency-safe order confirmation  
- Idempotent API operations  
- Dynamic promotion application  
- Inventory reservation and rollback  
- Modular and extensible architecture

---

## Tech Stack
- Spring Boot (Java 22)
- Spring Data JPA
- H2
- Lombok
- Maven 
