import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VahanPay {

    public static class User {
        public String userId;
        public String name;
        public String email;
        public String phoneNumber;
        public double accountBalance;

        public User(String userId, String name, String email, String phoneNumber, double accountBalance) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.accountBalance = accountBalance;
        }

        public void updateProfile(String name, String email, String phoneNumber) {
            if (name != null) {
                this.name = name;
            }
            if (email != null) {
                this.email = email;
            }
            if (phoneNumber != null) {
                this.phoneNumber = phoneNumber;
            }
        }

        public String viewProfile() {
            return "UserID: " + userId + ", Name: " + name + ", Email: " + email + ", Phone Number: " + phoneNumber + ", Account Balance: " + accountBalance;
        }

        public void adjustBalance(double amount) {
            this.accountBalance += amount;
        }
    }

    public static class Customer extends User {
        public List<Booking> rentalHistory;

        public Customer(String userId, String name, String email, String phoneNumber, double accountBalance) {
            super(userId, name, email, phoneNumber, accountBalance);
            this.rentalHistory = new ArrayList<>();
        }

        public void bookVehicle(Vehicle vehicle, BookingModule bookingModule, PaymentModule paymentModule) {
            Booking booking = bookingModule.createBooking(this, vehicle);
            if (booking != null) {
                boolean paymentStatus = paymentModule.processPayment(this, booking.totalCost, booking);
                if (paymentStatus) {
                    rentalHistory.add(booking);
                    System.out.println("Vehicle booked successfully and payment processed.");
                } else {
                    bookingModule.cancelBooking(booking);
                    System.out.println("Payment failed. Booking cancelled.");
                }
            }
        }

        public List<Booking> viewRentalHistory() {
            return rentalHistory;
        }

        public void cancelBooking(int bookingId, BookingModule bookingModule, PaymentModule paymentModule) {
            for (Booking booking : rentalHistory) {
                if (booking.bookingId == bookingId && booking.bookingStatus.equals("Confirmed")) {
                    booking.cancelBooking();
                    bookingModule.cancelBooking(booking);
                    paymentModule.cancelPayment(booking.payment);
                    System.out.println("Booking and payment cancelled successfully.");
                    return;
                }
            }
            System.out.println("Booking not found or already cancelled.");
        }
    }

    public static class Vehicle {
        public String vehicleId;
        public String model;
        public String brand;
        public String type;
        public String registrationNumber;
        public double rentalPricePerDay;
        public boolean availabilityStatus;

        public Vehicle(String vehicleId, String model, String brand, String type, String registrationNumber, double rentalPricePerDay) {
            this.vehicleId = vehicleId;
            this.model = model;
            this.brand = brand;
            this.type = type;
            this.registrationNumber = registrationNumber;
            this.rentalPricePerDay = rentalPricePerDay;
            this.availabilityStatus = true;
        }

        public void updateAvailability(boolean status) {
            this.availabilityStatus = status;
        }

        public double calculateRentalCost(int days) {
            return rentalPricePerDay * days;
        }

        public String viewDetails() {
            return "VehicleID: " + vehicleId + ", Model: " + model + ", Brand: " + brand + ", Type: " + type +
                    ", Registration Number: " + registrationNumber + ", Rental Price Per Day: " + rentalPricePerDay +
                    ", Availability Status: " + availabilityStatus;
        }

        public boolean isAvailable() {
            return availabilityStatus;
        }

        public String getVehicleId() {
            return vehicleId;
        }
    }

    public static class Booking {
        public static int bookingCounter = 1;
        public int bookingId;
        public Customer customer;
        public Vehicle vehicle;
        public LocalDate bookingDate;
        public LocalDate returnDate;
        public double totalCost;
        public String bookingStatus;
        public Payment payment;

        public Booking(Customer customer, Vehicle vehicle, LocalDate bookingDate, LocalDate returnDate) {
            this.bookingId = bookingCounter++;
            this.customer = customer;
            this.vehicle = vehicle;
            this.bookingDate = bookingDate;
            this.returnDate = returnDate;
            this.totalCost = vehicle.calculateRentalCost((int) ChronoUnit.DAYS.between(bookingDate, returnDate));
            this.bookingStatus = "Confirmed";
        }

        public void cancelBooking() {
            this.bookingStatus = "Cancelled";
            this.vehicle.updateAvailability(true);
        }

        public String viewBookingDetails() {
            return "BookingID: " + bookingId + ", Customer: " + customer.viewProfile() +
                    ", Vehicle: " + vehicle.viewDetails() + ", Booking Date: " + bookingDate +
                    ", Return Date: " + returnDate + ", Total Cost: " + totalCost +
                    ", Booking Status: " + bookingStatus + ", Payment Status: " + (payment != null ? payment.paymentStatus : "N/A");
        }
    }

    public static class BookingModule {
        private List<Booking> bookings;

        public BookingModule() {
            this.bookings = new ArrayList<>();
        }

        public Booking createBooking(Customer customer, Vehicle vehicle) {
            if (!vehicle.isAvailable()) {
                System.out.println("Vehicle " + vehicle.getVehicleId() + " is not available.");
                return null;
            }

            LocalDate bookingDate = LocalDate.now();
            LocalDate returnDate = bookingDate.plusDays(7);
            Booking booking = new Booking(customer, vehicle, bookingDate, returnDate);
            bookings.add(booking);
            vehicle.updateAvailability(false);
            return booking;
        }

        public void cancelBooking(Booking booking) {
            bookings.remove(booking);
        }

        public List<Booking> viewAllBookings() {
            return bookings;
        }
    }

    public static class Payment {
        public static int paymentCounter = 1;
        public int paymentId;
        public String userId;
        public double amount;
        public LocalDate paymentDate;
        public String paymentStatus;
        public Booking booking;

        public Payment(String userId, double amount, Booking booking) {
            this.paymentId = paymentCounter++;
            this.userId = userId;
            this.amount = amount;
            this.paymentDate = LocalDate.now();
            this.paymentStatus = "Pending";
            this.booking = booking;
        }

        public String viewPaymentDetails() {
            return "PaymentID: " + paymentId + ", UserID: " + userId + ", Amount: " + amount +
                    ", Payment Date: " + paymentDate + ", Payment Status: " + paymentStatus;
        }

        public void markAsSuccess() {
            this.paymentStatus = "Success";
        }

        public void markAsCancelled() {
            this.paymentStatus = "Cancelled";
        }
    }

    public static class PaymentModule {
        private List<Payment> payments;

        public PaymentModule() {
            this.payments = new ArrayList<>();
        }

        public boolean processPayment(Customer customer, double amount, Booking booking) {
            Payment payment = new Payment(customer.userId, amount, booking);
            payments.add(payment);
            if (customer.accountBalance >= amount) {
                customer.adjustBalance(-amount);
                payment.markAsSuccess();
                booking.payment = payment;
                return true;
            } else {
                System.out.println("Insufficient balance. Payment is pending.");
                booking.payment = payment;
                return false;
            }
        }

        public void cancelPayment(Payment payment) {
            if (payment != null) {
                payment.markAsCancelled();
                payment.booking.customer.adjustBalance(payment.amount);
                System.out.println("Payment " + payment.paymentId + " cancelled and amount refunded.");
            }
        }

        public List<Payment> viewAllPayments() {
            return payments;
        }
    }

    public static void printTable(List<String[]> rows) {
        int[] maxLengths = new int[rows.get(0).length];
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                maxLengths[i] = Math.max(maxLengths[i], row[i].length());
            }
        }
        printBorder(maxLengths);

        for (String[] row : rows) {
            printRow(row, maxLengths);
        }
        
        printBorder(maxLengths);
    }

    public static void printRow(String[] row, int[] maxLengths) {
        StringBuilder formatBuilder = new StringBuilder("|");
        for (int i = 0; i < row.length; i++) {
            formatBuilder.append(" %-" + maxLengths[i] + "s |");
        }
        System.out.printf(formatBuilder.toString() + "%n", (Object[]) row);
        System.out.println();
    }

    public static void printBorder(int[] maxLengths) {
        StringBuilder borderBuilder = new StringBuilder("+");
        for (int length : maxLengths) {
            borderBuilder.append("-".repeat(length + 2)).append("+");
        }
        System.out.println(borderBuilder);
    }
    
    public static final String NAME = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$";
    public static final String PHONE = "^\\d{10}$";
    public static final String EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String AMOUNT = "^\\d+(\\.\\d+)?$";

   
    public static boolean validateInput(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void main(String[] args) {
        BookingModule bookingModule = new BookingModule();
        PaymentModule paymentModule = new PaymentModule();
        Scanner scanner = new Scanner(System.in);

        Map<String, Customer> customers = new HashMap<>();
        Map<String, Vehicle> vehicles = new HashMap<>();

        Vehicle car1 = new Vehicle("1", "Model S", "Tesla", "Car", "ABC123", 100);
        Vehicle car2 = new Vehicle("2", "Mustang", "Ford", "Car", "XYZ789", 80);
        vehicles.put(car1.getVehicleId(), car1);
        vehicles.put(car2.getVehicleId(), car2);
        System.out.println("\n\t\t\t\t\t\t-------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t>> Welcome to VahanPay >> Make travel safe and fast >>");
        System.out.println("\t\t\t\t\t\t-------------------------------------------------------");
        while (true) {
            System.out.println();
            System.out.println("1. Register Customer");
            System.out.println("2. View Profile");
            System.out.println("3. Book Vehicle");
            System.out.println("4. View Rental History");
            System.out.println("5. Cancel Booking");
            System.out.println("6. View All Bookings");
            System.out.println("7. View All Payments");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (option) {
            case 1:
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter Name: ");
                String name = scanner.nextLine();
                if (!validateInput(name, NAME)) {
                    System.out.println("Invalid name format. Please enter a valid name.");
                    break;
                }
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                if (!validateInput(email, EMAIL)) {
                    System.out.println("Invalid email format. Please enter a valid email.");
                    break;
                }
                System.out.print("Enter Phone Number: ");
                String phoneNumber = scanner.nextLine();
                if (!validateInput(phoneNumber, PHONE)) {
                    System.out.println("Invalid phone number format. Please enter a valid phone number.");
                    break;
                }
                System.out.print("Enter Account Balance: ");
                String balanceInput = scanner.nextLine();
                if (!validateInput(balanceInput, AMOUNT)) {
                    System.out.println("Invalid amount format. Please enter a valid amount.");
                    break;
                }
                double balance = Double.parseDouble(balanceInput);
                Customer customer = new Customer(userId, name, email, phoneNumber, balance);
                customers.put(userId, customer);
                System.out.println("Customer registered successfully.");
                break;

                case 2:
                    System.out.print("Enter User ID: ");
                    String viewUserId = scanner.nextLine();
                    System.out.println();
                    Customer viewCustomer = customers.get(viewUserId);
                    if (viewCustomer != null) {
                        List<String[]> profileRows = new ArrayList<>();
                        profileRows.add(new String[]{"UserID", "Name", "Email", "Phone Number", "Account Balance"});
                        profileRows.add(new String[]{
                                viewCustomer.userId,
                                viewCustomer.name,
                                viewCustomer.email,
                                viewCustomer.phoneNumber,
                                String.valueOf(viewCustomer.accountBalance)
                        });
                        printTable(profileRows);
                    } else {
                        System.out.println("Customer not found.");
                    }
                    break;


                case 3:
                    System.out.print("Enter User ID: ");
                    String bookUserId = scanner.nextLine();
                    System.out.println();
                    Customer bookCustomer = customers.get(bookUserId);
                    if (bookCustomer == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    System.out.println("Available Vehicles:");
                    System.out.println();
                    for (Vehicle vehicle : vehicles.values()) {
                        if (vehicle.isAvailable()) {
                            System.out.println(vehicle.viewDetails());
                        }
                    }
                    System.out.println();
                    System.out.print("Enter Vehicle ID to book: ");
                    String vehicleId = scanner.nextLine();
                    Vehicle vehicleToBook = vehicles.get(vehicleId);
                    if (vehicleToBook == null || !vehicleToBook.isAvailable()) {
                        System.out.println("\nVehicle not available.");
                    } else {
                        bookCustomer.bookVehicle(vehicleToBook, bookingModule, paymentModule);
                    }
                    break;

                case 4:
                    System.out.print("Enter User ID: ");
                    String historyUserId = scanner.nextLine();
                    System.out.println();
                    Customer historyCustomer = customers.get(historyUserId);
                    if (historyCustomer != null) {
                        List<Booking> rentalHistory = historyCustomer.viewRentalHistory();
                        if (rentalHistory.isEmpty()) {
                            System.out.println("No rental history found.");
                        } else {
                            List<String[]> historyRows = new ArrayList<>();
                            historyRows.add(new String[]{"BookingID", "Vehicle", "Booking Date", "Return Date", "Total Cost", "Booking Status", "Payment Status"});
                            for (Booking booking : rentalHistory) {
                                historyRows.add(new String[]{
                                        String.valueOf(booking.bookingId),
                                        booking.vehicle.model,
                                        booking.bookingDate.toString(),
                                        booking.returnDate.toString(),
                                        String.valueOf(booking.totalCost),
                                        booking.bookingStatus,
                                        booking.payment != null ? booking.payment.paymentStatus : "N/A"
                                });
                            }
                            printTable(historyRows);
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter User ID: ");
                    String cancelUserId = scanner.nextLine();
                    System.out.println();
                    Customer cancelCustomer = customers.get(cancelUserId);
                    if (cancelCustomer == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    System.out.print("Enter Booking ID to cancel: ");
                    int bookingId = scanner.nextInt();
                    cancelCustomer.cancelBooking(bookingId, bookingModule, paymentModule);
                    break;

                case 6:
                    List<Booking> allBookings = bookingModule.viewAllBookings();
                    if (allBookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        List<String[]> bookingRows = new ArrayList<>();
                        bookingRows.add(new String[]{"BookingID", "Customer", "Vehicle", "Booking Date", "Return Date", "Total Cost", "Booking Status", "Payment Status"});
                        for (Booking booking : allBookings) {
                            bookingRows.add(new String[]{
                                    String.valueOf(booking.bookingId),
                                    booking.customer.name,
                                    booking.vehicle.model,
                                    booking.bookingDate.toString(),
                                    booking.returnDate.toString(),
                                    String.valueOf(booking.totalCost),
                                    booking.bookingStatus,
                                    booking.payment != null ? booking.payment.paymentStatus : "N/A"
                            });
                        }
                        printTable(bookingRows);
                    }
                    break;

                case 7:
                    List<Payment> allPayments = paymentModule.viewAllPayments();
                    if (allPayments.isEmpty()) {
                        System.out.println("No payments found.");
                    } else {
                        List<String[]> paymentRows = new ArrayList<>();
                        paymentRows.add(new String[]{"PaymentID", "UserID", "Amount", "Payment Date", "Payment Status"});
                        for (Payment payment : allPayments) {
                            paymentRows.add(new String[]{
                                    String.valueOf(payment.paymentId),
                                    payment.userId,
                                    String.valueOf(payment.amount),
                                    payment.paymentDate.toString(),
                                    payment.paymentStatus
                            });
                        }
                        printTable(paymentRows);
                    }
                    break;

                case 8:
                	System.out.println("\n\t\t\t\t\t\t----------------------------------------------------------");
                    System.out.println("\t\t\t\t\t\t<< 🙏 Thank you for choosing us... Welcome again... 😊 >>");
                    System.out.println("\t\t\t\t\t\t----------------------------------------------------------");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option, Please try again... ");
            }
        }
    }
}

