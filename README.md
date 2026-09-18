# Human Resource Management System (HRMS)

A robust, menu driven **Human Resource Management System** built with **Core Java** and **Maven**. Designed for clean object oriented architecture, complete file persistence, data validation, and automated unit testing.

---

## Project Overview

The **HRMS** project provides HR administrators with a streamlined console application to manage employee records, track salary and payroll information, search employee directories, and maintain persistent storage without external database overhead.

### Key Highlights
- **No Heavy GUI / Front-End Dependencies**: Fast, lightweight, and runs directly in any terminal environment.
- **Robust Input Validation**: Safely handles non numeric and out of range user entries without crashing.
- **Data Persistence**: Automatically reads and writes employee records to `data/employee.txt` in structured CSV format.
- **HR & Payroll Analytics**: Instant metrics on total employee count, total payroll, average salary, and top/lowest earners.
- **Unit Tested with JUnit 5**: Comprehensive test suite covering business logic, search, updates, and persistence.

---

## Features

1. **Add Employee**:
   - Validates unique Employee ID (prevents duplicate IDs).
   - Validates non empty names and non negative salary figures.
2. **View All Employees**:
   - Displays all registered staff members in a formatted ASCII table with ID, Name, and Salary in INR.
3. **Search Employee by ID**:
   - Fast lookup for an exact employee record.
4. **Search Employee by Name**:
   - Case-insensitive substring search (e.g., searching "him" matches "Himanshu").
5. **Update Employee**:
   - Flexible updates for Salary, Name, or both with validation.
6. **Remove Employee**:
   - Deletes employee record with confirmation prompt (`y/N`).
7. **HR & Payroll Statistics**:
   - Calculates total employees, total monthly payroll, average salary, highest paid employee, and lowest paid employee.
8. **File Storage & Persistence**:
   - Records are automatically loaded on application startup.
   - Manual save option (`[8] Save Records to File`) and auto save prompt upon exiting (`[0] Exit`).

---

## Project Structure

```text
HRMS/
├── pom.xml                               # Maven project configuration & plugins
├── README.md                             # Project documentation
├── data/
│   └── employee.txt                      # CSV file storing persistent employee records
└── src/
    ├── main/
    │   └── java/
    │       ├── app/
    │       │   └── Main.java             # Application entry point & interactive CLI menu
    │       ├── model/
    │       │   └── Employee.java         # Employee model class (encapsulation, CSV serialization)
    │       └── service/
    │           ├── EmployeeService.java  # Core business logic, search, updates, statistics
    │           └── FileService.java      # File persistence (buffered read/write, directory handling)
    └── test/
        └── java/
            └── service/
                ├── EmployeeServiceTest.java # JUnit 5 tests for business logic & payroll
                └── FileServiceTest.java     # JUnit 5 tests for file persistence & parsing
```

---

## Prerequisites

- **Java Development Kit (JDK)**: JDK 17, 21, or newer (built on Java 26)
- **Apache Maven**: 3.8+ or higher

Verify your environment:
```bash
java -version
mvn -version
```

---

## How to Build & Run

### 1. Compile the Project
```bash
mvn clean compile
```

### 2. Run the Unit Tests
```bash
mvn test
```

### 3. Run the Application
You can run the application directly using the Maven Exec plugin:
```bash
mvn exec:java
```

Or run via standard Java class path:
```bash
java -cp target/classes app.Main
```

---

## Sample Console Interface

```text
==================================================================
            HUMAN RESOURCE MANAGEMENT SYSTEM (HRMS)               
==================================================================
Successfully loaded 4 employee record(s) from data/employee.txt.

-------------------------- MAIN MENU ----------------------------
 [1] Add New Employee
 [2] View All Employees
 [3] Search Employee by ID
 [4] Search Employee by Name
 [5] Update Employee (Salary / Name)
 [6] Remove Employee
 [7] View HR & Payroll Statistics
 [8] Save Records to File
 [9] Reload Records from File
 [0] Exit Application
------------------------------------------------------------------
Enter your choice (0-9): 2

--- [2] View All Employees ---
+-------+--------------------------------+-----------------+
| ID    | Name                           | Salary (INR)    |
+-------+--------------------------------+-----------------+
| 1     | Snehil                         |       100000.00 |
| 2     | Dhyey                          |       200000.00 |
| 3     | Kartikey                       |       110000.00 |
| 4     | Kislay                         |       150000.00 |
+-------+--------------------------------+-----------------+
Total Employees: 4
```

---

## File Storage Format (`data/employee.txt`)

Records are stored as simple, human-readable comma-separated values:
```text
# ID,Name,Salary
1,Snehil,100000.00
2,Dhyey,200000.00
3,Kartikey,110000.00
4,Kislay,150000.00
```
