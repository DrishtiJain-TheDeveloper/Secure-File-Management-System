package com.securefs.modules;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class AuthenticationAccessControlModule {

    private static Map<String, User> users = new HashMap<>();
    private static Set<String> activeUsers = new HashSet<>();
    private static Map<String, Integer> loginAttempts = new HashMap<>();
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public static final String[] SECURITY_QUESTIONS = {
        "What is your favorite color?",
        "What is your pet's name?",
        "What was your childhood nickname?",
        "What is your mother's maiden name?",
        "What was the name of your first school?",
        "What is your favorite food?",
        "What city were you born in?",
        "What is your favorite movie?",
        "Who was your first best friend?",
        "What is your dream job?"
    };

    public static class User {
        public String username;
        public String password;
        public String role;
        public String securityQuestion;
        public String securityAnswer;
        public List<String> createdFiles = new ArrayList<>();

        public User(String username, String password, String role, String securityQuestion, String securityAnswer) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.securityQuestion = securityQuestion;
            this.securityAnswer = securityAnswer;
        }
    }

    static {
        users.put("secureadmin", new User("secureadmin", "password123", "admin",
                "What is your favorite color?", "blue"));
    }

    public static Map<String, Object> register(String username, String password,
                                                String securityQuestion, String securityAnswer) {
        Map<String, Object> result = new HashMap<>();
        if (users.containsKey(username)) {
            result.put("success", false);
            result.put("message", "Username already exists.");
            return result;
        }
        users.put(username, new User(username, password, "user", securityQuestion, securityAnswer));
        result.put("success", true);
        result.put("message", "User registered successfully.");
        return result;
    }

    public static Map<String, Object> login(String username, String password, String securityAnswer) {
        Map<String, Object> result = new HashMap<>();

        if (loginAttempts.getOrDefault(username, 0) >= MAX_LOGIN_ATTEMPTS) {
            result.put("success", false);
            result.put("message", "Account locked due to too many failed attempts.");
            return result;
        }

        User user = users.get(username);
        if (user == null || !user.password.equals(password)) {
            incrementLoginAttempts(username);
            int remaining = MAX_LOGIN_ATTEMPTS - loginAttempts.getOrDefault(username, 0);
            result.put("success", false);
            result.put("message", remaining > 0
                    ? "Invalid credentials. " + remaining + " attempts remaining."
                    : "Account locked.");
            return result;
        }

        if (!user.securityAnswer.equalsIgnoreCase(securityAnswer.trim())) {
            incrementLoginAttempts(username);
            result.put("success", false);
            result.put("message", "Incorrect security answer.");
            return result;
        }

        activeUsers.add(username);
        loginAttempts.remove(username);
        result.put("success", true);
        result.put("message", "Login successful.");
        result.put("role", user.role);
        result.put("username", username);
        return result;
    }

    public static Map<String, Object> getSecurityQuestion(String username) {
        Map<String, Object> result = new HashMap<>();
        User user = users.get(username);
        if (user == null) {
            result.put("success", false);
            result.put("message", "User not found.");
        } else {
            result.put("success", true);
            result.put("question", user.securityQuestion);
        }
        return result;
    }

    public static void logout(String username) {
        activeUsers.remove(username);
    }

    private static void incrementLoginAttempts(String username) {
        loginAttempts.put(username, loginAttempts.getOrDefault(username, 0) + 1);
    }

    public static boolean isLoggedIn(String username) {
        return activeUsers.contains(username);
    }

    public static boolean isAdmin(String username) {
        User user = users.get(username);
        return user != null && user.role.equals("admin");
    }

    public static boolean isUserRegistered(String username) {
        return users.containsKey(username);
    }

    public static String getUserRole(String username) {
        User user = users.get(username);
        return user != null ? user.role : null;
    }

    public static List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (User u : users.values()) {
            Map<String, Object> m = new HashMap<>();
            m.put("username", u.username);
            m.put("role", u.role);
            m.put("filesCount", u.createdFiles.size());
            list.add(m);
        }
        return list;
    }

    public static boolean removeUser(String username) {
        return users.remove(username) != null;
    }

    public static void addCreatedFile(String username, String filename) {
        User user = users.get(username);
        if (user != null) user.createdFiles.add(filename);
    }

    public static boolean removeCreatedFile(String username, String filename) {
        User user = users.get(username);
        return user != null && user.createdFiles.remove(filename);
    }

    public static void cleanFileReferences(String filename) {
        for (User user : users.values()) user.createdFiles.remove(filename);
    }

    public static List<String> getUserFiles(String username) {
        User user = users.get(username);
        return user != null ? new ArrayList<>(user.createdFiles) : new ArrayList<>();
    }
}
