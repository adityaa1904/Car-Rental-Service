import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> activeRentals;
    private List<Rental> rentalHistory;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        activeRentals = new ArrayList<>();
        rentalHistory = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            Rental newRental = new Rental(car, customer, days);

            activeRentals.add(newRental);
            rentalHistory.add(newRental);

        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();

        Rental rentalToRemove = null;
        for (Rental rental : activeRentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            activeRentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    // ✅ View all cars (available + rented)
    public void viewAllCars() {
        System.out.println("\n== All Cars ==\n");
        if (cars.isEmpty()) {
            System.out.println("No cars available in the system.");
            return;
        }

        for (Car car : cars) {
            String status = car.isAvailable() ? "Available ✅" : "Rented ❌";
            System.out.println(
                car.getCarId() + " - " + car.getBrand() + " " + car.getModel()
                + " | Price/Day: $" + car.getBasePricePerDay()
                + " | Status: " + status
            );
        }
    }

    // ✅ Rental history
    public void viewRentalHistory() {
        System.out.println("\n== Rental History ==\n");
        if (rentalHistory.isEmpty()) {
            System.out.println("No rentals have been made yet.");
            return;
        }

        int count = 1;
        for (Rental rental : rentalHistory) {
            System.out.println("Rental #" + count++);
            System.out.println("Customer: " + rental.getCustomer().getName()
                    + " (ID: " + rental.getCustomer().getCustomerId() + ")");
            System.out.println("Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel()
                    + " (ID: " + rental.getCar().getCarId() + ")");
            System.out.println("Days: " + rental.getDays());
            System.out.printf("Total Paid: $%.2f%n", rental.getTotalCost());
            System.out.println("-----------------------------");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View All Cars");
            System.out.println("4. View Rental History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Cars:");
                boolean anyCarAvailable = false;
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        anyCarAvailable = true;
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                if (!anyCarAvailable) {
                    System.out.println("No cars available right now.");
                    continue;
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays;
                try {
                    rentalDays = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid rental days input.");
                    scanner.nextLine();
                    continue;
                }

                if (rentalDays <= 0) {
                    System.out.println("Rental days must be greater than 0.");
                    continue;
                }

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);

                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully ✅");
                    } else {
                        System.out.println("\nRental canceled ❌");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available.");
                }

            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    returnCar(carToReturn);
                    System.out.println("Car returned successfully ✅");
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }

            } else if (choice == 3) {
                viewAllCars();

            } else if (choice == 4) {
                viewRentalHistory();

            } else if (choice == 5) {
                break;

            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
        scanner.close();
    }
}
