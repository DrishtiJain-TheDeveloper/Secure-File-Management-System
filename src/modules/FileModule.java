<<<<<<< HEAD

=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
package modules;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileModule {
    private static final String[] MALWARE_SIGNATURES = {
<<<<<<< HEAD
            "eval\\(.\\)", "exec\\(.\\)", "base64_decode", "rm -rf",
            "drop table", "delete from", "<script>.*</script>",
            "\\|.\\|", "\\$\\{.\\}", "powershell", "wscript\\.shell"
    };

    private static final String[] DANGEROUS_EXTENSIONS = {
            ".exe", ".bat", ".cmd", ".sh", ".php", ".js", ".jar", ".dll"
    };

    public void run(Scanner scanner) {
        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine();

=======
        "eval\\(.*\\)", "exec\\(.*\\)", "base64_decode", "rm -rf", 
        "drop table", "delete from", "<script>.*</script>", 
        "\\|.*\\|", "\\$\\{.*\\}", "powershell", "wscript\\.shell"
    };
    
    private static final String[] DANGEROUS_EXTENSIONS = {
        ".exe", ".bat", ".cmd", ".sh", ".php", ".js", ".jar", ".dll"
    };

    public void run(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) {
            System.out.println("Please login first.");
            return;
        }

<<<<<<< HEAD
        if (AuthenticationAccessControlModule.isAdmin(username)) {
            System.out.println("Admins are not allowed to perform file operations in this module.");
            return;
        }

=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        while (true) {
            System.out.println("\nFile Operations Menu:");
            System.out.println("1. Create a File");
            System.out.println("2. Write to File (with Encryption)");
            System.out.println("3. Read from File (with Decryption)");
            System.out.println("4. Display File Metadata");
            System.out.println("5. Share File");
<<<<<<< HEAD
            System.out.println("6. Delete File");
            System.out.println("7. Back to Main Menu");
=======
            System.out.println("6. Back to Main Menu");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createFile(scanner, username);
                    break;
                case 2:
                    writeToFile(scanner, username);
                    break;
                case 3:
                    readFile(scanner, username);
                    break;
                case 4:
                    displayFileMetadata(scanner, username);
                    break;
                case 5:
                    shareFile(scanner, username);
                    break;
                case 6:
<<<<<<< HEAD
                    deleteFile(scanner, username);
                    break;
                case 7:
=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

<<<<<<< HEAD
    public void deleteFile(Scanner scanner, String username) {
        System.out.print("Enter the name of the file to delete: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Error: File not found.");
            return;
        }

        if (file.delete()) {
            System.out.println("File deleted successfully.");
            AuthenticationAccessControlModule.removeCreatedFile(username, fileName);
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
 

=======
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
    public void createFile(Scanner scanner, String username) {
        System.out.print("Enter the file name: ");
        String fileName = scanner.nextLine();

        if (hasDoubleExtension(fileName)) {
            System.out.println("Warning: Suspicious file name with double extension detected!");
            return;
        }

        if (hasDangerousExtension(fileName)) {
            System.out.println("Warning: Potentially dangerous file extension detected!");
            if (!AuthenticationAccessControlModule.isAdmin(username)) {
                System.out.println("Only admins can create files with this extension.");
                return;
            }
        }

        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                AuthenticationAccessControlModule.addCreatedFile(username, fileName);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public void writeToFile(Scanner scanner, String username) {
<<<<<<< HEAD
        System.out.print("\nEnter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Error: File doesn't exist.");
            return;
        }

        System.out.println("\nWrite Mode:");
        System.out.println("1. Overwrite existing content");
        System.out.println("2. Append to existing content");
        System.out.print("Choose write mode: ");
        int mode = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter content: ");
=======
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        System.out.print("Enter content to write: ");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        String content = scanner.nextLine();

        if (content.length() > 1048576) {
            System.out.println("Error: Content exceeds the maximum allowed size (1MB).");
            return;
        }

        if (isMalware(content, fileName)) {
            System.out.println("File write failed due to malware detection.");
            return;
        }

        System.out.print("Enter encryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine();

        String encryptedContent = encrypt(content, shift);

<<<<<<< HEAD
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fileName, mode == 2))) {
            writer.write(encryptedContent);
            System.out.println("Data written successfully!");
=======
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(encryptedContent);
            System.out.println("Data written and encrypted successfully!");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void readFile(Scanner scanner, String username) {
<<<<<<< HEAD
        System.out.print("\nEnter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Error: File '" + fileName + "' does not exist.");
            System.out.println("You have not created any file with this name.");
            return;
        }

=======
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        if (file.length() > 1048576) {
            System.out.println("Error: File size exceeds the maximum allowed size (1MB).");
            return;
        }

        System.out.print("Enter decryption shift (1-25): ");
        int shift = scanner.nextInt();
        scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
                if (contentBuilder.length() > 1048576) {
                    System.out.println("Error: File content exceeds the maximum allowed size (1MB).");
                    return;
                }
            }

            String encryptedContent = contentBuilder.toString();
            String decryptedContent = decrypt(encryptedContent, shift);
<<<<<<< HEAD
            System.out.println("\nDecrypted Content:\n" + decryptedContent);
=======
            System.out.println("Decrypted Content:\n" + decryptedContent);
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void displayFileMetadata(Scanner scanner, String username) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
        if (file.exists()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("File Metadata:");
            System.out.println("File Name: " + file.getName());
            System.out.println("Size: " + file.length() + " bytes");
            System.out.println("Last Modified: " + sdf.format(new Date(file.lastModified())));
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        } else {
            System.out.println("File not found.");
        }
    }

    public void shareFile(Scanner scanner, String username) {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
        if (!file.exists()) {
            AuthenticationAccessControlModule.removeCreatedFile(username, fileName);
            System.out.println("Error: File not found.");
            return;
        }

        if (file.length() > 1048576) {
            System.out.println("Error: File size exceeds the maximum allowed size (1MB).");
            return;
        }

<<<<<<< HEAD
        System.out.print("Enter recipient's username: ");
        String recipient = scanner.nextLine();

        if (!AuthenticationAccessControlModule.isUserRegistered(recipient)) {
            System.out.println("Error: Recipient '" + recipient + "' is not a registered user.");
            return;
        }

        File sharedDir = new File("shared_files");
        if (!sharedDir.exists()) {
            sharedDir.mkdirs();
        }

        File destFile = new File(sharedDir, recipient + "_" + file.getName());

        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("File shared successfully with " + recipient + "!");
            System.out.println("Simulated copy created at: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error while sharing file: " + e.getMessage());
        }
=======
        System.out.print("Enter recipient's name: ");
        String recipient = scanner.nextLine();

        if (recipient.trim().isEmpty()) {
            System.out.println("Error: Recipient's name cannot be empty.");
            return;
        }

        System.out.println("Sharing " + fileName + " with " + recipient + "...");
        System.out.println("File shared successfully!");
>>>>>>> 790cb1d4545b00b6a6c6d4f068c445d3c4e80bb4
    }

    private String encrypt(String content, int shift) {
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

    private String decrypt(String content, int shift) {
        return encrypt(content, 26 - shift);
    }

    private boolean isMalware(String content, String fileName) {
        for (String pattern : MALWARE_SIGNATURES) {
            if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(content).find()) {
                System.out.println("Malware detected: Known malicious pattern '" + pattern + "' found.");
                return true;
            }
        }

        if (hasDoubleExtension(fileName)) {
            System.out.println("Malware detected: File has double extension.");
            return true;
        }

        if (hasDangerousExtension(fileName) && content.length() > 1000) {
            System.out.println("Malware detected: Large content in potentially dangerous file type.");
            return true;
        }

        if (isBinaryContentInTextFile(content, fileName)) {
            System.out.println("Malware detected: Binary content in text file.");
            return true;
        }

        return false;
    }

    private boolean hasDoubleExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            int secondLastDot = fileName.lastIndexOf('.', lastDot - 1);
            return secondLastDot > 0;
        }
        return false;
    }

    private boolean hasDangerousExtension(String fileName) {
        String lowerName = fileName.toLowerCase();
        for (String ext : DANGEROUS_EXTENSIONS) {
            if (lowerName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBinaryContentInTextFile(String content, String fileName) {
        if (fileName.endsWith(".txt") || fileName.endsWith(".csv") || fileName.endsWith(".json")) {
            for (char c : content.toCharArray()) {
                if (c < 32 && c != '\n' && c != '\r' && c != '\t') {
                    return true;
                }
            }
        }
        return false;
    }
}
