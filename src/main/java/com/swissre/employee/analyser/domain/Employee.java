package com.swissre.employee.analyser.domain;

public class Employee {
	private int id;
	private String firstName;
	private String lastName;
	private int salary;
	private Integer managerId;

	public Employee(int id, String firstName, String lastName, int salary, Integer managerId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.managerId = managerId;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getSalary() {
		return salary;
	}

	public Integer getManagerId() {
		return managerId;
	}
}
