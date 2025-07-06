package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE LOWER(CONCAT('%', :keywordPart, '%'))
          AND (
               LOWER(r.title) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(r.description) LIKE LOWER(CONCAT('%', :term, '%'))
          )
        ORDER BY r.id ASC
    """)
    List<SearchResult> searchFlexible(
        @Param("keywordPart") String keywordPart,
        @Param("term") String term
    );
}
