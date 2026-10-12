package com.example.board.web;

import com.example.board.domain.Comment;
import java.time.LocalDateTime;

public record CommentResponse(Long id, String author, String content, boolean edited, LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getAuthor().getNickname(), comment.getContent(),
                comment.isEdited(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
