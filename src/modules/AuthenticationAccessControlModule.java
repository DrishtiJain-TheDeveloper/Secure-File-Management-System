package modules;

import java.util.*;
import java.security.SecureRandom;
import java.io.File;

public class AuthenticationAccessControlModule {
    private static Map<String, User> users = new HashMap<>();
    private static Set<String> activeUsers = new HashSet<>();
    private static SecureRandom random = new SecureRandom();

    private static class User {
        String username;
        String password;
        String role;
        List<String> createdFiles = new ArrayList<>();

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    static {
        users.put("admin", new User("admin", "admin123", "admin"));
        users.put("user1", new User("user1", "user123", "user"));
    }

    public static void run(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nAuthentication & Access Control Menu:");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. List All Users (Admin only)");
            System.out.println("4. List My Files (Logged in users)");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerNewUser(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    listAllUsers(scanner);
                    break;
                case 4:
                    listMyFiles(scanner);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerNewUser(Scanner scanner) {
        System.out.println("Register New User");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Assign role (admin/user): ");
        String role = scanner.nextLine().toLowerCase();

        if (!role.equals("admin") && !role.equals("user")) {
            System.out.println("Invalid role. Defaulting to 'user'.");
            role = "user";
        }

        users.put(username, new User(username, password, role));
        System.out.println("User registered successfully!");
    }

    private static void login(Scanner scanner) {
        System.out.println("Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            int otp = 100000 + random.nextInt(900000);
            System.out.println("Authentication successful!");
            System.out.println("Your OTP: " + otp);
            System.out.print("Enter OTP: ");
            String enteredOtp = scanner.nextLine();

            if (enteredOtp.equals(String.valueOf(otp))) {
                System.out.println("2FA Verified! Access granted.");
                activeUsers.add(username);
                System.out.println("Logged in as " + user.role + ": " + 
                    (user.role.equals("admin") ? "Full access granted." : "Limited access granted."));
            } else {
                System.out.println("Invalid OTP. Login failed.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void listAllUsers(Scanner scanner) {
        System.out.print("Enter admin username to verify: ");
        String username = scanner.nextLine();
        
        User user = users.get(username);
        if (user == null || !user.role.equals("admin")) {
            System.out.println("Access denied. Admin privileges required.");
            return;
        }

        System.out.println("\nList of Registered Users:");
        System.out.printf("%-15s %-10s %-15s%n", "Username", "Role", "Files Created");
        System.out.println("----------------------------------");
        
        for (User u : users.values()) {
            System.out.printf("%-15s %-10s %-15d%n", 
                u.username, u.role, u.createdFiles.size());
        }
    }

    public static void listMyFiles(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
        if (!activeUsers.contains(username)) {
            System.out.println("Please login first.");
            return;
        }

        User user = users.get(username);
        if (user.createdFiles.isEmpty()) {
            System.out.println("You haven't created any files yet.");
        } else {
            System.out.println("\nFiles created by you (existing files):");
            Iterator<String> iterator = user.createdFiles.iterator();
            while (iterator.hasNext()) {
                String filename = iterator.next();
                File file = new File(filename);
                if (file.exists()) {
                    System.out.println("- " + filename);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    public static void addCreatedFile(String username, String filename) {
        User user = users.get(username);
        if (user != null) {
            user.createdFiles.add(filename);
        }
    }

    public static boolean removeCreatedFile(String username, String filename) {
        User user = users.get(username);
        if (user != null) {
            return user.createdFiles.remove(filename);
        }
        return false;
    }

    public static void cleanFileReferences(String filename) {
        for (User user : users.values()) {
            user.createdFiles.remove(filename);
        }
    }

    public static boolean isAdmin(String username) {
        User user = users.get(username);
        return user != null && user.role.equals("admin");
    }

    public static boolean isLoggedIn(String username) {
        return activeUsers.contains(username);
    }

    public static String getUserRole(String username) {
        User user = users.get(username);
        return user != null ? user.role : null;
    }
}
