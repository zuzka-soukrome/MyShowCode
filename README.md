# My showcase study - E-shop

## About me:
Name: Zuzana Tumova  
Email: zuzkatum@gmail.com  
LinkedIn: https://www.linkedin.com/in/zuzana-t%C5%AFmov%C3%A1-74ab8a105/

## About this project:
* Java 11
* Spring Boot 2
* Maven 3
* MySQL

## Basic functionality:
* This project provides basic REST API for running e-shop
* It consists of two mySQL databases:
    * Products
    * Orders
* You can add new products, update them or delete them from the database
* When creating an order, required quantities of products will be reserved (blocked) in the system
* If the e-shop doesn't have sufficient products in stock, it will return the list of missing items and the missing quantity (in this case no products are reserved)
* The complete order is reserved in the system for 30 minutes
* In this time period, client can pay the order, otherwise after 30 minutes the order is cancelled and all reserved items are returned back to available stock



## How to run:
In order to run this app you need to run docker containers. These containers are defined in `./docker-compose.yml` file.  
`$ docker-compose up`

* Swagger UI REST API description: http://localhost:8001/ (running in Docker container)