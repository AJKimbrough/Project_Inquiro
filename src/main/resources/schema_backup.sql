CREATE TABLE search_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(255),
    title VARCHAR(255),
    url VARCHAR(255),
    description TEXT
);
