import java.util.Scanner;

public class VahanPay {

    static class Customer {
        private String name, email, phoneNumber;
        private double accountBalance;

        public Customer(String name, String email, String phoneNumber, double accountBalance) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.accountBalance = accountBalance;
        }

        public double getAccountBalance() {
            return accountBalance;
        }

        public void deductBalance(double amount) {
            this.accountBalance -= amount;
        }

        public String viewProfile() {
            return String.format("Name: %s, Email: %s, Phone: %s, Balance: %.2f", name, email, phoneNumber, accountBalance);
        }
    }

    static class Vehicle {
        private String model;
        private double rentalPricePerDay;
        private boolean available;

        public Vehicle(String model, double rentalPricePerDay) {
            this.model = model;
            this.rentalPricePerDay = rentalPricePerDay;
            this.available = true;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailability(boolean availability) {
            this.available = availability;
        }

        public double calculateRentalCost(int days) {
            return rentalPricePerDay * days;
        }

        @Override
        public String toString() {
            return String.format("Model: %s, Price per Day: %.2f, Available: %b", model, rentalPricePerDay, available);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Customer customer = new Customer("Aravind", "aravind@gmail.com", "1234567890", 5000.0);
        Vehicle vehicle = new Vehicle("Toyota Innova", 100.0);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View Profile");
            System.out.println("2. View Vehicle Details");
            System.out.println("3. Rent a Vehicle");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Customer Profile:");
                    System.out.println(customer.viewProfile());
                    break;

                case 2:
                    System.out.println("Vehicle Details:");
                    System.out.println(vehicle);
                    break;

                case 3:
                    if (!vehicle.isAvailable()) {
                        System.out.println("Sorry, the vehicle is not available.");
                        break;
                    }
                    System.out.print("Enter number of rental days: ");
                    int days = scanner.nextInt();
                    double rentalCost = vehicle.calculateRentalCost(days);

                    if (customer.getAccountBalance() >= rentalCost) {
                        customer.deductBalance(rentalCost);
                        vehicle.setAvailability(false);
                        System.out.printf("Vehicle rented successfully! Total cost: %.2f%n", rentalCost);
                    } else {
                        System.out.println("Insufficient balance. Rental failed.");
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using VahanPay. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
