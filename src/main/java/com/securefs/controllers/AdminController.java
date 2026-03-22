package com.securefs.controllers;

import com.securefs.modules.AuthenticationAccessControlModule;
import com.securefs.modules.FileModule;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final FileModule fileModule = new FileModule();

    private Map<String, Object> denied() {
        return Map.of("success", false, "message", "Access denied. Admin only.");
    }

    @GetMapping("/users")
    public Map<String, Object> listUsers(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isAdmin(username) ||
            !AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        return Map.of("success", true, "users", AuthenticationAccessControlModule.getAllUsers());
    }

    @DeleteMapping("/users/{target}")
    public Map<String, Object> deleteUser(@RequestParam String username, @PathVariable String target) {
        if (!AuthenticationAccessControlModule.isAdmin(username) ||
            !AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        if (target.equals("secureadmin"))
            return Map.of("success", false, "message", "Cannot delete default admin.");
        boolean removed = AuthenticationAccessControlModule.removeUser(target);
        return Map.of("success", removed, "message", removed ? "User deleted." : "User not found.");
    }

    @GetMapping("/users/{target}/files")
    public Map<String, Object> viewUserFiles(@RequestParam String username, @PathVariable String target) {
        if (!AuthenticationAccessControlModule.isAdmin(username) ||
            !AuthenticationAccessControlModule.isLoggedIn(username)) return denied();
        List<String> files = AuthenticationAccessControlModule.getUserFiles(target);
        return Map.of("success", true, "files", files);
    }
}
