# 📚 Library Desk

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

**Library Desk** is a modern, web‑based library management system built for library staff. It streamlines daily operations such as managing the book catalog, registering members, issuing and returning books, and tracking fines — all from a single, professional control centre.

![Library Desk Dashboard](https://via.placeholder.com/800x400?text=Library+Desk+Screenshot)

## ✨ Features

- **📖 Catalog Management** – View all books with real‑time availability status.
- **👥 Member Directory** – Register new members and review their current loans and fines.
- **📤 Issue Books** – Assign titles to members with immediate validation and automatic due‑date calculation.
- **📥 Process Returns** – Close transactions and instantly display any overdue fines.
- **📊 Member Activity** – Check which books a member currently has on loan before follow‑up or return processing.
- **💰 Fine Tracking** – Automatically calculate and record fines for overdue returns.

## 🛠️ Technology Stack

| Layer       | Technology                              |
|-------------|-----------------------------------------|
| **Frontend**| JSP, HTML5, CSS3                        |
| **Backend** | Java 17, Jakarta Servlet 6.0, JSTL      |
| **Database**| MySQL 8.0                                |
| **Build**   | Maven                                    |
| **Server**  | Apache Tomcat 10+ (or any Jakarta EE 10 container) |

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

### Prerequisites

- **Java 17** or later
- **Apache Maven** 3.8+
- **MySQL** 8.0
- **Apache Tomcat** 10+ (or any Servlet 6.0 compatible server)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ayush-dot-146/Library-Desk.git
   cd Library-Desk/library-webapp

   Set up the database

Create a MySQL database (e.g., library_db).

Run the provided SQL script to create the members table and add the fine_amount column to the transactions table:

bash
mysql -u root -p library_db < database/member_schema_update.sql
(If you have additional schema files, execute them in the correct order.)

Configure database connection

Open src/main/java/com/library/db/DatabaseConnection.java.

Update the JDBC URL, username, and password according to your MySQL setup:

java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
Build the application

bash
mvn clean package
This will generate a WAR file in the target/ directory.

Deploy to Tomcat

Copy the generated WAR file to Tomcat's webapps/ folder.

Start (or restart) the Tomcat server.

Access the application
Open your browser and go to:

http://localhost:8080/library-webapp/

## 📁 Project Structure

```plaintext
library-webapp/
├── src/
│   └── main/
│       ├── java/com/library/
│       │   ├── db/              # Database connection utility
│       │   ├── exception/       # Custom exceptions (e.g., DatabaseException)
│       │   ├── model/           # Data models (Book, Member, Transaction)
│       │   ├── service/         # Business logic (LibraryService)
│       │   └── servlet/         # Request controllers (Issue, Return, ListBooks, etc.)
│       └── webapp/
│           ├── WEB-INF/         # Configuration and JSP views
│           ├── assets/          # Static resources (CSS, JS, images)
│           └── index.jsp        # Application dashboard
├── database/                    # SQL schema files
├── pom.xml                      # Maven configuration
└── README.md
```

🖥️ Usage Example
View Catalog – Navigate to the Catalog section to see all books and their available copies.

Register a Member – Go to Members → Register and enter the member’s details.

Issue a Book – Select Issue, enter the member ID and book ID. The system validates both and assigns a due date.

Process a Return – Select Return, enter the transaction ID. The system closes the transaction and shows any fine incurred.

Check Member Activity – Use Member Activity to see all books currently held by a specific member.


🤝 Contributing
Contributions are welcome! If you have ideas for new features, improvements, or bug fixes, please follow these steps:

Fork the repository.

Create a new branch (git checkout -b feature/amazing-feature).

Commit your changes (git commit -m 'Add some amazing feature').

Push to the branch (git push origin feature/amazing-feature).

Open a Pull Request.


📄 License
This project is licensed under the MIT License - see the LICENSE file for details.


👤 Author
Ayush-dot-146 – GitHub Profile
