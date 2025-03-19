package modules;

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

        scanner.close();
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
                    // Call FileOperationsModule.createFile() (stub for now)
                    break;
                case 2:
                    System.out.println("Admin: Reading File...");
                    // Call FileOperationsModule.readFile() (stub for now)
                    break;
                case 3:
                    System.out.println(" Admin: Writing to File...");
                    // Call FileOperationsModule.writeFile() (stub for now)
                    break;
                case 4:
                    System.out.println("Admin: Deleting File...");
                    // Stub for future functionality
                    break;
                case 5:
                    System.out.println("Admin: Sharing File...");
                    // Stub for future functionality
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
                    // Call FileOperationsModule.readFile() (stub for now)
                    break;
                case 2:
                    System.out.println("User: Writing to File...");
                    // Call FileOperationsModule.writeFile() (stub for now)
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
}
