package model;

import java.util.Objects;

public class Employee {
    private int id;
    private String name;
    private double salary;

    public Employee(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Converts employee record to CSV line for file storage: id,name,salary
     */
    public String toCsvString() {
        return id + "," + name + "," + String.format(java.util.Locale.US, "%.2f", salary);
    }

    /**
     * Parses a CSV line into an Employee object.
     * Returns null if the line is invalid or empty.
     */
    public static Employee fromCsvString(String line) {
        if (line == null) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null;
        }

        String[] parts = trimmed.split(",", -1);
        if (parts.length < 3) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            double salary = Double.parseDouble(parts[2].trim());
            return new Employee(name, id, salary);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Employee [ID=%d, Name='%s', Salary=₹%.2f]", id, name, salary);
    }
}