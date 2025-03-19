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
            scanner.nextLine();  // Consume newline

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

    //Create a New File
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

        System.out.print("Enter encryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine();  // Consume newline

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

        System.out.print("Enter decryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String encryptedContent = reader.readLine();
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

    //Share File (Simulating Sharing)
    private static void shareFile(Scanner scanner) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        System.out.print("Enter recipient's name: ");
        String recipient = scanner.nextLine();

        System.out.println("Sharing " + fileName + " with " + recipient + "...");
        System.out.println("File shared successfully!");
    }

    //  Encryption using Caesar Cipher
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
        return encrypt(content, 26 - shift);  // Decrypt by reversing the shift
    }
}
