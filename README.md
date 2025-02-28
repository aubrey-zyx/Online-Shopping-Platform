# 🛒 Online-Shopping-Platform
This is a **full-stack e-commerce web application** built with **Spring Boot (Backend) and Angular (Frontend)**.
## Prerequisites
Make sure you have the following installed:

- **Java 8** (for the backend)
- **Maven** (for dependency management)
- **MySQL** (for database)
- **Node.js & npm** (for the frontend)
- **Angular CLI** (for running the Angular app)  
  Install globally if you haven’t:  
  ```bash
  npm install -g @angular/cli
## Getting Started
### 1. Start the Backend
1. Build and run the Spring Boot application:
```bash
mvn spring-boot:run
```
2. The backend will be available at: [http://localhost:8080](http://localhost:8080)
### 2. Start the Frontend
1. Navigate to the frontend directory:
```bash
cd frontend
```
2. Install dependencies:
```bash
npm install
```
3. Serve the Angular app:
```bash
ng serve
```
4. The frontend will be available at: [http://localhost:4200](http://localhost:4200)
## API Documentation
You can test the REST APIs using Postman with the provided collection file:  
`OnlineShoppingApp APIs.postman_collection.json`
## Database Setup
1. Crate a MySQL database and tables with the provided SQL script:  
`shopping.sql`
2. Update the `application.properties` file in the backend with your database credentials.
## Features
- **User Authentication**: Register/Login using JWT-based authentication.
- **Product Management**: Browse and view product details.
- **Watchlist & Orders**: Add products to the watchlist, place orders, and track order status.
- **Admin (Seller)**: Manage products, orders, and view analytics.
