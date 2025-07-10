Inquiro 🔍
Inquiro is a lightweight, Java based search engine built with Spring Boot and Thymeleaf. It supports advanced search capabilities like boolean query modes (OR, AND, NOT), basic autocorrect functionality, and result deduplication. Designed for educational use, Inquiro demonstrates core search engine logic using Java, HTML, and a custom data model.

🚀 Features
- Search Modes: OR (default), AND, NOT
- AutoCorrect Engine for typo correction using Levenshtein distance
- Dual concurrent search engine execution
- Result deduplication by URL
- URL validation and pruning tool

🛠️ Deployment Instructions

📦 Prerequisites
• Java 17+
 • Maven 3.6+
 • Git

📁 Clone the Repository
git clone https://github.com/your-username/inquiro.git
 cd inquiro

⚙️ Build the Project
mvn clean install
▶️ Run the Application
mvn spring-boot:run
 Then open your browser and navigate to: http://localhost:8080

🧪 How to Use
1. Launch the application in a browser.
 2. Enter a search term like 'utd engineering'.
 3. Select your search mode (OR, AND, or NOT).
 4. Press Search.
 5. View results labeled by their source engine.


🏗️ Tech Stack
Backend: Spring Boot (Java 17)
 Frontend: Thymeleaf, HTML/CSS
 Data Access: Spring Data JPA
 Build Tool: Maven

🗃 Project Structure
src
 ├── controller    	# Web controllers 
 ├── dto           	# Data Transfer Object 
 ├── model       	  # JPA Entity 
 ├── repository    	# Spring JPA Repository interface
 ├── service       	# SearchService business logic
 ├── util          	# URL validation and utilities
 └── templates     	# Thymeleaf HTML templates

🧼 Maintenance Tools
You can call SearchService.deleteOutdatedUrls() manually or from a scheduled task to clean the database by checking for broken links.

📆 Phase Two (Coming Soon)
- User Login System: Account creation, login/logout, and session-based personalization.
 - Image Search: A new engine for indexing and returning relevant image-based results.
 - Search History: Logged-in users can view and revisit past search queries.

📜 License
This project is intended for academic and educational use.

