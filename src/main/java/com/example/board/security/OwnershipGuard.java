package com.example.board.security;

import com.example.board.domain.Post;
import com.example.board.domain.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/** 작성자만 수정·삭제할 수 있다. */
@Component
public class OwnershipGuard {

    public void requireOwner(Post post, User user) {
        if (!post.isWrittenBy(user)) {
            throw new AccessDeniedException("작성자만 변경할 수 있습니다");
        }
    }
}
