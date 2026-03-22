package com.securefs.modules;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ShareModule {

    public static class ShareRecord {
        public String id;
        public String sender;
        public String recipient;
        public String fileName;
        public String timestamp;
        public String status; // "delivered", "pending"

        public ShareRecord(String sender, String recipient, String fileName) {
            this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.sender = sender;
            this.recipient = recipient;
            this.fileName = fileName;
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            this.status = "delivered";
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new HashMap<>();
            m.put("id", id);
            m.put("sender", sender);
            m.put("recipient", recipient);
            m.put("fileName", fileName);
            m.put("timestamp", timestamp);
            m.put("status", status);
            return m;
        }
    }

    private static final List<ShareRecord> shareHistory = new ArrayList<>();

    public static void recordShare(String sender, String recipient, String fileName) {
        shareHistory.add(new ShareRecord(sender, recipient, fileName));
    }

    public static List<Map<String, Object>> getInbox(String username) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = shareHistory.size() - 1; i >= 0; i--) {
            ShareRecord r = shareHistory.get(i);
            if (r.recipient.equals(username)) result.add(r.toMap());
        }
        return result;
    }

    public static List<Map<String, Object>> getSent(String username) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = shareHistory.size() - 1; i >= 0; i--) {
            ShareRecord r = shareHistory.get(i);
            if (r.sender.equals(username)) result.add(r.toMap());
        }
        return result;
    }

    public static List<Map<String, Object>> getAll(String username) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = shareHistory.size() - 1; i >= 0; i--) {
            ShareRecord r = shareHistory.get(i);
            if (r.sender.equals(username) || r.recipient.equals(username)) result.add(r.toMap());
        }
        return result;
    }

    public static int getInboxCount(String username) {
        return (int) shareHistory.stream().filter(r -> r.recipient.equals(username)).count();
    }
}
