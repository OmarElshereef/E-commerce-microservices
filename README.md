# 🛒 E-Commerce Microservices Platform

## 📖 Overview
This project is an **intermediate-level e-commerce platform** built using a **polyglot microservices architecture**. It demonstrates **JWT-based authentication**, **distributed services**, **asynchronous messaging**, and a modern **Angular frontend**.  

Core features:
- User **registration & login** with JWT
- Browse products, add to **cart** or **wishlist**
- Place orders & process payments securely
- **Cart expiration rules** with Redis TTL logic
- **Asynchronous communication** with RabbitMQ
- **Service discovery & routing** via Eureka + API Gateway

---

## 🏗 Architecture

### **Backend Services**
- **Auth Service (Spring Boot + PostgreSQL)**
  - Handles user registration/login
  - Issues JWTs for authentication
- **Product Service (Spring Boot + MySQL)**
  - Manages products, categories, stock
- **Order Service (Spring Boot + PostgreSQL)**
  - Manages order lifecycle
  - Publishes/consumes RabbitMQ events
- **Cart Service (ASP.NET Core + Redis)**
  - Manages carts with TTL rules:
    - Default: 24h  
    - On order: 7 days  
    - On payment: indefinite
- **Payment Service (ASP.NET Core + SQL Server)**
  - Validates JWT before payments
  - Publishes payment events via RabbitMQ

### **Infrastructure**
- **API Gateway (Spring Boot)** → JWT validation, routing  
- **Eureka Service Discovery (Spring Boot)** → service registry  
- **RabbitMQ** → async event bus for order, payment, and stock updates  

### **Frontend**
- **Angular** SPA
- JWT-based login/register flow
- Product browsing, cart, wishlist, checkout

---

## 📊 System Diagram

```mermaid
flowchart TD
    User["👤 User (Browser)"]

    subgraph UI["Angular Frontend"]
        Frontend["E-Commerce SPA"]
    end

    subgraph API["API Gateway (Spring Boot)"]
    end

    subgraph DISC["Discovery Service (Eureka)"]
    end

    subgraph JAVA["Spring Boot Services"]
        Product["Product Service (MySQL)"]
        Order["Order Service (PostgreSQL)"]
        Auth["Auth Service (PostgreSQL, JWT)"]
    end

    subgraph DOTNET[".NET Core Services"]
        Cart["Cart Service (Redis, TTL logic)"]
        Payment["Payment Service (SQL Server)"]
    end

    subgraph MQ["RabbitMQ Event Bus"]
    end

    User --> Frontend
    Frontend --> API

    API --> Auth
    API --> Product
    API --> Order
    API --> Cart
    API --> Payment

    Product <--> DISC
    Order <--> DISC
    Auth <--> DISC
    Cart <--> DISC
    Payment <--> DISC

    Order -- Publish --> MQ
    Payment -- Publish --> MQ
    MQ -- Consume --> Product
    MQ -- Consume --> Order
    MQ -- Consume --> Payment
