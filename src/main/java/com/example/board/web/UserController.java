package com.example.board.web;

import com.example.board.domain.User;
import com.example.board.security.CurrentUser;
import com.example.board.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    /** 다른 회원의 프로필 (게시글 작성자 클릭 시) */
    @GetMapping("/api/users/{id}")
    @PreAuthorize("isAuthenticated()")
    public User profile(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/api/users/me/bio")
    @PreAuthorize("isAuthenticated()")
    public BioResponse updateBio(@RequestBody BioRequest request) {
        User user = userService.updateBio(currentUser.get(), request.bio());
        return new BioResponse(user.getNickname(), user.getBio());
    }

    public record BioRequest(String bio) {
    }

    public record BioResponse(String nickname, String bio) {
    }
}
