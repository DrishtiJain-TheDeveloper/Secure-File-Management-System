package modules;

import java.io.File;
import java.util.Scanner;

public class RoleBasedAccessControlModule {
    public static void run(Scanner scanner, String username) {
        FileModule fileModule = new FileModule();
        String role = AuthenticationAccessControlModule.getUserRole(username);
        
        if (role == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\nRole-Based Access Control (RBAC) Module");
        System.out.println("Welcome, " + username + "! Your role: " + role);

        if (role.equals("admin")) {
            adminMenu(scanner, username, fileModule);
        } else if (role.equals("user")) {
            userMenu(scanner, username, fileModule);
        } else {
            System.out.println("Invalid role. Access denied.");
        }
    }

    private static void adminMenu(Scanner scanner, String username, FileModule fileModule) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Admin Menu:");
            System.out.println("1. Create File");
            System.out.println("2. Read File");
            System.out.println("3. Write to File");
            System.out.println("4. Delete File");
            System.out.println("5. Share File");
            System.out.println("6. List All Users");
            System.out.println("7. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    fileModule.createFile(scanner, username);
                    break;
                case 2:
                    fileModule.readFile(scanner, username);
                    break;
                case 3:
                    fileModule.writeToFile(scanner, username);
                    break;
                case 4:
                    deleteFile(scanner, username);
                    break;
                case 5:
                    fileModule.shareFile(scanner, username);
                    break;
                case 6:
                    AuthenticationAccessControlModule.listAllUsers(scanner);
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void userMenu(Scanner scanner, String username, FileModule fileModule) {
        boolean exit = false;

        while (!exit) {
            System.out.println("User Menu:");
            System.out.println("1. Read File");
            System.out.println("2. Write to File");
            System.out.println("3. List My Files");
            System.out.println("4. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    fileModule.readFile(scanner, username);
                    break;
                case 2:
                    fileModule.writeToFile(scanner, username);
                    break;
                case 3:
                    AuthenticationAccessControlModule.listMyFiles(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void deleteFile(Scanner scanner, String username) {
        System.out.print("Enter the file name to delete: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File '" + fileName + "' deleted successfully.");
                AuthenticationAccessControlModule.cleanFileReferences(fileName);
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File not found.");
        }
    }
}
