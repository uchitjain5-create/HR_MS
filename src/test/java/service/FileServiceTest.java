package service;

import model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should save and load employees accurately")
    void testSaveAndLoadEmployees() {
        Path filePath = tempDir.resolve("test_employees.txt");
        FileService fileService = new FileService(filePath.toString());

        List<Employee> original = List.of(
                new Employee("Snehil", 1, 100000.0),
                new Employee("Dhyey", 2, 200000.0),
                new Employee("Kartikey", 3, 110000.0)
        );

        boolean saved = fileService.saveEmployees(original);
        assertTrue(saved, "Saving should return true");

        List<Employee> loaded = fileService.loadEmployees();
        assertEquals(3, loaded.size(), "Loaded employee count should match");

        assertEquals(1, loaded.get(0).getId());
        assertEquals("Snehil", loaded.get(0).getName());
        assertEquals(100000.0, loaded.get(0).getSalary(), 0.001);

        assertEquals(2, loaded.get(1).getId());
        assertEquals("Dhyey", loaded.get(1).getName());
        assertEquals(200000.0, loaded.get(1).getSalary(), 0.001);

        assertEquals(3, loaded.get(2).getId());
        assertEquals("Kartikey", loaded.get(2).getName());
        assertEquals(110000.0, loaded.get(2).getSalary(), 0.001);
    }

    @Test
    @DisplayName("Should return empty list for non-existent file without crashing")
    void testLoadNonExistentFile() {
        Path filePath = tempDir.resolve("does_not_exist.txt");
        FileService fileService = new FileService(filePath.toString());

        List<Employee> loaded = fileService.loadEmployees();
        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    @DisplayName("Should parse CSV line and ignore comment or invalid lines")
    void testCsvParsing() {
        Employee valid = Employee.fromCsvString("10,Amit Sharma,75000.50");
        assertNotNull(valid);
        assertEquals(10, valid.getId());
        assertEquals("Amit Sharma", valid.getName());
        assertEquals(75000.50, valid.getSalary(), 0.001);

        // Comments, headers and invalid lines
        assertNull(Employee.fromCsvString("# Header line"));
        assertNull(Employee.fromCsvString(""));
        assertNull(Employee.fromCsvString("   "));
        assertNull(Employee.fromCsvString("not,a,valid,number"));
        assertNull(Employee.fromCsvString("1,OnlyTwoParts"));
    }
}
