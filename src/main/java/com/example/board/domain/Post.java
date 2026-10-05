package com.example.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 30)
    private String category;

    protected Post() {
    }

    public Post(User author, String title, String content) {
        this(author, title, content, "GENERAL");
    }

    public Post(User author, String title, String content, String category) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isWrittenBy(User user) {
        return author.getId().equals(user.getId());
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getViewCount() {
        return viewCount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCategory() {
        return category;
    }
}
