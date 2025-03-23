package modules;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FileModule {

    // Main Menu to Perform File Operations
    public static void run(Scanner scanner) {
        while (true) {
            System.out.println("\nFile Operations Menu:");
            System.out.println("1. Create a File");
            System.out.println("2. Write to File (with Encryption)");
            System.out.println("3. Read from File (with Decryption)");
            System.out.println("4. Display File Metadata");
            System.out.println("5. Share File");
            System.out.println("6. Back to Main Menu");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createFile(scanner);
                    break;
                case 2:
                    writeToFile(scanner);
                    break;
                case 3:
                    readFile(scanner);
                    break;
                case 4:
                    displayFileMetadata(scanner);
                    break;
                case 5:
                    shareFile(scanner);
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Create a New File
    private static void createFile(Scanner scanner) {
        System.out.print("Enter the file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    // Write to File with Encryption
    private static void writeToFile(Scanner scanner) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        System.out.print("Enter content to write: ");
        String content = scanner.nextLine();

        // Buffer Overflow Protection: Limit content size to 1MB (1,048,576 bytes)
        if (content.length() > 1048576) { // 1MB in bytes
            System.out.println("Error: Content exceeds the maximum allowed size (1MB).");
            return; // Exit the method if content is too large
        }

        // Check if content contains malware before proceeding
        if (isMalware(content)) {
            System.out.println("File write failed due to malware detection.");
            return; // Abort writing to the file if malware detected
        }

        // If no malware detected, proceed with encryption
        System.out.print("Enter encryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String encryptedContent = encrypt(content, shift);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(encryptedContent);
            System.out.println("Data written and encrypted successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

    }

    // Read from File with Decryption
    private static void readFile(Scanner scanner) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);

        // Check file size before reading (e.g., 1MB limit)
        if (file.length() > 1048576) { // 1MB in bytes
            System.out.println("Error: File size exceeds the maximum allowed size (1MB).");
            return; // Exit the method if file is too large
        }

        System.out.print("Enter decryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");

                // Buffer Overflow Protection: Stop reading if the content exceeds 1MB
                if (contentBuilder.length() > 1048576) {
                    System.out.println("Error: File content exceeds the maximum allowed size (1MB).");
                    return; // Exit the method if the content size exceeds the limit
                }
            }

            String encryptedContent = contentBuilder.toString();
            String decryptedContent = decrypt(encryptedContent, shift);

            System.out.println("Decrypted Content:\n" + decryptedContent);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Display File Metadata (Size, Last Modified Date)
    private static void displayFileMetadata(Scanner scanner) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);

        if (file.exists()) {
            long fileSize = file.length();
            long lastModified = file.lastModified();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lastModifiedDate = sdf.format(new Date(lastModified));

            System.out.println("File Metadata:");
            System.out.println("File Name: " + file.getName());
            System.out.println("Size: " + fileSize + " bytes");
            System.out.println("Last Modified: " + lastModifiedDate);
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        } else {
            System.out.println("File not found.");
        }
    }

    
    // Share File (Simulating Sharing)
private static void shareFile(Scanner scanner) {
    System.out.print("Enter file name: ");
    String fileName = scanner.nextLine();

    // Check if the file exists
    File file = new File(fileName);
    if (!file.exists()) {
        System.out.println("Error: File not found. Please ensure the file exists.");
        return; // Exit if the file doesn't exist
    }

    // Check file size before sharing (e.g., 1MB limit)
    if (file.length() > 1048576) { // 1MB in bytes
        System.out.println("Error: File size exceeds the maximum allowed size (1MB). Unable to share.");
        return; // Exit if the file is too large
    }

    // Ask for the recipient's name
    System.out.print("Enter recipient's name: ");
    String recipient = scanner.nextLine();

    // Validate recipient's name (it should not be empty)
    if (recipient.trim().isEmpty()) {
        System.out.println("Error: Recipient's name cannot be empty. Please provide a valid recipient.");
        return;
    }

    // Simulate sharing by printing a message
    System.out.println("Sharing " + fileName + " with " + recipient + "...");
    System.out.println("File shared successfully!");
}


    // Encryption using Caesar Cipher
    private static String encrypt(String content, int shift) {
        StringBuilder result = new StringBuilder();

        for (char ch : content.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                result.append((char) ((ch - base + shift) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    // Decryption using Caesar Cipher
    private static String decrypt(String content, int shift) {
        return encrypt(content, 26 - shift); // Decrypt by reversing the shift
    }

    // Check for suspicious patterns (basic malware detection simulation)
    private static boolean isMalware(String content) {
        // Check for certain dangerous patterns in the content (e.g., executable code or
        // shell commands)
        String[] dangerousPatterns = { "eval", "exec", "base64", "rm -rf", "drop table", "delete from" };

        for (String pattern : dangerousPatterns) {
            if (content.contains(pattern)) {
                System.out.println("Warning: Suspicious content detected (potential malware).");
                return true; // Malware detected
            }
        }
        return false; // No malware detected
    }

}
