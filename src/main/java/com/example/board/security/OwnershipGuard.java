package com.example.board.security;

import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/** 작성자만 수정·삭제할 수 있다. 관리자는 운영을 위해 예외로 허용한다. */
@Component
public class OwnershipGuard {

    private static final String ADMIN = "ADMIN";

    public void requireOwner(Post post, User user) {
        if (!post.isWrittenBy(user) && !isAdmin(user)) {
            throw new AccessDeniedException("작성자만 변경할 수 있습니다");
        }
    }

    public void requireOwner(Comment comment, User user) {
        if (!comment.isWrittenBy(user)) {
            throw new AccessDeniedException("작성자만 변경할 수 있습니다");
        }
    }

    private static boolean isAdmin(User user) {
        return ADMIN.equals(user.getRole());
    }
}
