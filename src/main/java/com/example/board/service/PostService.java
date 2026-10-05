package com.example.board.service;

import com.example.board.common.NotFoundException;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.PostRepository;
import com.example.board.security.OwnershipGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    /** 최신 글부터 페이지 단위로 조회한다. */
    @Transactional(readOnly = true)
    public Page<Post> list(int page, int size) {
        return posts.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
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
