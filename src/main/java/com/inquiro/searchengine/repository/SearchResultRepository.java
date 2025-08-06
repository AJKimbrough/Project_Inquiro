package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


// Extends JpaRepository, CRUD operations, custom query methods for SearchResult 
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    // OR Search: keyword OR title OR description (case-insensitive)
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
           OR LOWER(r.title) LIKE %:term%
           OR LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> orSearch(@Param("term") String term);

    // AND search: all of keyword, title, and description (case-insensitive).
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
          AND LOWER(r.title) LIKE %:term%
          AND LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> andSearch(@Param("term") String term);

    // NOT Search: none of keywords, title, or description (case-insensitive). ***Excludes results matching term.
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) NOT LIKE %:term%
          AND LOWER(r.title) NOT LIKE %:term%
          AND LOWER(r.description) NOT LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> notSearch(@Param("term") String term);

    // typo correction and autofill suggestions 
    @Query("SELECT DISTINCT LOWER(r.keyword) FROM SearchResult r")
    List<String> findKeywords();
}
