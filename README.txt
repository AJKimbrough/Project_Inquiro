Inquiro 🔍
Inquiro is a lightweight, Java based search engine built with Spring Boot and Thymeleaf. It supports advanced search capabilities like boolean query modes (OR, AND, NOT), basic autocorrect functionality, and result deduplication. Designed for educational use, Inquiro demonstrates core search engine logic using Java, HTML, and a custom data model.

🌐 Live Deployment
   Inquiro is live and accessible at:

🔗 https://project-inquiro.onrender.com/

    - No installation needed — simply visit the URL and start searching.
    - Demo credentials for login are provided below.

🚀 Features
    -  Boolean Search Modes: OR (default), AND, and NOT
    -  AutoCorrect Engine: Fix typos using Levenshtein distance
    -  Concurrent Search: Runs both ExactMatch and AutoCorrect engines in parallel
    -  Result Deduplication: Filters duplicate results based on URL
    -  URL Validation: Checks if URLs are reachable
    -  Search History Logging: Tracks query, mode, timestamp per user
    -  Role-Based Access: Admin vs Guest functionality


📦 Prerequisites
• Java 17+
 • Maven 3.6+
 • MySQL 8+
 • Git

📁 Clone the Repository
git clone https://github.com/your-username/inquiro.git
 cd inquiro

🗃️ MySQL Database Setup
To run Inquiro with a MySQL backend, follow these steps:
1. Launch MySQL Server
    - Make sure your MySQL server is running. You can start it via terminal or a GUI tool like MySQL Workbench, 
2. Import the Existing Database
    - In the root of the project, there's a pre-configured SQL file:
        -- search_results.sql
This contains the full schema and sample data.
To import:
    -bash
    -Copy
    -Edit
    -mysql -u your_mysql_user -p your_database_name < search_results.sql
        -- Replace your_mysql_user with your MySQL username (e.g., root).
        -- Replace your_database_name with the name of the database you created (e.g., inquiro_db).
3. Update application.properties
    - Open src/main/resources/application.properties and ensure it matches your database settings


⚙️ Build the Project
mvn clean install
▶️ Run the Application
mvn spring-boot:run
 Then open your browser and navigate to: http://localhost:8080

🧪 How to Use
 1. Launch the application in a browser.
 2. Login via role
    - Admin 
        --username: admin
        --password: password123
    - Guest
        --username: guest
        -- password: paswword123
 3. Enter a search term like 'utd engineering'.
 4. Select your search mode (OR, AND, or NOT).
 5. Press Search.
 6. View results labeled by their source engine.

 Extended Funcitonality:
 Admin users may access the Admin Page. Only visible when admin is logged in.
    - URL validation
 All users can view search History
    - listed by search, query with date and time


🏗️ Tech Stack
 Backend: Spring Boot (Java 17)
 Frontend: Thymeleaf, HTML/CSS
 Database: MySQL + Spring Data JPA
 Data Access: Spring Data JPA
 Build Tool: Maven
 Testing: JUnit 5 + Mockito

🗃 Project Structure
src
 ├── controller    	# Web controllers 
 ├── dto           	# Data Transfer Object 
 ├── model       	  # JPA Entity 
 ├── repository    	# Spring JPA Repository interface
 ├── service       	# SearchService business logic
 ├── util          	# URL validation and utilities
 └── templates     	# Thymeleaf HTML templates
 └── test        	# Unit and integration tests

🧼 Maintenance Tools
You can call SearchService.deleteOutdatedUrls() manually or from a scheduled task to clean the database by checking for broken links.

🧭 Future Enhancements
    - Image Search Engine: Index and return images based on queries
    - Third-party API Integration: Integrate with external search APIs for broader results
    - Usage analytics and performance monitoring

📜 License
This project is intended for academic and educational use.

