package com.example.board.service;

import com.example.board.domain.User;
import com.example.board.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final int MAX_BIO_LENGTH = 1000;

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public User updateBio(User user, String bio) {
        if (bio != null && bio.length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException("소개는 " + MAX_BIO_LENGTH + "자 이하여야 합니다");
        }
        User managed = users.getReferenceById(user.getId());
        managed.changeBio(bio);
        return managed;
    }
}
