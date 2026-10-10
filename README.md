# TASKATOR — Time Management & Task Prioritization System

A Java Swing desktop application for managing tasks, deadlines, priorities, time tracking and progress, backed by MariaDB through JDBC.

## Included
- Modern Swing desktop UI with dashboard, sidebar navigation and responsive layouts
- User/Admin login
- Task CRUD and validation
- Priority calculation based on importance and deadline
- Live time tracking with background thread
- Progress dashboard
- Admin center for categories, priority rules and system logs
- JDBC + PreparedStatement
- Transaction management with commit/rollback
- DAO, Service and Model layers
- OOP: inheritance, overriding, interface, polymorphism and exceptions
- Collections & Generics: List, Set, Map and generic Repository
- Multithreading and synchronization

## Requirements
- JDK 27 (or compatible JDK)
- MariaDB Server
- MariaDB Connector/J 3.5.10 (already included in `lib`)

## Database
Database name: `time_management_system`

The application uses the existing database schema and seed data. No database redesign is required.

Current connection configuration is in:
`src/util/DatabaseConnection.java`

If your local MariaDB username/password differs, change only the three connection constants there.

## Demo Accounts
- User: `harshit@example.com` / `user123`
- Admin: `admin@timemanager.com` / `admin123`

## Run quickly on Windows
Double-click:
`Run-Taskator.bat`

Or from the project folder:

```powershell
javac -cp "lib\mariadb-java-client-3.5.10.jar" -d bin src\util\DatabaseConnection.java src\interfaces\*.java src\model\*.java src\dao\*.java src\service\*.java src\exceptions\*.java src\threads\*.java src\gui\*.java src\App.java
java -cp "bin;lib\mariadb-java-client-3.5.10.jar" App
```

## Project structure
```text
Taskator/
├── lib/
│   └── mariadb-java-client-3.5.10.jar
├── src/
│   ├── dao/
│   ├── exceptions/
│   ├── gui/
│   ├── interfaces/
│   ├── model/
│   ├── service/
│   ├── threads/
│   ├── util/
│   └── App.java
├── Run-Taskator.bat
└── README.md
```


## Demo dashboard data

When a user opens the dashboard, Taskator automatically creates a small set of realistic student demo tasks (COA, Java Codekata, SQLkata, Oracle DBMS, Scilab Practical File, and DS Trees & Graphs) if they do not already exist for that user. This only inserts task records and does not modify the database schema. Progress values are intentionally mixed so the dashboard shows meaningful statistics.
