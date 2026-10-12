package com.example.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Column(length = 300)
    private String bio;

    @Column(nullable = false)
    private boolean deleted;

    protected User() {
    }

    public User(String email, String nickname, String role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }

    public boolean isDeleted() {
        return deleted;
    }

    /** 탈퇴. 작성한 글·댓글은 남기고 개인정보만 지운다. */
    public void withdraw() {
        this.deleted = true;
        this.nickname = "탈퇴한 회원";
        this.bio = null;
    }
}
