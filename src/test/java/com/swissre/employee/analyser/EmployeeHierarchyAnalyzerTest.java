package com.swissre.employee.analyser;

import com.swissre.employee.analyser.domain.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class EmployeeHierarchyAnalyzerTest {

    @Test
    public void testCountReportingLineDepth() {
        Employee ceo = new Employee(123, "Joe", "Doe", 60000, null);
        Employee manager = new Employee(124, "Martin", "Chekov", 45000, 123);
        Employee employee = new Employee(125, "Bob", "Ronstad", 47000, 124);

        List<Employee> employees = Arrays.asList(ceo, manager, employee);
        EmployeeDataHierarchyAnalyzer analyzer = new EmployeeDataHierarchyAnalyzer();
        int depth = analyzer.countReportingLineDepth(employee, employees);

        assertEquals(2, depth);
    }

    @Test
    public void testSalaryAnalysis() {
        Employee manager = new Employee(124, "Martin", "Chekov", 45000, 123);
        Employee subordinate = new Employee(125, "Bob", "Ronstad", 30000, 124);

        Map<Integer, List<Employee>> managerToEmployees = new HashMap<>();
        managerToEmployees.put(124, Arrays.asList(subordinate));
        EmployeeDataHierarchyAnalyzer analyzer = new EmployeeDataHierarchyAnalyzer();
        analyzer.analyzeSalaries(managerToEmployees);
    }
}
