package modules;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RoleBasedAccessControlModule {

    public static void run(Scanner scanner) {

        System.out.println(" Role-Based Access Control (RBAC) Module");

        // Simulated role verification (retrieved from the Authentication Module)
        System.out.print("\nEnter your role (admin/user): ");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("admin")) {
            System.out.println("Welcome, Admin! You have FULL access.");
            adminMenu(scanner);
        } else if (role.equals("user")) {
            System.out.println("Welcome, User! You have LIMITED access.");
            userMenu(scanner);
        } else {
            System.out.println("Invalid role. Access denied.");
        }
    }

    // Admin menu with full privileges
    private static void adminMenu(Scanner scanner) {

        boolean exit = false;

        while (!exit) {
            System.out.println("\n🔧 Admin Menu:");
            System.out.println("1. Create File");
            System.out.println("2. Read File");
            System.out.println("3. Write to File");
            System.out.println("4. Delete File");
            System.out.println("5. Share File");
            System.out.println("6. Exit");

            System.out.print("\nChoose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Admin: Creating File...");
                    createFile(scanner);
                    break;
                case 2:
                    System.out.println("Admin: Reading File...");
                    readFile(scanner);
                    break;
                case 3:
                    System.out.println(" Admin: Writing to File...");
                    writeFile(scanner);
                    break;
                case 4:
                    System.out.println("Admin: Deleting File...");
                    deleteFile(scanner);
                    break;
                case 5:
                    System.out.println("Admin: Sharing File...");
                    shareFile(scanner);
                    break;
                case 6:
                    System.out.println("Exiting Admin Menu...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // User menu with limited privileges
    private static void userMenu(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("User Menu:");
            System.out.println("1. Read File");
            System.out.println("2. Write to File");
            System.out.println("3. Exit");

            System.out.print("\nChoose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("User: Reading File...");
                    readFile(scanner);
                    break;
                case 2:
                    System.out.println("User: Writing to File...");
                    writeFile(scanner);
                    break;
                case 3:
                    System.out.println("Exiting User Menu...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Method to create a file
    private static void createFile(Scanner scanner) {
        System.out.print("Enter file name to create: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + fileName);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    // Method to read a file
    private static void readFile(Scanner scanner) {
        System.out.print("Enter file name to read: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (file.exists()) {
            System.out.println("Reading file: " + fileName);
            // Add code here to read the file content (stub for now)
        } else {
            System.out.println("File not found.");
        }
    }

    // Method to write to a file
    private static void writeFile(Scanner scanner) {
        System.out.print("Enter file name to write to: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (file.exists()) {
            System.out.println("Writing to file: " + fileName);
            // Add code here to write to the file (stub for now)
        } else {
            System.out.println("File not found.");
        }
    }

    // Method to delete a file
    private static void deleteFile(Scanner scanner) {
        System.out.print("Enter the file name to delete: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File '" + fileName + "' deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File not found.");
        }
    }

    // Method to share a file (stub for future implementation)
    private static void shareFile(Scanner scanner) {
        System.out.println("File sharing functionality is under development.");
        // Add sharing functionality here in the future.
    }
}
