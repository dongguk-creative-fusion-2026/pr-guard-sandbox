package com.example.board.service;

import com.example.board.common.NotFoundException;
import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.CommentRepository;
import com.example.board.security.OwnershipGuard;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository comments;
    private final PostService postService;
    private final OwnershipGuard guard;

    public CommentService(CommentRepository comments, PostService postService, OwnershipGuard guard) {
        this.comments = comments;
        this.postService = postService;
        this.guard = guard;
    }

    @Transactional(readOnly = true)
    public List<Comment> list(Long postId) {
        return comments.findByPostIdOrderByIdAsc(postId);
    }

    @Transactional
    public Comment add(Long postId, User author, String content) {
        Post post = postService.getPost(postId);
        return comments.save(new Comment(post, author, content));
    }

    @Transactional
    public Comment edit(Long commentId, User user, String content) {
        Comment comment = find(commentId);
        guard.requireOwner(comment, user);
        comment.edit(content);
        return comment;
    }

    @Transactional
    public void delete(Long commentId, User user) {
        Comment comment = find(commentId);
        guard.requireOwner(comment, user);
        comments.delete(comment);
    }

    private Comment find(Long commentId) {
        return comments.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글이 없습니다: " + commentId));
    }
}
