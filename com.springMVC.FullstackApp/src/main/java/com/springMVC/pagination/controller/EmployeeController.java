package com.springMVC.pagination.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.lowagie.text.DocumentException;
import com.springMVC.pagination.model.Employee;
import com.springMVC.pagination.service.EmployeeService;
import com.springMVC.pagination.util.ExcelExporter;
import com.springMVC.pagination.util.PDFExport;

@Controller
@RequestMapping
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	// display list of employees
	@GetMapping("/employee")
	public String viewHomePage(Model model) {
		return findPaginated(1, "id", "asc", model);
	}

	@GetMapping("/employee/new")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}

	@PostMapping("/employee/save")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		// save employee to database
		employeeService.saveEmployee(employee);
		return "redirect:/employee";
	}

	@GetMapping("/employee/{id}")
	public String showFormForUpdate(@PathVariable long id , Model model) {

		// get employee from the service
		Employee employee = employeeService.getEmployeeById(id);

		// set employee as a model attribute to pre-populate the form
		model.addAttribute("employee", employee);
		employeeService.updateEmployee(employee);
		return "update_employee";
	}

	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id) {

		// call delete employee method
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/employee";
	}

	@GetMapping("/employee/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 8;

		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listEmployees", listEmployees);
		return "index";
	}

	@GetMapping("/employee/search")
	public String search(Model model, @Param("keyword") String keyword) {
		List<Employee> listEmployees = employeeService.listAll(keyword);

		model.addAttribute("listEmployees", listEmployees);
		model.addAttribute("keyword", keyword);

		return "index";

	}
	
	
	@GetMapping("/employee/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Employees_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Employee> listEmployee = employeeService.listAll();
         
        ExcelExporter excelExporter = new ExcelExporter(listEmployee);
         
        excelExporter.export(response);    
    }  
 
 
 @GetMapping("/employee/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Employee_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
         
        List<Employee> listEmployee = employeeService.listAll();
         
        PDFExport exporter = new PDFExport(listEmployee);
        exporter.export(response);
         
    }
 
 @GetMapping("/employee/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_hh:mm a");
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Employee_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		
		List<Employee> listEmployee = employeeService.listAll();

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Employee ID", "First Name", "Last Name", "Email    "};
		String[] nameMapping = {"id", "firstName", "lastName", "email"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (Employee employee : listEmployee) {
			csvWriter.write(employee, nameMapping);
		}
		
		csvWriter.close();
		
	}

}
