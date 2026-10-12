package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.board.common.NotFoundException;
import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import com.example.board.security.OwnershipGuard;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

class CommentServiceTest {

    private final CommentRepository comments = mock(CommentRepository.class);
    private final PostRepository posts = mock(PostRepository.class);
    private final OwnershipGuard guard = new OwnershipGuard();
    private final CommentService service = new CommentService(comments, new PostService(posts, guard), guard);

    @Test
    void add_toMissingPost_throwsNotFound() {
        when(posts.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.add(99L, user(1L), "댓글")).isInstanceOf(NotFoundException.class);
    }

    @Test
    void edit_byOwner_marksEdited() {
        User author = user(1L);
        Comment comment = new Comment(new Post(author, "제목", "내용"), author, "댓글");
        when(comments.findById(5L)).thenReturn(Optional.of(comment));

        service.edit(5L, author, "고친 댓글");

        assertThat(comment.getContent()).isEqualTo("고친 댓글");
        assertThat(comment.isEdited()).isTrue();
    }

    @Test
    void delete_byNonOwner_doesNotDelete() {
        User author = user(1L);
        Comment comment = new Comment(new Post(author, "제목", "내용"), author, "댓글");
        when(comments.findById(5L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> service.delete(5L, user(2L))).isInstanceOf(AccessDeniedException.class);
        verify(comments, never()).delete(comment);
    }

    private static User user(Long id) {
        User user = new User("u" + id + "@example.com", "user" + id, "USER");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
