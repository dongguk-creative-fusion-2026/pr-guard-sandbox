package com.example.board.repository;

import com.example.board.domain.Post;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

/** 제목·본문 검색. 정렬 조건이 화면마다 달라서 쿼리를 직접 만든다. */
@Repository
public class PostSearchRepository {

    private final EntityManager em;

    public PostSearchRepository(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<Post> search(String keyword, String sort) {
        String orderBy = "latest".equals(sort) ? "created_at DESC" : "view_count DESC";
        String sql = "SELECT * FROM posts WHERE title LIKE '%" + keyword + "%' OR content LIKE '%" + keyword
                + "%' ORDER BY " + orderBy + " LIMIT 50";
        return em.createNativeQuery(sql, Post.class).getResultList();
    }
}
