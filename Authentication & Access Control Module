import java.util.Scanner;

public class SecureFileManagementSystem {

    static String[] usernames = new String[100];   // Dynamic array for usernames
    static String[] passwords = new String[100];   // Dynamic array for passwords
    static String[] roles = new String[100];       // Dynamic array for roles
    static int userCount = 0;                      // Tracks the number of users

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Secure File Management System");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    registerUser(scanner);    // New user registration
                    break;
                case 2:
                    loginUser(scanner);       // Login with authentication
                    break;
                case 3:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    //Register New User
    private static void registerUser(Scanner scanner) {
        System.out.println("Register New User");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Assign role (admin/user): ");
        String role = scanner.nextLine().toLowerCase();

        // Store in dynamic arrays
        usernames[userCount] = username;
        passwords[userCount] = password;
        roles[userCount] = role;
        userCount++;

        System.out.println("User registered successfully!");
    }

    //Login and Authentication
    private static void loginUser(Scanner scanner) {
        System.out.println("Login");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean authenticated = false;
        int userIndex = -1;

        // Check for valid credentials
        for (int i = 0; i < userCount; i++) {
            if (username.equals(usernames[i]) && password.equals(passwords[i])) {
                authenticated = true;
                userIndex = i;
                break;
            }
        }

        if (authenticated) {
            System.out.println("Authentication successful!");

            // Two-Factor Authentication (2FA)
            if (twoFactorAuthentication(scanner)) {
                System.out.println("2FA Verified! Access granted.");

                // Role-based access control
                switch (roles[userIndex]) {
                    case "admin":
                        System.out.println("Logged in as Admin: Full access granted.");
                        break;
                    case "user":
                        System.out.println("Logged in as User: Limited access granted.");
                        break;
                    default:
                        System.out.println("Unknown role. Access denied.");
                }
            } else {
                System.out.println("2FA failed. Access denied.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // Two-Factor Authentication (2FA)
    private static boolean twoFactorAuthentication(Scanner scanner) {
        int otp = (int) (Math.random() * 900000) + 100000;  // Random 6-digit OTP
        System.out.println("Your OTP: " + otp);

        System.out.print("Enter OTP: ");
        int enteredOTP = scanner.nextInt();

        return enteredOTP == otp;
    }
}
