package com.example.controller;

import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jpa")
public class JpaController {
    @Autowired
    private EmployeeService employeeService ;

    @RequestMapping(value = "/v1/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> findEmployees() throws JsonProcessingException {
        return new ResponseEntity(employeeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/v1/employees/{id}")
    public ResponseEntity<Object> findEmployee(@PathVariable("id") Long empId) throws JsonProcessingException {
        return new ResponseEntity(employeeService.findByEmpId(empId), HttpStatus.OK);
    }

    @PostMapping("/v1/employees")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee employee) throws JsonProcessingException {
        return new ResponseEntity(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    @DeleteMapping("/v1/employees/{id}")
    public ResponseEntity<Object> delEmployee(@PathVariable("id") Long empId) throws JsonProcessingException {
        return new ResponseEntity(employeeService.deleteEmployee(empId), HttpStatus.OK);
    }

    @PutMapping("/v1/employees/{id}")
    public ResponseEntity<Object> putEmployee(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        emp.setEmpName(employee.getEmpName());
        emp.setEmpTeam(employee.getEmpTeam());
        emp.setEmpBirthDate(employee.getEmpBirthDate());

        return new ResponseEntity(employeeService.saveEmployee(emp), HttpStatus.OK);
    }

    @PatchMapping("/v1/employees/{id}")
    public ResponseEntity<Object> patchEmployee(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        if(employee.getEmpName() != null) emp.setEmpName(employee.getEmpName());
        if(employee.getEmpTeam() != null) emp.setEmpTeam(employee.getEmpTeam());
        if(employee.getEmpBirthDate() != null) emp.setEmpBirthDate(employee.getEmpBirthDate());

        return new ResponseEntity(employeeService.saveEmployee(emp), HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEmployees() {
        return new ResponseEntity(employeeService.getAllEmployee(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v3/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEmployeesData() {
        return new ResponseEntity(employeeService.getAllEmployeeData(), HttpStatus.OK);
    }
}
