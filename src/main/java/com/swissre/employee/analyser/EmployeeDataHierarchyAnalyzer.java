
package com.swissre.employee.analyser;

import com.swissre.employee.analyser.domain.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swissre.employee.analyser.utils.Constants.*;

public class EmployeeDataHierarchyAnalyzer {

    public static List<Employee> readEmployeesFromFile(String fileName) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith(FILE_COMMENT_LINE)) {
                    continue; //skipping commented lines
                }
                String[] parts = line.split(COMMA_SEPARATED_LINE);
                if(parts == null || parts.length < 4) {
                    System.out.println("Line is corrupted and no sufficient data "+line);
                }
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                int salary = Integer.parseInt(parts[3]);
                Integer managerId = null;
                if(parts.length > 4) {
                    managerId = Integer.parseInt(parts[4]);
                }
                employees.add(new Employee(id, firstName, lastName, salary, managerId));
            }
        }
        return employees;
    }

    public Map<Integer, List<Employee>> mapManagersToEmployees(List<Employee> employees) {
        Map<Integer, List<Employee>> managerToEmployees = new HashMap<>();
        for (Employee e : employees) {
            if (e.getManagerId() != null) {
                managerToEmployees.computeIfAbsent(e.getManagerId(), k -> new ArrayList<>()).add(e);
            }
        }
        return managerToEmployees;
    }

    public void analyzeSalaries(List<Employee> employeeList, Map<Integer, List<Employee>> managerToEmployees) {
        for (Map.Entry<Integer, List<Employee>> entry : managerToEmployees.entrySet()) {
            int managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();
            double avgSalary = subordinates.stream().mapToInt(Employee::getSalary).average().orElse(0);
            Employee manager = findEmployeeById(managerId, employeeList);
            if (manager != null) {
                double minAllowed = avgSalary * MIN_MULTIPLIER;
                double maxAllowed = avgSalary * MAX_MULTIPLIER;
                if (manager.getSalary() < minAllowed) {
                    System.out.printf("Manager %d earns less than they should by %.2f%n", managerId, minAllowed - manager.getSalary());
                } else if (manager.getSalary() > maxAllowed) {
                    System.out.printf("Manager %d earns more than they should by %.2f%n", managerId, manager.getSalary() - maxAllowed);
                }
            }
        }
    }

    public void analyzeReportingLines(List<Employee> employees, Map<Integer, List<Employee>> managerToEmployees) {
        for (Employee e : employees) {
            int depth = countReportingLineDepth(e, employees);
            if (depth > MAX_REPORTING_LINE) {
                System.out.printf("Employee %d has a reporting line that is too long by %d levels%n", e.getId(), depth - MAX_REPORTING_LINE);
            }
        }
    }

    public int countReportingLineDepth(Employee employee, List<Employee> employees) {
        int depth = 0;
        Integer managerId = employee.getManagerId();
        while (managerId != null) {
            depth++;
            managerId = findEmployeeById(managerId, employees).getManagerId();
        }
        return depth;
    }

    private Employee findEmployeeById(int id, List<Employee> employees) {
        return employees.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }
}
