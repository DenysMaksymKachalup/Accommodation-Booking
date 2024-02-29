<div style="text-align: center;">

[![image.png](https://i.postimg.cc/RhNS06Gb/image.png)](https://postimg.cc/N2vqJ031)

</div>

Accommodation Booking System is a comprehensive platform tailored to offer users a seamless experience in discovering, booking, and managing accommodations for their travel needs. Our project is dedicated to simplifying the process of finding the perfect stay, whether it's for a vacation, business trip, or any other purpose. With a focus on user satisfaction, our system aims to bridge the gap between travelers and their ideal accommodations, ensuring a hassle-free booking experience right from the comfort of their homes.
## Project Structure👨‍💻

Our Accommodation Booking System is built on a solid foundation, leveraging modern technologies and best practices:

1. **Backend**:
   - Powered by Spring Boot and Hibernate for robust backend development.
   - Seamless data access and manipulation with Spring Data.

2. **Database**:
   - MySQL for efficient data storage and retrieval.
   - Liquibase for streamlined database versioning.

3. **Containerization**:
   - Docker for easy deployment across diverse environments.

4. **API Documentation**:
   - Swagger for clear and comprehensive API documentation.

5. **Authentication and Authorization**:
   - Secure authentication and authorization using JWT.

6. **Payment Integration**:
   - Integration with Stripe for seamless payment processing.

7. **Notifications**:
   - Real-time updates via Telegram API for users and administrators.
### Tech Stack

| Categories               | Technologies                                                     |
|--------------------------|------------------------------------------------------------------|
| Backend                  | Spring Boot <br> Spring Data <br> Hibernate                     |
| Database                 | MySQL                                                            |
| Containerization         | Docker                                                           |
| Mapping                  | MapStruct                                                        |
| Database Version Control | Liquibase                                                        |
| API Documentation        | Swagger                                                          |
| Authentication           | JWT (JSON Web Tokens)                                            |
| Payment Gateway          | Stripe                                                           |
| Notifications            | Telegram API                                                     |

### Domain Models (Entities)

1. **Accommodation**: Represents various types of accommodations available for booking.
2. **User (Customer)**: Stores user information including authentication details.
3. **Booking**: Tracks bookings made by users for accommodations.
4. **Payment**: Handles payment transactions associated with bookings.

### Endpoints

#### - Authentication Controller:
- **POST /register:** Allows users to register a new account.
- **POST /login:** Grants JWT tokens to authenticated users.

#### - User Controller:
- **PUT /users/{id}/role:** Enables users to update their roles.
- **GET /users/me:** Retrieves the profile information for the currently logged-in user.
- **PUT/PATCH /users/me:** Allows users to update their profile information.

#### - Accommodation Controller:
- **POST /accommodations:** Adds new accommodations.
- **GET /accommodations:** Retrieves a list of available accommodations.
- **GET /accommodations/{id}:** Retrieves detailed information about a specific accommodation.
- **PUT/PATCH /accommodations/{id}:** Allows updates to accommodation details.
- **DELETE /accommodations/{id}:** Removes accommodations.

#### - Booking Controller:
- **POST /bookings:** Creates new accommodation bookings.
- **GET /bookings/?user_id=...&status=...:** Retrieves bookings based on user ID and status.
- **GET /bookings/my:** Retrieves bookings for the logged-in user.
- **GET /bookings/{id}:** Provides information about a specific booking.
- **PUT/PATCH /bookings/{id}:** Allows users to update their booking details.
- **DELETE /bookings/{id}:** Cancels bookings.

#### - Payment Controller (Stripe):
- **GET /payments/?user_id=...:** Retrieves payment information for users.
- **POST /payments/:** Initiates payment sessions for booking transactions.
- **GET /payments/success/:** Handles successful payment processing.
- **GET /payments/cancel/:** Manages payment cancellation.

#### - Notifications Service (Telegram):
- Sends notifications about new bookings, cancellations, successful payments, etc.

## Features⚡️

### For Customers👫

1. **Accommodation Discovery**:
    - Explore a wide range of accommodations available for booking.
    - Filter accommodations based on preferences such as location, size, and amenities.

2. **Booking Management**:
    - Easily book accommodations for desired dates.
    - View and manage bookings, including check-in and check-out dates.

3. **User Authentication**:
    - Register as a new user to access booking functionalities.
    - Sign in securely to manage bookings and preferences.

4. **Payment Processing**:
    - Seamlessly process payments for bookings using Stripe integration.
    - Receive notifications about successful payments and booking confirmations.

### For Managers👨‍💼

1. **Accommodation Management**:
    - Add new accommodations to the platform.
    - Update details of existing accommodations, including availability and pricing.
    - Remove accommodations that are no longer available or relevant.

2. **Booking Administration**:
    - View and manage all bookings, including their status and details.
    - Update booking statuses (e.g., "Confirmed", "Cancelled", "Expired").

3. **User Role Management**:
    - Assign roles to users, such as Manager or Customer, to control access to platform functionalities.
    - Grant or revoke permissions based on user roles and responsibilities.

4. **Notification System**:
    - Receive real-time notifications about new bookings, cancellations, and payment statuses.
    - Stay informed about important updates and changes within the system.

## How to use▶️

```sh
# Clone the project from GitHub:
git clone https://github.com/DenysMaksymKachalup/Accommodation-Booking
cd Accommodation-Booking
```

Set environment variables:
Add the necessary environment variables to the .env file.

After that, execute the command...
```sh
docker-compose up
```
Now you can use the app with Swagger:

<a>http://localhost:8081/swagger-ui/index.html</a>

## Contact📞

I'm always open to collaboration and discussion on any topic. Feel free to reach out to me!
- [Email:](mailto:denys.k82@gmail.com)
- [Telegram](https://t.me/denyskachalup)
- [LinkedIn](https://www.linkedin.com/in/denys-kachalup-358430222/)

Thank you for your interest in my Online Book Store project!