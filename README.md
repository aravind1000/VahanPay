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
