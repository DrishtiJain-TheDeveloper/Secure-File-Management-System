package modules;

import java.util.Scanner;

public class AuthenticationAccessControlModule {

    // Run the Authentication & Access Control menu
    public static void run(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nAuthentication & Access Control Menu:");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    registerNewUser(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting Authentication & Access Control...");
                    exit = true;  // This will exit the loop and return to the main menu
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Register a new user
    private static void registerNewUser(Scanner scanner) {
        System.out.println("Register New User");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Assign role (admin/user): ");
        String role = scanner.nextLine();

        // Simulate user registration logic
        System.out.println("User registered successfully!");
    }

    // User login process
    private static void login(Scanner scanner) {
        System.out.println("Login");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Simulate authentication (for the sake of example)
        if ("admin".equals(username) && "password123".equals(password)) {
            System.out.println("Authentication successful!");
            System.out.println("Your OTP: 905556");
            System.out.print("Enter OTP: ");
            String otp = scanner.nextLine();

            // Simulate OTP validation
            if ("905556".equals(otp)) {
                System.out.println("2FA Verified! Access granted.");
                System.out.println("Logged in as Admin: Full access granted.");
            } else {
                System.out.println("Invalid OTP. Login failed.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}
