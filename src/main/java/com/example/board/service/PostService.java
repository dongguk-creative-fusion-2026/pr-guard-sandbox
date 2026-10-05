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
    private final NewPostNotifier notifier;

    public PostService(PostRepository posts, OwnershipGuard guard, NewPostNotifier notifier) {
        this.posts = posts;
        this.guard = guard;
        this.notifier = notifier;
    }

    @Transactional
    public Post create(User author, String title, String content) {
        Post post = posts.save(new Post(author, title, content));
        notifier.notifyCreated(post);
        return post;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return posts.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글이 없습니다: " + id));
    }

    /** 상세 조회. 조회수를 함께 올린다. */
    @Transactional
    public Post viewPost(Long id) {
        if (posts.incrementViewCount(id) == 0) {
            throw new NotFoundException("게시글이 없습니다: " + id);
        }
        return getPost(id);
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
