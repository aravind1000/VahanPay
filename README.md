# VahanPay

VahanPay is a vehicle rental management system that allows customers to register, book vehicles, view their rental history, and manage bookings and payments. The system includes modules for booking and payment management, along with user and vehicle management.

## Features

- **Customer Registration:** Register new customers with details such as user ID, name, email, phone number, and account balance.
- **Profile Management:** View and update customer profiles.
- **Vehicle Booking:** Customers can book available vehicles for a rental period.
- **Rental History:** View past rental bookings and their details.
- **Booking Cancellation:** Cancel existing bookings and process refunds.
- **Admin Functions:** View all bookings and payments.
- **Validation:** Input validation for customer details and payment amounts.

## Classes

### User

Represents a user of the system. Contains methods to update the profile, view profile details, and adjust the account balance.

### Customer (extends User)

Represents a customer. Contains methods to book a vehicle, view rental history, and cancel bookings.

### Vehicle

Represents a vehicle available for rent. Contains methods to view details, check availability, and calculate rental costs.

### Booking

Represents a booking of a vehicle by a customer. Contains methods to view booking details and cancel the booking.

### BookingModule

Manages bookings. Contains methods to create and cancel bookings and view all bookings.

### Payment

Represents a payment made by a customer. Contains methods to view payment details, mark payment as successful or cancelled.

### PaymentModule

Manages payments. Contains methods to process and cancel payments and view all payments.

### VahanPay

Main class that contains the application logic and menu for user interaction.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later
- An IDE or text editor to write and run Java code

### Running the Application

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/vahanpay.git
    ```

2. Navigate to the project directory:

    ```sh
    cd vahanpay
    ```

3. Compile the Java files:

    ```sh
    javac VahanPay.java
    ```

4. Run the application:

    ```sh
    java VahanPay
    ```

## Usage

Upon running the application, you will be presented with a menu with the following options:

1. **Register Customer:** Register a new customer by entering the required details.
2. **View Profile:** View the profile of a registered customer by entering their user ID.
3. **Book Vehicle:** Book an available vehicle for a rental period.
4. **View Rental History:** View the rental history of a registered customer.
5. **Cancel Booking:** Cancel an existing booking by entering the booking ID.
6. **View All Bookings:** View all bookings made in the system.
7. **View All Payments:** View all payments made in the system.
8. **Exit:** Exit the application.
