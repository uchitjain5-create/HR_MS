package app;

import model.Employee;
import service.EmployeeService;
import service.FileService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private final EmployeeService employeeService;
    private final FileService fileService;
    private final Scanner scanner;

    public Main() {
        this.employeeService = new EmployeeService();
        this.fileService = new FileService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        printBanner();
        loadInitialData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice (0-9): ");
            System.out.println();

            switch (choice) {
                case 1 -> handleAddEmployee();
                case 2 -> handleViewAllEmployees();
                case 3 -> handleSearchById();
                case 4 -> handleSearchByName();
                case 5 -> handleUpdateEmployee();
                case 6 -> handleRemoveEmployee();
                case 7 -> handleViewStatistics();
                case 8 -> handleSaveToFile();
                case 9 -> handleReloadFromFile();
                case 0 -> {
                    handleExit();
                    running = false;
                }
                default -> System.out.println("Invalid option! Please select a number between 0 and 9.");
            }

            if (running) {
                System.out.println();
                promptEnterToContinue();
            }
        }
    }

    private void printBanner() {
        System.out.println("==================================================================");
        System.out.println("            HUMAN RESOURCE MANAGEMENT SYSTEM (HRMS)               ");
        System.out.println("==================================================================");
    }

    private void printMenu() {
        System.out.println("\n-------------------------- MAIN MENU ----------------------------");
        System.out.println(" [1] Add New Employee");
        System.out.println(" [2] View All Employees");
        System.out.println(" [3] Search Employee by ID");
        System.out.println(" [4] Search Employee by Name");
        System.out.println(" [5] Update Employee (Salary / Name)");
        System.out.println(" [6] Remove Employee");
        System.out.println(" [7] View HR & Payroll Statistics");
        System.out.println(" [8] Save Records to File");
        System.out.println(" [9] Reload Records from File");
        System.out.println(" [0] Exit Application");
        System.out.println("------------------------------------------------------------------");
    }

    private void loadInitialData() {
        List<Employee> loaded = fileService.loadEmployees();
        if (!loaded.isEmpty()) {
            employeeService.setEmployees(loaded);
            System.out.println("Successfully loaded " + loaded.size() + " employee record(s) from " + fileService.getFilePath() + ".");
        } else {
            // Seed initial records if file was empty or new
            System.out.println("No existing employee file found. Seeding initial records...");
            employeeService.addEmployee(new Employee("Snehil", 1, 100000.0));
            employeeService.addEmployee(new Employee("Dhyey", 2, 200000.0));
            employeeService.addEmployee(new Employee("Kartikey", 3, 110000.0));
            employeeService.addEmployee(new Employee("Kislay", 4, 150000.0));
            fileService.saveEmployees(employeeService.getAllEmployees());
            System.out.println("Initialized " + employeeService.getEmployeeCount() + " sample employees and saved to " + fileService.getFilePath() + ".");
        }
    }

    private void handleAddEmployee() {
        System.out.println("--- [1] Add New Employee ---");
        int id = readPositiveInt("Enter Employee ID: ");

        if (employeeService.findEmployeeById(id) != null) {
            System.out.println("Error: An employee with ID " + id + " already exists!");
            return;
        }

        String name = readNonEmptyString("Enter Employee Name: ");
        double salary = readNonNegativeDouble("Enter Employee Salary (INR): ");

        Employee emp = new Employee(name, id, salary);
        boolean added = employeeService.addEmployee(emp);

        if (added) {
            System.out.println("Success: Employee added successfully!");
            System.out.println(emp);
        } else {
            System.out.println("Error: Could not add employee.");
        }
    }

    private void handleViewAllEmployees() {
        System.out.println("--- [2] View All Employees ---");
        employeeService.displayEmployees();
        System.out.println("Total Employees: " + employeeService.getEmployeeCount());
    }

    private void handleSearchById() {
        System.out.println("--- [3] Search Employee by ID ---");
        int id = readPositiveInt("Enter Employee ID to search: ");
        Employee emp = employeeService.findEmployeeById(id);

        if (emp != null) {
            System.out.println("Employee Found:");
            EmployeeService.printEmployeeTable(List.of(emp));
        } else {
            System.out.println("No employee found with ID " + id + ".");
        }
    }

    private void handleSearchByName() {
        System.out.println("--- [4] Search Employee by Name ---");
        String query = readNonEmptyString("Enter name or partial name: ");
        List<Employee> results = employeeService.findEmployeesByName(query);

        if (!results.isEmpty()) {
            System.out.println("Found " + results.size() + " matching employee(s):");
            EmployeeService.printEmployeeTable(results);
        } else {
            System.out.println("No employees found matching: \"" + query + "\".");
        }
    }

    private void handleUpdateEmployee() {
        System.out.println("--- [5] Update Employee ---");
        int id = readPositiveInt("Enter Employee ID to update: ");
        Employee emp = employeeService.findEmployeeById(id);

        if (emp == null) {
            System.out.println("No employee found with ID " + id + ".");
            return;
        }

        System.out.println("Current Details:");
        EmployeeService.printEmployeeTable(List.of(emp));

        System.out.println("Select what to update:");
        System.out.println(" 1. Update Salary");
        System.out.println(" 2. Update Name");
        System.out.println(" 3. Update Both");
        System.out.println(" 0. Cancel");
        int option = readInt("Enter choice (0-3): ");

        switch (option) {
            case 1 -> {
                double newSalary = readNonNegativeDouble("Enter new Salary (INR): ");
                if (employeeService.updateSalary(id, newSalary)) {
                    System.out.println("Salary updated successfully.");
                } else {
                    System.out.println("Failed to update salary.");
                }
            }
            case 2 -> {
                String newName = readNonEmptyString("Enter new Name: ");
                if (employeeService.updateName(id, newName)) {
                    System.out.println("Name updated successfully.");
                } else {
                    System.out.println("Failed to update name.");
                }
            }
            case 3 -> {
                String newName = readNonEmptyString("Enter new Name: ");
                double newSalary = readNonNegativeDouble("Enter new Salary (INR): ");
                employeeService.updateName(id, newName);
                employeeService.updateSalary(id, newSalary);
                System.out.println("Employee details updated successfully.");
            }
            case 0 -> System.out.println("Update cancelled.");
            default -> System.out.println("Invalid option selected.");
        }

        Employee updated = employeeService.findEmployeeById(id);
        if (updated != null) {
            System.out.println("Updated Details:");
            EmployeeService.printEmployeeTable(List.of(updated));
        }
    }

    private void handleRemoveEmployee() {
        System.out.println("--- [6] Remove Employee ---");
        int id = readPositiveInt("Enter Employee ID to remove: ");
        Employee emp = employeeService.findEmployeeById(id);

        if (emp == null) {
            System.out.println("No employee found with ID " + id + ".");
            return;
        }

        System.out.println("Record to be deleted:");
        EmployeeService.printEmployeeTable(List.of(emp));

        System.out.print("Are you sure you want to delete this employee? (y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean removed = employeeService.removeEmployee(id);
            if (removed) {
                System.out.println("Employee ID " + id + " has been successfully removed.");
            } else {
                System.out.println("Failed to remove employee.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void handleViewStatistics() {
        System.out.println("--- [7] HR & Payroll Statistics ---");
        int count = employeeService.getEmployeeCount();
        if (count == 0) {
            System.out.println("No employee data available.");
            return;
        }

        double totalPayroll = employeeService.getTotalPayroll();
        double avgSalary = employeeService.getAverageSalary();
        Employee highest = employeeService.getHighestPaidEmployee();
        Employee lowest = employeeService.getLowestPaidEmployee();

        System.out.println("+-----------------------------+-----------------------------+");
        System.out.printf("| %-27s | %-27s |%n", "Metric", "Value");
        System.out.println("+-----------------------------+-----------------------------+");
        System.out.printf("| %-27s | %-27d |%n", "Total Employees", count);
        System.out.printf("| %-27s | INR %23.2f |%n", "Total Monthly Payroll", totalPayroll);
        System.out.printf("| %-27s | INR %23.2f |%n", "Average Salary", avgSalary);
        if (highest != null) {
            System.out.printf("| %-27s | %s (INR %.2f) |%n", "Highest Paid Employee", highest.getName(), highest.getSalary());
        }
        if (lowest != null) {
            System.out.printf("| %-27s | %s (INR %.2f) |%n", "Lowest Paid Employee", lowest.getName(), lowest.getSalary());
        }
        System.out.println("+-----------------------------+-----------------------------+");
    }

    private void handleSaveToFile() {
        System.out.println("--- [8] Save Records to File ---");
        boolean saved = fileService.saveEmployees(employeeService.getAllEmployees());
        if (saved) {
            System.out.println("Successfully saved " + employeeService.getEmployeeCount() + " records to " + fileService.getFilePath() + ".");
        } else {
            System.out.println("Failed to save records to file.");
        }
    }

    private void handleReloadFromFile() {
        System.out.println("--- [9] Reload Records from File ---");
        List<Employee> list = fileService.loadEmployees();
        employeeService.setEmployees(list);
        System.out.println("Reloaded " + list.size() + " record(s) from " + fileService.getFilePath() + ".");
    }

    private void handleExit() {
        System.out.println("--- [0] Exiting Application ---");
        System.out.print("Do you want to save current changes to file before exiting? (Y/n): ");
        String answer = scanner.nextLine().trim();
        if (answer.isEmpty() || answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
            fileService.saveEmployees(employeeService.getAllEmployees());
            System.out.println("Changes saved to " + fileService.getFilePath() + ".");
        }
        System.out.println("\nThank you for using Human Resource Management System (HRMS). Goodbye!");
    }

    // Helper input validation methods
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than 0.");
        }
    }

    private double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0) {
                    return val;
                }
                System.out.println("Salary cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    private void promptEnterToContinue() {
        System.out.print("Press [Enter] to return to main menu...");
        scanner.nextLine();
    }
}