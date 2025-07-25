package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


// Extends JpaRepository to provide CRUD operations and custom query methods for the SearchResult entity
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    // Searches results keyword OR title OR description is found in the query (case-insensitive)
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
           OR LOWER(r.title) LIKE %:term%
           OR LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> orSearch(@Param("term") String term);

    // Searches results ALL of keyword, title, and description contain the query (case-insensitive).
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) LIKE %:term%
          AND LOWER(r.title) LIKE %:term%
          AND LOWER(r.description) LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> andSearch(@Param("term") String term);

    // Searches results, NONE of keywords, title, or description contain term (case-insensitive). Excludes results matching term.
    @Query("""
        SELECT r FROM SearchResult r
        WHERE LOWER(r.keyword) NOT LIKE %:term%
          AND LOWER(r.title) NOT LIKE %:term%
          AND LOWER(r.description) NOT LIKE %:term%
        ORDER BY r.title ASC
    """)
    List<SearchResult> notSearch(@Param("term") String term);

    // Used for typo correction and autofill suggestions and returns a distinct list of all lowercase keywords stored in the database. 
    @Query("SELECT DISTINCT LOWER(r.keyword) FROM SearchResult r")
    List<String> findKeywords();
}
