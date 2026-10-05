package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.board.common.NotFoundException;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.PostRepository;
import com.example.board.security.OwnershipGuard;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

class PostServiceTest {

    private final PostRepository posts = mock(PostRepository.class);
    private final PostService service = new PostService(posts, new OwnershipGuard());

    @Test
    void update_byNonOwner_throwsAccessDenied() {
        User owner = user(1L);
        User other = user(2L);
        when(posts.findById(10L)).thenReturn(Optional.of(new Post(owner, "제목", "내용")));

        assertThatThrownBy(() -> service.update(10L, other, "새 제목", "새 내용"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void update_byOwner_changesTitleAndContent() {
        User owner = user(1L);
        Post post = new Post(owner, "제목", "내용");
        when(posts.findById(10L)).thenReturn(Optional.of(post));

        service.update(10L, owner, "새 제목", "새 내용");

        assertThat(post.getTitle()).isEqualTo("새 제목");
        assertThat(post.getUpdatedAt()).isNotNull();
    }

    @Test
    void delete_byNonOwner_doesNotDelete() {
        User owner = user(1L);
        Post post = new Post(owner, "제목", "내용");
        when(posts.findById(10L)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> service.delete(10L, user(2L))).isInstanceOf(AccessDeniedException.class);
        verify(posts, never()).delete(post);
    }

    @Test
    void viewPost_missingPost_throwsNotFound() {
        when(posts.incrementViewCount(99L)).thenReturn(0);

        assertThatThrownBy(() -> service.viewPost(99L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void list_requestsNewestFirst() {
        when(posts.findAll(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"))))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(service.list(0, 20).getContent()).isEmpty();
    }

    private static User user(Long id) {
        User user = new User("u" + id + "@example.com", "user" + id, "USER");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
