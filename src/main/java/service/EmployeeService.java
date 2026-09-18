package service;

import model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeService {

    private final ArrayList<Employee> employees = new ArrayList<>();

    /**
     * Adds an employee if an employee with the same ID does not already exist.
     *
     * @param employee employee to add
     * @return true if added successfully, false if duplicate ID
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            return false;
        }

        int id = employee.getId();
        Employee existing = findEmployeeById(id);

        if (existing != null) {
            return false;
        }

        employees.add(employee);
        return true;
    }

    /**
     * Returns an unmodifiable copy of all current employees.
     */
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(new ArrayList<>(employees));
    }

    /**
     * Replaces the current list of employees (used when loading from storage).
     */
    public void setEmployees(List<Employee> loadedEmployees) {
        employees.clear();
        if (loadedEmployees != null) {
            for (Employee e : loadedEmployees) {
                if (e != null && findEmployeeById(e.getId()) == null) {
                    employees.add(e);
                }
            }
        }
    }

    /**
     * Finds an employee by their unique ID.
     */
    public Employee findEmployeeById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Searches for employees whose names contain the given query (case-insensitive).
     */
    public List<Employee> findEmployeesByName(String nameQuery) {
        List<Employee> result = new ArrayList<>();
        if (nameQuery == null || nameQuery.trim().isEmpty()) {
            return result;
        }

        String queryLower = nameQuery.trim().toLowerCase();
        for (Employee e : employees) {
            if (e.getName() != null && e.getName().toLowerCase().contains(queryLower)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Updates an employee's salary.
     */
    public boolean updateSalary(int id, double newSalary) {
        if (newSalary < 0) {
            return false;
        }
        Employee e = findEmployeeById(id);
        if (e == null) {
            return false;
        }
        e.setSalary(newSalary);
        return true;
    }

    /**
     * Updates an employee's name.
     */
    public boolean updateName(int id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return false;
        }
        Employee e = findEmployeeById(id);
        if (e == null) {
            return false;
        }
        e.setName(newName.trim());
        return true;
    }

    /**
     * Removes an employee by their ID.
     */
    public boolean removeEmployee(int id) {
        Employee existingEmployee = findEmployeeById(id);
        if (existingEmployee != null) {
            employees.remove(existingEmployee);
            return true;
        }
        return false;
    }

    /**
     * Returns total number of registered employees.
     */
    public int getEmployeeCount() {
        return employees.size();
    }

    /**
     * Returns total monthly payroll across all employees.
     */
    public double getTotalPayroll() {
        double total = 0.0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

    /**
     * Returns average salary of employees.
     */
    public double getAverageSalary() {
        if (employees.isEmpty()) {
            return 0.0;
        }
        return getTotalPayroll() / employees.size();
    }

    /**
     * Returns the employee with the highest salary, or null if list is empty.
     */
    public Employee getHighestPaidEmployee() {
        if (employees.isEmpty()) {
            return null;
        }
        Employee highest = employees.get(0);
        for (Employee e : employees) {
            if (e.getSalary() > highest.getSalary()) {
                highest = e;
            }
        }
        return highest;
    }

    /**
     * Returns the employee with the lowest salary, or null if list is empty.
     */
    public Employee getLowestPaidEmployee() {
        if (employees.isEmpty()) {
            return null;
        }
        Employee lowest = employees.get(0);
        for (Employee e : employees) {
            if (e.getSalary() < lowest.getSalary()) {
                lowest = e;
            }
        }
        return lowest;
    }

    /**
     * Displays all employees formatted in a clean ASCII table.
     */
    public void displayEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found in the system.");
            return;
        }

        printEmployeeTable(employees);
    }

    /**
     * Helper method to print an employee list in a formatted ASCII table.
     */
    public static void printEmployeeTable(List<Employee> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }

        String border = "+-------+--------------------------------+-----------------+";
        System.out.println(border);
        System.out.printf("| %-5s | %-30s | %-15s |%n", "ID", "Name", "Salary (INR)");
        System.out.println(border);

        for (Employee e : list) {
            System.out.printf("| %-5d | %-30s | %15.2f |%n",
                    e.getId(),
                    e.getName(),
                    e.getSalary());
        }
        System.out.println(border);
    }
}
