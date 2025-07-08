package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
           OR LOWER(r.title) LIKE %:term%
           OR LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> searchWithOr(@Param("term") String term);

    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
          AND LOWER(r.title) LIKE %:term%
          AND LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> searchWithAnd(@Param("term") String term);

    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) NOT LIKE %:term%
          AND LOWER(r.title) NOT LIKE %:term%
          AND LOWER(r.description) NOT LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> searchWithNot(@Param("term") String term);

    // 🔧 Add this to support typo correction/autofill
    @Query("SELECT DISTINCT LOWER(r.keyword) FROM SearchResult r")
    List<String> findAllKeywords();
}
