package com.example.board.web;

import com.example.board.security.CurrentUser;
import com.example.board.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CurrentUser currentUser;

    public PostController(PostService postService, CurrentUser currentUser) {
        this.postService = postService;
        this.currentUser = currentUser;
    }

    private static final int MAX_PAGE_SIZE = 50;

    @GetMapping
    public List<PostResponse> list(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return postService.list(Math.max(page, 0), safeSize).map(PostResponse::from).getContent();
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return PostResponse.from(postService.viewPost(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public PostResponse create(@Valid @RequestBody PostRequest request) {
        return PostResponse.from(postService.create(currentUser.get(), request.title(), request.content()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PostResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return PostResponse.from(postService.update(id, currentUser.get(), request.title(), request.content()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id) {
        postService.delete(id, currentUser.get());
    }
}
