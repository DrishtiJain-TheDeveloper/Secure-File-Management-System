package com.securefs.controllers;

import com.securefs.modules.AuthenticationAccessControlModule;
import com.securefs.modules.ShareModule;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private Map<String, Object> denied() {
        return Map.of("success", false, "message", "Please login first.");
    }

    @GetMapping("/inbox")
    public Map<String, Object> inbox(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        return Map.of("success", true, "records", ShareModule.getInbox(username));
    }

    @GetMapping("/sent")
    public Map<String, Object> sent(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        return Map.of("success", true, "records", ShareModule.getSent(username));
    }

    @GetMapping("/history")
    public Map<String, Object> history(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        return Map.of("success", true, "records", ShareModule.getAll(username));
    }

    @GetMapping("/inbox/count")
    public Map<String, Object> inboxCount(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        return Map.of("success", true, "count", ShareModule.getInboxCount(username));
    }

    @GetMapping("/users")
    public Map<String, Object> users(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        // Return all registered users except self for recipient picker
        var allUsers = AuthenticationAccessControlModule.getAllUsers();
        var filtered = allUsers.stream()
            .filter(u -> !u.get("username").equals(username))
            .toList();
        return Map.of("success", true, "users", filtered);
    }
}
