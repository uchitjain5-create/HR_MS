package service;

import model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();
    }

    @Test
    @DisplayName("Should successfully add unique employees")
    void testAddEmployeeSuccess() {
        Employee e1 = new Employee("Snehil", 1, 100000);
        assertTrue(service.addEmployee(e1));
        assertEquals(1, service.getEmployeeCount());
        assertEquals(e1, service.findEmployeeById(1));
    }

    @Test
    @DisplayName("Should reject adding employee with duplicate ID")
    void testAddDuplicateEmployee() {
        Employee e1 = new Employee("Snehil", 1, 100000);
        Employee e2 = new Employee("Another Snehil", 1, 90000);

        assertTrue(service.addEmployee(e1));
        assertFalse(service.addEmployee(e2), "Adding duplicate ID should return false");
        assertEquals(1, service.getEmployeeCount());
    }

    @Test
    @DisplayName("Should find employee by ID")
    void testFindEmployeeById() {
        Employee e1 = new Employee("Dhyey", 2, 200000);
        service.addEmployee(e1);

        Employee found = service.findEmployeeById(2);
        assertNotNull(found);
        assertEquals("Dhyey", found.getName());

        assertNull(service.findEmployeeById(99), "Non-existent ID should return null");
    }

    @Test
    @DisplayName("Should search employees by name (case-insensitive substring)")
    void testFindEmployeesByName() {
        service.addEmployee(new Employee("Kartikey", 3, 110000));
        service.addEmployee(new Employee("Kislay", 4, 150000));
        service.addEmployee(new Employee("Arun Kumar", 5, 80000));

        List<Employee> kMatches = service.findEmployeesByName("k");
        assertEquals(3, kMatches.size(), "Should find Kartikey, Kislay, and Arun Kumar");

        List<Employee> layMatches = service.findEmployeesByName("LAY");
        assertEquals(1, layMatches.size());
        assertEquals("Kislay", layMatches.get(0).getName());

        List<Employee> emptyMatches = service.findEmployeesByName("NonExistent");
        assertTrue(emptyMatches.isEmpty());
    }

    @Test
    @DisplayName("Should update salary for existing employee")
    void testUpdateSalary() {
        Employee e1 = new Employee("Snehil", 1, 100000);
        service.addEmployee(e1);

        assertTrue(service.updateSalary(1, 125000));
        assertEquals(125000, service.findEmployeeById(1).getSalary(), 0.001);

        assertFalse(service.updateSalary(999, 150000), "Updating salary of non-existent ID should return false");
        assertFalse(service.updateSalary(1, -500), "Negative salary should return false");
    }

    @Test
    @DisplayName("Should update employee name")
    void testUpdateName() {
        Employee e1 = new Employee("Snehil", 1, 100000);
        service.addEmployee(e1);

        assertTrue(service.updateName(1, "Snehil Sahay"));
        assertEquals("Snehil Sahay", service.findEmployeeById(1).getName());

        assertFalse(service.updateName(1, "   "), "Blank name should fail");
        assertFalse(service.updateName(999, "Ghost"), "Non-existent employee should fail");
    }

    @Test
    @DisplayName("Should remove employee by ID")
    void testRemoveEmployee() {
        Employee e1 = new Employee("Snehil", 1, 100000);
        service.addEmployee(e1);

        assertTrue(service.removeEmployee(1));
        assertEquals(0, service.getEmployeeCount());
        assertNull(service.findEmployeeById(1));

        assertFalse(service.removeEmployee(1), "Removing already removed employee should return false");
    }

    @Test
    @DisplayName("Should accurately calculate payroll statistics")
    void testPayrollStatistics() {
        service.addEmployee(new Employee("E1", 1, 50000));
        service.addEmployee(new Employee("E2", 2, 70000));
        service.addEmployee(new Employee("E3", 3, 120000));

        assertEquals(3, service.getEmployeeCount());
        assertEquals(240000.0, service.getTotalPayroll(), 0.001);
        assertEquals(80000.0, service.getAverageSalary(), 0.001);

        Employee highest = service.getHighestPaidEmployee();
        assertNotNull(highest);
        assertEquals("E3", highest.getName());

        Employee lowest = service.getLowestPaidEmployee();
        assertNotNull(lowest);
        assertEquals("E1", lowest.getName());
    }
}
