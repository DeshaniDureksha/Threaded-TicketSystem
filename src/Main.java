import java.util.Scanner;

public class Main {
    // Tracks if the ticketing system is currently running
    private static boolean systemRunning = false;
    // Threads for vendors and customers
    private static Thread vendorThread1, vendorThread2, customerThread1, customerThread2;
    // Vendor objects
    private static Vendor vendor1, vendor2;
    // Customer objects
    private static Customer customer1, customer2;

    public static void main(String[] args) {
        // Scanner to get user input
        Scanner scanner = new Scanner(System.in);
        // Configuration object to store system settings
        Configuration config = new Configuration();
        // Filename to store config info
        String fileName = "config.json";

        // Welcome message
        System.out.println("Welcome to the Real-Time Ticketing System CLI!");

        // Configure initial settings by asking the user
        configureSettings(scanner, config);

        // Main loop to handle user interaction
        while (true) {
            // If the system isn't running, show options to user
            if (!systemRunning) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Save Configuration");
                System.out.println("2. Load Configuration");
                System.out.println("3. Start Ticketing System");
                System.out.println("4. Stop Ticketing System");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
            }

            // Get the user's choice while validating it within the range 1-5
            int choice = getValidChoice(scanner, 1, 5);

            // Only execute when system isn't running OR if the user chose the stop option (4)
            if (!systemRunning || choice == 4) {
                switch (choice) {
                    // Save configuration option
                    case 1 -> saveConfiguration(config, fileName);
                    // Load configuration option
                    case 2 -> config = loadConfiguration(fileName);
                    // Start system option
                    case 3 -> startSystem(config);
                    // Stop system option
                    case 4 -> stopSystem();
                    // Exit system option
                    case 5 -> {
                        // Stop the system if its running
                        stopSystem();
                        System.out.println("Exiting the system. Goodbye!");
                        // Close scanner to prevent leaks
                        scanner.close();
                        // End the application
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // Message if ticketing system is running when it shoudn't be
                System.out.println("Ticketing system is running. Select '4' to stop it.");
            }
        }
    }

    // Method to get configuration settings from user
    private static void configureSettings(Scanner scanner, Configuration config) {
        System.out.println("\nEnter configuration settings:");
        // Get total number of tickets with validation
        config.setTotalTickets(getValidInput(scanner, "Total Number of Tickets (must be > 0): ", 1, Integer.MAX_VALUE));
        // Get ticket release rate with validation
        config.setTicketReleaseRate(getValidInput(scanner, "Ticket Release Rate (ms, must be > 0): ", 1000, Integer.MAX_VALUE));
        // Get customer retrieval rate with validation
        config.setCustomerRetrievalRate(getValidInput(scanner, "Customer Retrieval Rate (ms, must be > 0): ", 1000, Integer.MAX_VALUE));
        // Get max ticket capacity with validation, limit is total amount of tickets
        config.setMaxTicketCapacity(getValidInput(scanner, "Maximum Ticket Capacity (must be <= Total Tickets): ", 1, config.getTotalTickets()));
        System.out.println("Configuration updated successfully!");
    }

    // Method to save the current config to json file
    private static void saveConfiguration(Configuration config, String fileName) {
        try {
            config.saveToFile(fileName);
            System.out.println("Configuration saved to " + fileName);
        } catch (Exception e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Method to load the config from a file
    private static Configuration loadConfiguration(String fileName) {
        try {
            // Load config from file
            Configuration config = Configuration.loadFromFile(fileName);
            System.out.println("Configuration loaded successfully from " + fileName);
            // Show the config to the user
            System.out.println("Total Tickets: " + config.getTotalTickets());
            System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
            System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
            System.out.println("Maximum Ticket Capacity: " + config.getMaxTicketCapacity());
            return config;
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
        // Return default config if loading fails
        return new Configuration();
    }

    // Method to start all the threads and ticketing system
    private static void startSystem(Configuration config) {
        // Prevent running multiple times
        if (systemRunning) {
            System.out.println("The ticketing system is already running.");
            return;
        }
        // Create the ticket pool
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

        // Create vendors with their threads and configuration
        vendor1 = new Vendor("Vendor 1", config.getTicketReleaseRate(), ticketPool);
        vendor2 = new Vendor("Vendor 2", config.getTicketReleaseRate(), ticketPool);
        // Create customers with their threads and configuration
        customer1 = new Customer("Customer 1", config.getCustomerRetrievalRate(), ticketPool);
        customer2 = new Customer("Customer 2", config.getCustomerRetrievalRate() + 500, ticketPool);

        // Create the thread objects
        vendorThread1 = new Thread(vendor1);
        vendorThread2 = new Thread(vendor2);
        customerThread1 = new Thread(customer1);
        customerThread2 = new Thread(customer2);
        // Start all of the threads
        vendorThread1.start();
        vendorThread2.start();
        customerThread1.start();
        customerThread2.start();

        // Track that the system is running
        systemRunning = true;
        System.out.println("Ticketing system started. Select '4' to stop it.");
    }

    // Method to stop the system by stopping all the threads
    private static void stopSystem() {
        // Prevent user from stopping if its not running
        if (!systemRunning) {
            System.out.println("Ticketing system is not running.");
            return;
        }

        System.out.println("Stopping the ticketing system...");
        // Call each threads stop method
        vendor1.stop();
        vendor2.stop();
        customer1.stop();
        customer2.stop();

        try {
            // Ensure each thread is stopped
            vendorThread1.join();
            vendorThread2.join();
            customerThread1.join();
            customerThread2.join();
        } catch (InterruptedException e) {
            System.err.println("Error stopping threads: " + e.getMessage());
        }

        // Track that the system has stopped
        systemRunning = false;
        System.out.println("Ticketing system stopped.");
    }

    // Method to get input with validation for int value
    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                // Take the int and validate
                int value = scanner.nextInt();
                if (value >= min && value <= max) {
                    // consume the newline character
                    scanner.nextLine();
                    return value;
                }
                System.out.println("Invalid input. Please enter a value between " + min + " and " + max + ".");
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                // Consume the invalid input
                scanner.next();
            }
        }
    }

    // Method to get a valid choice for int with validation
    private static int getValidChoice(Scanner scanner, int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                // Take the int and validate
                int choice = scanner.nextInt();
                if (choice >= min && choice <= max) return choice;
            }
            System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
            // consume the newline character
            scanner.nextLine();
        }
    }
}