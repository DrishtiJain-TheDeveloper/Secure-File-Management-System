package modules;

import java.io.File;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
import java.util.Scanner;

public class RoleBasedAccessControlModule {
    public static void run(Scanner scanner, String username) {
<<<<<<< HEAD
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) {
            System.out.println("Please login first.");
            return;
        }

        String role = AuthenticationAccessControlModule.getUserRole(username);

=======
        FileModule fileModule = new FileModule();
        String role = AuthenticationAccessControlModule.getUserRole(username);
        
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        if (role == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\nRole-Based Access Control (RBAC) Module");
        System.out.println("Welcome, " + username + "! Your role: " + role);

        if (role.equals("admin")) {
<<<<<<< HEAD
            adminMenu(scanner, username);
        } else if (role.equals("user")) {
            userMenu(scanner, username);
=======
            adminMenu(scanner, username, fileModule);
        } else if (role.equals("user")) {
            userMenu(scanner, username, fileModule);
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        } else {
            System.out.println("Invalid role. Access denied.");
        }
    }

<<<<<<< HEAD
    private static void adminMenu(Scanner scanner, String username) {
        FileModule fileModule = new FileModule();
        boolean exit = false;

        while (!exit) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. List All Users");
            System.out.println("2. Delete Any User");
            System.out.println("3. Delete Any File");
            System.out.println("4. View Files of Any User");
            System.out.println("5. Create File");
            System.out.println("6. Write to File");
            System.out.println("7. Read File");
            System.out.println("8. Display File Metadata");
            System.out.println("9. Share Own File");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
=======
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
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
<<<<<<< HEAD
                    AuthenticationAccessControlModule.listAllUsers(scanner);
                    break;
                case 2:
                    deleteAnyUser(scanner);
                    break;
                case 3:
                    deleteAnyFile(scanner);
                    break;
                case 4:
                    viewFilesOfAnyUser(scanner);
                    break;
                case 5:
                    fileModule.createFile(scanner, username);
                    break;
                case 6:
                    fileModule.writeToFile(scanner, username);
                    break;
                case 7:
                    fileModule.readFile(scanner, username);
                    break;
                case 8:
                    fileModule.displayFileMetadata(scanner, username);
                    break;
                case 9:
                    fileModule.shareFile(scanner, username);
                    break;
                case 10:
=======
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
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

<<<<<<< HEAD
    private static void userMenu(Scanner scanner, String username) {
        FileModule fileModule = new FileModule();
        fileModule.run(scanner);
    }

    private static void deleteAnyUser(Scanner scanner) {
        System.out.print("Enter the username to delete: ");
        String usernameToDelete = scanner.nextLine();

        if (usernameToDelete.equals("secureadmin")) {
            System.out.println("Cannot delete the default admin.");
            return;
        }

        if (AuthenticationAccessControlModule.removeUser(usernameToDelete)) {
            System.out.println("User '" + usernameToDelete + "' deleted successfully.");
        } else {
            System.out.println("User not found. Deletion failed.");
        }
    }

    private static void deleteAnyFile(Scanner scanner) {
        System.out.print("Enter the name of the file to delete: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        if (file.delete()) {
            System.out.println("File deleted successfully.");
            AuthenticationAccessControlModule.cleanFileReferences(fileName);
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    private static void viewFilesOfAnyUser(Scanner scanner) {
        System.out.print("Enter the username to view their files: ");
        String targetUser = scanner.nextLine();

        if (!AuthenticationAccessControlModule.isUserRegistered(targetUser)) {
            System.out.println("User not found.");
            return;
        }

        List<String> files = AuthenticationAccessControlModule.getUserFiles(targetUser);
        if (files.isEmpty()) {
            System.out.println("User '" + targetUser + "' has not created any files.");
        } else {
            System.out.println("Files created by '" + targetUser + "':");
            for (String file : files) {
                File f = new File(file);
                if (f.exists()) {
                    System.out.println("- " + file);
                } else {
                    System.out.println("- " + file + " (File not found)");
                }
            }
        }
    }
=======
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
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
}
