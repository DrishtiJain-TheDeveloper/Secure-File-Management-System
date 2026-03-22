package com.securefs.controllers;

import com.securefs.modules.AuthenticationAccessControlModule;
import com.securefs.modules.FileModule;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileModule fileModule = new FileModule();

    private Map<String, Object> notLoggedIn() {
        return Map.of("success", false, "message", "Please login first.");
    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        return fileModule.createFile(username, body.get("fileName"));
    }

    @PostMapping("/write")
    public Map<String, Object> write(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        int shift = Integer.parseInt(body.get("shift").toString());
        boolean append = Boolean.parseBoolean(body.getOrDefault("append", "false").toString());
        return fileModule.writeToFile(username, (String) body.get("fileName"),
                (String) body.get("content"), shift, append);
    }

    @PostMapping("/read")
    public Map<String, Object> read(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        int shift = Integer.parseInt(body.get("shift").toString());
        return fileModule.readFile(username, (String) body.get("fileName"), shift);
    }

    @PostMapping("/metadata")
    public Map<String, Object> metadata(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        return fileModule.getMetadata(username, body.get("fileName"));
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        return fileModule.deleteFile(username, body.get("fileName"));
    }

    @PostMapping("/share")
    public Map<String, Object> share(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        return fileModule.shareFile(username, body.get("fileName"), body.get("recipient"));
    }

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam String username) {
        if (!AuthenticationAccessControlModule.isLoggedIn(username)) return notLoggedIn();
        List<String> files = AuthenticationAccessControlModule.getUserFiles(username);
        return Map.of("success", true, "files", files);
    }
}
