package service;

import model.Employee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static final String DEFAULT_FILE_PATH = "data/employee.txt";

    private final String filePath;

    public FileService() {
        this(DEFAULT_FILE_PATH);
    }

    public FileService(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * Saves list of employees to file in CSV format.
     *
     * @param employees the list of employees to save
     * @return true if save succeeded, false otherwise
     */
    public boolean saveEmployees(List<Employee> employees) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# ID,Name,Salary");
            writer.newLine();

            if (employees != null) {
                for (Employee e : employees) {
                    if (e != null) {
                        writer.write(e.toCsvString());
                        writer.newLine();
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving employees to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads employees from file.
     *
     * @return list of loaded employees (never null, may be empty)
     */
    public List<Employee> loadEmployees() {
        List<Employee> list = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Employee employee = Employee.fromCsvString(line);
                if (employee != null) {
                    list.add(employee);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employees from file: " + e.getMessage());
        }

        return list;
    }

    /**
     * Legacy helper method preserved for backward compatibility.
     */
    public void save() {
        System.out.println("Use saveEmployees(List<Employee>) to persist current employee list.");
    }
}