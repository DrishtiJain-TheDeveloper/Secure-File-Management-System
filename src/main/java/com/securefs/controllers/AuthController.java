package com.securefs.controllers;

import com.securefs.modules.AuthenticationAccessControlModule;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        return AuthenticationAccessControlModule.register(
            body.get("username"), body.get("password"),
            body.get("securityQuestion"), body.get("securityAnswer")
        );
    }

    @GetMapping("/security-question")
    public Map<String, Object> getSecurityQuestion(@RequestParam String username) {
        return AuthenticationAccessControlModule.getSecurityQuestion(username);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        return AuthenticationAccessControlModule.login(
            body.get("username"), body.get("password"), body.get("securityAnswer")
        );
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestBody Map<String, String> body) {
        AuthenticationAccessControlModule.logout(body.get("username"));
        return Map.of("success", true, "message", "Logged out.");
    }

    @GetMapping("/questions")
    public Map<String, Object> getQuestions() {
        return Map.of("questions", AuthenticationAccessControlModule.SECURITY_QUESTIONS);
    }
}
