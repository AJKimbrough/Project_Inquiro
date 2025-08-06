package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchTimeDesc(User user);
    void deleteByUser(User user);
}
