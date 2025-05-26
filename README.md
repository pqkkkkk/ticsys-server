# TicSys-server - server-side implementation for TicSys. Using modular monoliths approach
## Introduction
Side project implemented business logic of the event ticketing system. Check [TicSys-client]{https://github.com/pqkkkkk/TicSys-client}
## Project structure
```
ticsys-server/
    ├── ticsys/
        ├── src/
        │    ├── main/
        │    │    ├── java/com/example/ticsys/
        │    │    │    ├── app/
        │    │    │    │    ├── account/
        │    │    │    │    ├── comment/
        │    │    │    │    ├── event/
        │    │    │    │    ├── notification/
        │    │    │    │    ├── order/
        │    │    │    │    ├── promotion/
        │    │    │    ├── external_service/
        │    │    │    ├── config/
        │    │    ├── resources/
        ├── [README.md]
        ├── .gitignore
```
## Technologies
- Spring Boot
- Spring Security: implement authentication and authorization by using JWT and RBAC
- Cloudinary: cloud storage service
- SQL Server: database for this project
- Kafka: use to communicate with TSBank to process the order payment.

## Features
- Customer
    - Buy ticket of event
    - Manage ticket list
    - Ask question about event
    - Online payment
    - Link account to TSBank account
- Organizer
    - Create event
    - Manage own event
    - Manage promotions of event
    - View event report (statistics about revenue, sold ticket quantity, customer quantity)
    - Reply question about event of the customer
- Admin
    - Manage users
    - Manage orders
    - View event report (statistics about revenue, sold ticket quantity, customer quantity) of entire system
