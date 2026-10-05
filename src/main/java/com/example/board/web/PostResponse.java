package com.example.board.web;

import com.example.board.domain.Post;
import java.time.LocalDateTime;

public record PostResponse(Long id, String author, String title, String content, long viewCount,
                           LocalDateTime createdAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getAuthor().getNickname(), post.getTitle(), post.getContent(),
                post.getViewCount(), post.getCreatedAt());
    }
}
