package modules;
<<<<<<< HEAD
=======

>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
import java.util.*;
import java.security.SecureRandom;
import java.io.File;

public class AuthenticationAccessControlModule {
    private static Map<String, User> users = new HashMap<>();
    private static Set<String> activeUsers = new HashSet<>();
<<<<<<< HEAD
    private static Map<String, Integer> loginAttempts = new HashMap<>();
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private static final String[] SECURITY_QUESTIONS = {
        "What is your favorite color?",
        "What is your pet’s name?",
        "What was your childhood nickname?",
        "What is your mother’s maiden name?",
        "What was the name of your first school?",
        "What is your favorite food?",
        "What city were you born in?",
        "What is your favorite movie?",
        "Who was your first best friend?",
        "What is your dream job?"
    };
=======
    private static SecureRandom random = new SecureRandom();
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4

    private static class User {
        String username;
        String password;
        String role;
<<<<<<< HEAD
        String securityQuestion;
        String securityAnswer;
        List<String> createdFiles = new ArrayList<>();

        User(String username, String password, String role, String securityQuestion, String securityAnswer) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.securityQuestion = securityQuestion;
            this.securityAnswer = securityAnswer;
=======
        List<String> createdFiles = new ArrayList<>();

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        }
    }

    static {
<<<<<<< HEAD
        users.put("secureadmin", new User("secureadmin", "password123", "admin", "What is your favorite color?", "blue"));
=======
        users.put("admin", new User("admin", "admin123", "admin"));
        users.put("user1", new User("user1", "user123", "user"));
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
    }

    public static void run(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
<<<<<<< HEAD
            System.out.println("\nAuthentication & Access Control Menu:");
=======
            System.out.println("\Authentication & Access Control Menu:");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
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
<<<<<<< HEAD
    public static boolean isUserRegistered(String username) {
        return users.containsKey(username);
    }
    
    private static void registerNewUser(Scanner scanner) {
        System.out.println("\nRegister New User");
=======

    private static void registerNewUser(Scanner scanner) {
        System.out.println("Register New User");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

<<<<<<< HEAD
        System.out.println("\nChoose a security question:");
        for (int i = 0; i < SECURITY_QUESTIONS.length; i++) {
            System.out.println((i + 1) + ". " + SECURITY_QUESTIONS[i]);
        }
        System.out.print("Enter the number of your choice: ");
        int questionIndex = scanner.nextInt();
        scanner.nextLine();

        if (questionIndex < 1 || questionIndex > SECURITY_QUESTIONS.length) {
            System.out.println("Invalid choice. Registration failed.");
            return;
        }

        String chosenQuestion = SECURITY_QUESTIONS[questionIndex - 1];
        System.out.println("Your selected question: " + chosenQuestion);
        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine();

        String role = "user";
        users.put(username, new User(username, password, role, chosenQuestion, answer));
        System.out.println("User registered successfully with security question!");
    }

    private static void login(Scanner scanner) {
        System.out.println("\nLogin");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (loginAttempts.containsKey(username) && 
            loginAttempts.get(username) >= MAX_LOGIN_ATTEMPTS) {
            System.out.println("Account locked due to too many failed attempts.");
            return;
        }

=======
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

>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
<<<<<<< HEAD
            System.out.println("Authentication successful!");
            System.out.println("Security Question: " + user.securityQuestion);
            System.out.print("Enter your answer: ");
            String answer = scanner.nextLine();

            if (user.securityAnswer.equalsIgnoreCase(answer.trim())) {
                System.out.println("2FA Verified! Access granted.");
                activeUsers.add(username);
                loginAttempts.remove(username);
                System.out.println("Logged in as " + user.role + ": " + 
                    (user.role.equals("admin") ? "Full access granted." : "Limited access granted."));
            } else {
                System.out.println("Incorrect security answer. Login failed.");
                incrementLoginAttempts(username);
            }
        } else {
            System.out.println("Invalid username or password.");
            incrementLoginAttempts(username);
        }
    }

    private static void incrementLoginAttempts(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);

        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            System.out.println("Too many failed attempts. Account locked.");
            activeUsers.remove(username);
        } else {
            System.out.println("Warning: " + (MAX_LOGIN_ATTEMPTS - attempts) +
                             " attempts remaining before account lock.");
=======
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
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        }
    }

    public static void listAllUsers(Scanner scanner) {
<<<<<<< HEAD
        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine();

        if (!activeUsers.contains(username)) {
            System.out.println("Please login first.");
            return;
        }

=======
        System.out.print("Enter admin username to verify: ");
        String username = scanner.nextLine();
        
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        User user = users.get(username);
        if (user == null || !user.role.equals("admin")) {
            System.out.println("Access denied. Admin privileges required.");
            return;
        }

        System.out.println("\nList of Registered Users:");
        System.out.printf("%-15s %-10s %-15s%n", "Username", "Role", "Files Created");
        System.out.println("----------------------------------");
<<<<<<< HEAD

=======
        
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        for (User u : users.values()) {
            System.out.printf("%-15s %-10s %-15d%n", 
                u.username, u.role, u.createdFiles.size());
        }
    }

    public static void listMyFiles(Scanner scanner) {
<<<<<<< HEAD
        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine();

=======
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
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
<<<<<<< HEAD
    public static boolean removeUser(String username) {
        return users.remove(username) != null;
    }
    public static List<String> getUserFiles(String username) {
        User user = users.get(username);
        if (user != null) {
            return new ArrayList<>(user.createdFiles);
        }
        return new ArrayList<>();
    }
    
    
    public static void listUserFiles(String username) {
        User user = users.get(username);
        if (user == null) {
            System.out.println("User does not exist.");
            return;
        }
        if (user.createdFiles.isEmpty()) {
            System.out.println("No files created by " + username + ".");
        } else {
            System.out.println("Files created by " + username + ":");
            for (String file : user.createdFiles) {
                System.out.println("- " + file);
            }
        }
    }
    
=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
}
