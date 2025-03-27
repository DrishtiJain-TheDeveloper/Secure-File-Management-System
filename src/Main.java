import java.util.Scanner;
import modules.AuthenticationAccessControlModule;
import modules.FileModule;
import modules.RoleBasedAccessControlModule;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Secure File Management System");

        boolean exit = false;

        while (!exit) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Authentication & Access Control");
            System.out.println("2. File Operations & Encryption");
            System.out.println("3. Role-Based Access Control");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    AuthenticationAccessControlModule.run(scanner);
                    break;
                case 2:
                    FileModule fileModule = new FileModule();
                    fileModule.run(scanner);
                    break;
                case 3:
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    if (AuthenticationAccessControlModule.isLoggedIn(username)) {
                        RoleBasedAccessControlModule.run(scanner, username);
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting Secure File Management System. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}
