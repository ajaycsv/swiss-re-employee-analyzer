package com.swissre.employee.generator;

import com.swissre.employee.analyser.EmployeeDataHierarchyAnalyzer;
import com.swissre.employee.analyser.domain.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EmployeeReportGenerator {

    public static void main(String[] args) {
        EmployeeDataHierarchyAnalyzer analyzer = new EmployeeDataHierarchyAnalyzer();
        List<Employee> employeeList = null;
        try {
            employeeList = analyzer.readEmployeesFromFile("employees.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, List<Employee>> managerMapToEmployees = analyzer.mapManagersToEmployees(employeeList);
        analyzer.analyzeSalaries(employeeList, managerMapToEmployees);
        analyzer.analyzeReportingLines(employeeList, managerMapToEmployees);
    }
}
