package com.securefs.modules;

import org.springframework.stereotype.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class FileModule {

    private static final String[] MALWARE_SIGNATURES = {
        "eval\\(.\\)", "exec\\(.\\)", "base64_decode", "rm -rf",
        "drop table", "delete from", "<script>.*</script>",
        "\\|.\\|", "\\$\\{.\\}", "powershell", "wscript\\.shell"
    };

    private static final String[] DANGEROUS_EXTENSIONS = {
        ".exe", ".bat", ".cmd", ".sh", ".php", ".js", ".jar", ".dll"
    };

    private static final String FILES_DIR = "shared_files/";

    public Map<String, Object> createFile(String username, String fileName) {
        Map<String, Object> result = new HashMap<>();
        if (hasDoubleExtension(fileName)) {
            result.put("success", false);
            result.put("message", "Suspicious file name with double extension detected.");
            return result;
        }
        if (hasDangerousExtension(fileName) && !AuthenticationAccessControlModule.isAdmin(username)) {
            result.put("success", false);
            result.put("message", "Only admins can create files with this extension.");
            return result;
        }
        File dir = new File(FILES_DIR);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(FILES_DIR + username + "_" + fileName);
        try {
            if (file.createNewFile()) {
                AuthenticationAccessControlModule.addCreatedFile(username, file.getPath());
                result.put("success", true);
                result.put("message", "File created: " + fileName);
                result.put("path", file.getPath());
            } else {
                result.put("success", false);
                result.put("message", "File already exists.");
            }
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "Error creating file: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> writeToFile(String username, String fileName, String content, int shift, boolean append) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(FILES_DIR + username + "_" + fileName);
        if (!file.exists()) {
            result.put("success", false);
            result.put("message", "File does not exist.");
            return result;
        }
        if (content.length() > 1048576) {
            result.put("success", false);
            result.put("message", "Content exceeds maximum allowed size (1MB).");
            return result;
        }
        if (isMalware(content, fileName)) {
            result.put("success", false);
            result.put("message", "Malware detected in content.");
            return result;
        }
        String encrypted = encrypt(content, shift);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(encrypted);
            result.put("success", true);
            result.put("message", "Content written successfully.");
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "Error writing file: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> readFile(String username, String fileName, int shift) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(FILES_DIR + username + "_" + fileName);
        if (!file.exists()) {
            result.put("success", false);
            result.put("message", "File not found.");
            return result;
        }
        if (file.length() > 1048576) {
            result.put("success", false);
            result.put("message", "File exceeds maximum allowed size (1MB).");
            return result;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            result.put("success", true);
            result.put("content", decrypt(sb.toString(), shift));
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "Error reading file: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> getMetadata(String username, String fileName) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(FILES_DIR + username + "_" + fileName);
        if (!file.exists()) {
            result.put("success", false);
            result.put("message", "File not found.");
            return result;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result.put("success", true);
        result.put("name", file.getName());
        result.put("size", file.length());
        result.put("lastModified", sdf.format(new Date(file.lastModified())));
        result.put("path", file.getAbsolutePath());
        return result;
    }

    public Map<String, Object> deleteFile(String username, String fileName) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(FILES_DIR + username + "_" + fileName);
        if (!file.exists()) {
            result.put("success", false);
            result.put("message", "File not found.");
            return result;
        }
        if (file.delete()) {
            AuthenticationAccessControlModule.removeCreatedFile(username, file.getPath());
            result.put("success", true);
            result.put("message", "File deleted.");
        } else {
            result.put("success", false);
            result.put("message", "Failed to delete file.");
        }
        return result;
    }

    public Map<String, Object> shareFile(String username, String fileName, String recipient) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(FILES_DIR + username + "_" + fileName);
        if (!file.exists()) {
            result.put("success", false);
            result.put("message", "File not found.");
            return result;
        }
        if (!AuthenticationAccessControlModule.isUserRegistered(recipient)) {
            result.put("success", false);
            result.put("message", "Recipient not registered.");
            return result;
        }
        File dest = new File(FILES_DIR + recipient + "_shared_" + fileName);
        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            AuthenticationAccessControlModule.addCreatedFile(recipient, dest.getPath());
            ShareModule.recordShare(username, recipient, fileName);
            result.put("success", true);
            result.put("message", "File shared with " + recipient + ".");
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "Error sharing file: " + e.getMessage());
        }
        return result;
    }

    private String encrypt(String content, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char ch : content.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                sb.append((char) ((ch - base + shift) % 26 + base));
            } else sb.append(ch);
        }
        return sb.toString();
    }

    private String decrypt(String content, int shift) {
        return encrypt(content, 26 - shift);
    }

    private boolean isMalware(String content, String fileName) {
        for (String pattern : MALWARE_SIGNATURES)
            if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(content).find()) return true;
        return hasDoubleExtension(fileName) ||
               (hasDangerousExtension(fileName) && content.length() > 1000) ||
               isBinaryContentInTextFile(content, fileName);
    }

    private boolean hasDoubleExtension(String fileName) {
        int last = fileName.lastIndexOf('.');
        return last > 0 && fileName.lastIndexOf('.', last - 1) > 0;
    }

    private boolean hasDangerousExtension(String fileName) {
        String lower = fileName.toLowerCase();
        for (String ext : DANGEROUS_EXTENSIONS) if (lower.endsWith(ext)) return true;
        return false;
    }

    private boolean isBinaryContentInTextFile(String content, String fileName) {
        if (fileName.endsWith(".txt") || fileName.endsWith(".csv") || fileName.endsWith(".json"))
            for (char c : content.toCharArray())
                if (c < 32 && c != '\n' && c != '\r' && c != '\t') return true;
        return false;
    }
}
