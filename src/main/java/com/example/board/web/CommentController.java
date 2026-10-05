package com.example.board.web;

import com.example.board.security.CurrentUser;
import com.example.board.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final CurrentUser currentUser;

    public CommentController(CommentService commentService, CurrentUser currentUser) {
        this.commentService = commentService;
        this.currentUser = currentUser;
    }

    @GetMapping("/api/posts/{postId}/comments")
    public List<CommentResponse> list(@PathVariable Long postId) {
        return commentService.list(postId).stream().map(CommentResponse::from).toList();
    }

    @PostMapping("/api/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public CommentResponse add(@PathVariable Long postId, @Valid @RequestBody CommentRequest request) {
        return CommentResponse.from(commentService.add(postId, currentUser.get(), request.content()));
    }

    @PutMapping("/api/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommentResponse edit(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return CommentResponse.from(commentService.edit(id, currentUser.get(), request.content()));
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id) {
        commentService.delete(id, currentUser.get());
    }

    public record CommentRequest(@NotBlank String content) {
    }
}
