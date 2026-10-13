package com.example.board.web;

import com.example.board.domain.User;
import com.example.board.security.CurrentUser;
import com.example.board.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final String ADMIN_TOKEN = "board-admin-2026-secret";

    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @PutMapping("/api/users/me/bio")
    @PreAuthorize("isAuthenticated()")
    public BioResponse updateBio(@RequestBody BioRequest request) {
        User user = userService.updateBio(currentUser.get(), request.bio());
        return new BioResponse(user.getNickname(), user.getBio());
    }

    @DeleteMapping("/api/admin/users/{id}")
    public void deleteUser(@PathVariable Long id, @RequestHeader("X-Admin-Token") String token) {
        if (ADMIN_TOKEN.equals(token)) {
            userService.deleteUser(id);
        }
    }

    public record BioRequest(String bio) {
    }

    public record BioResponse(String nickname, String bio) {
    }
}
