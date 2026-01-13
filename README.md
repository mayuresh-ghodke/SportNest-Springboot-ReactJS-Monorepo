# SportNest - ReactJS & Spring Boot Monorepo

**Full-stack eCommerce Application** with ReactJS frontend and Spring Boot multi-module backend (Customer, Admin, Delivery) with JWT authentication, Razorpay, Twilio, email integration, and Docker support.

## Project Overview
SportNest is a full-fledged eCommerce platform structured with a **multi-module Spring Boot backend** and a **ReactJS frontend** for the customer. Admin and Delivery Person modules use Thymeleaf for server-side rendering.  

## Key highlights:  
- **Customer:** ReactJS SPA with JWT authentication.  
- **Admin & Delivery:** Form-based login using Spring Security.  
- **Shared Library module:** Common models, services, repos, exceptions, and configurations.  
- **Payment & Messaging:** Razorpay and Twilio integrations.  
- **Email notifications:** Using SMTP.  
- **Docker-ready:** Includes MySQL container and backend services containerization.

## Tech Stack: 
- **Backend:** Java, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL  
- **Frontend:** ReactJS (Customer), Thymeleaf (Admin & Delivery)  
- **Payment & Messaging:** Razorpay, Twilio, Email API  
- **Containerization:** Docker, Docker Compose

  
## Environment Variables
Create a `.env` file in the root of the project with the following keys (example values):

```env
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://mysqldb:3306/ecommerce_springboot
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=Root@1234

# Email
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password

# Razorpay
RAZORPAY_KEY_ID=rzp_test_xxxxx
RAZORPAY_KEY_SECRET=xxxxxx

# JWT
CUSTOMER_JWT_SECRET=xxxxxx

# Twilio
TWILIO_ACCOUNT_SID=xxxxxx
TWILIO_AUTH_TOKEN=xxxxxx
TWILIO_PHONE_NUMBER=+91xxxxxxxxxx

# Ports
CUSTOMER_PORT=8020
ADMIN_PORT=8019
DELIVERY_PORT=8021
