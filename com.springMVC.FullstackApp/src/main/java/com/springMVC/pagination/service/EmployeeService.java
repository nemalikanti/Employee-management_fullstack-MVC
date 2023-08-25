package com.springMVC.pagination.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springMVC.pagination.model.Employee;



public interface EmployeeService {
	List<Employee> getAllEmployees();

	void saveEmployee(Employee employee);

	Employee getEmployeeById(long id);
	
	Employee updateEmployee(Employee employee);

	void deleteEmployeeById(long id);

	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	List<Employee> listAll(String keyword);
	
	List<Employee> listAll();

	
}
