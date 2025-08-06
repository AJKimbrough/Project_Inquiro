package com.inquiro.searchengine.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String keyword;

    @Column(nullable = false, length = 10)
    private String mode;

    @CreationTimestamp
    @Column(name = "search_time", updatable = false)
    private LocalDateTime searchTime;

    public SearchHistory() {}
    public SearchHistory(User user, String keyword, String mode) {
        this.user = user;
        this.keyword = keyword;
        this.mode = mode;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public LocalDateTime getSearchTime() { return searchTime; }

    public String getMode() { 
    return mode; 
}
    public void setMode(String mode) { 
     this.mode = mode; 
    }
}
