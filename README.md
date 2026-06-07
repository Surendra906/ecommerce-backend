# E-Commerce Backend

## Overview

A backend application built using Java Spring Boot and MySQL that provides core e-commerce functionalities including product management, shopping cart operations, order processing, and payment simulation. The project follows a layered architecture and RESTful API design principles.

## Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
* REST APIs
* Lombok
* Hibernate

## Features

### Product Catalog

* Add products
* View products
* Update product details
* Delete products
* Search products by category
* Pagination and sorting support

### Shopping Cart

* Add products to cart
* Update product quantity
* Remove products from cart
* View cart items
* Clear cart

### Order Management

* Place orders
* View order details
* View all orders
* Cancel orders
* Track order status

### Payment Module

* Mock payment processing
* Payment status tracking
* Order status updates based on payment result

## Database Entities

* User
* Product
* Cart
* CartItem
* Order
* OrderItem
* Payment

## API Endpoints

### Product APIs

| Method | Endpoint           | Description       |
| ------ | ------------------ | ----------------- |
| POST   | /api/products      | Add Product       |
| GET    | /api/products      | Get All Products  |
| GET    | /api/products/{id} | Get Product By ID |
| PUT    | /api/products/{id} | Update Product    |
| DELETE | /api/products/{id} | Delete Product    |

### Cart APIs

| Method | Endpoint              | Description         |
| ------ | --------------------- | ------------------- |
| POST   | /api/cart/add         | Add Product To Cart |
| PUT    | /api/cart/update      | Update Quantity     |
| DELETE | /api/cart/remove/{id} | Remove Product      |
| GET    | /api/cart             | View Cart           |
| DELETE | /api/cart/clear       | Clear Cart          |

### Order APIs

| Method | Endpoint                | Description         |
| ------ | ----------------------- | ------------------- |
| POST   | /api/orders             | Place Order         |
| GET    | /api/orders             | Get All Orders      |
| GET    | /api/orders/{id}        | Get Order By ID     |
| PUT    | /api/orders/{id}/status | Update Order Status |
| DELETE | /api/orders/{id}        | Cancel Order        |

### Payment APIs

| Method | Endpoint              | Description     |
| ------ | --------------------- | --------------- |
| POST   | /api/payments/process | Process Payment |
| GET    | /api/payments/{id}    | Payment Details |

## Project Structure

src/main/java
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config



### . Run Application

```bash
mvn spring-boot:run
```

Application will start on:

```text
http://localhost:8080
```

## Future Enhancements

* JWT Authentication
* Spring Security
* Swagger API Documentation
* Docker Containerization
* Unit Testing with JUnit
* Payment Gateway Integration

## Author

Surendra Mode
