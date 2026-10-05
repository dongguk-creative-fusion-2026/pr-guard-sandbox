package com.example.board.security;

import com.example.board.common.NotFoundException;
import com.example.board.domain.User;
import com.example.board.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** 로그인한 사용자를 DB 에서 찾는다. */
@Component
public class CurrentUser {

    private final UserRepository users;

    public CurrentUser(UserRepository users) {
        this.users = users;
    }

    public User get() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다: " + email));
    }
}
