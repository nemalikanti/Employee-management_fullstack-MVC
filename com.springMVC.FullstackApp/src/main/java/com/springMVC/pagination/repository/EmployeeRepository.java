package com.springMVC.pagination.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springMVC.pagination.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	// Custom query
	@Query(value = "select * from Employees e where e.firstName like %:keyword% or e.lastName like %:keyword% or e.email like %:keyword%", nativeQuery = true)
	List<Employee> search(@Param("keyword") String keyword);
	
	}
