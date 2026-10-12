package com.example.board.repository;

import com.example.board.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 목록에서 작성자 닉네임을 쓰므로 작성자를 함께 읽어 N+1 을 없앤다
    @Query("select c from Comment c join fetch c.author where c.post.id = :postId")
    List<Comment> findWithAuthorByPostId(@Param("postId") Long postId);
}
