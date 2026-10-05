package com.example.board.service;

import com.example.board.common.NotFoundException;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.PostRepository;
import com.example.board.security.OwnershipGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository posts;
    private final OwnershipGuard guard;

    public PostService(PostRepository posts, OwnershipGuard guard) {
        this.posts = posts;
        this.guard = guard;
    }

    @Transactional
    public Post create(User author, String title, String content) {
        return posts.save(new Post(author, title, content));
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return posts.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글이 없습니다: " + id));
    }

    @Transactional
    public Post update(Long id, User user, String title, String content) {
        Post post = getPost(id);
        guard.requireOwner(post, user);
        post.update(title, content);
        return post;
    }

    @Transactional
    public void delete(Long id, User user) {
        Post post = getPost(id);
        guard.requireOwner(post, user);
        posts.delete(post);
    }
}
