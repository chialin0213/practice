package com.example.controller;

import com.example.entity.Employee;
import com.example.entity.Titles;
import com.example.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/jpa")
public class JpaController {
    @Autowired
    private EmployeeService employeeService ;

    @RequestMapping(value = "/v1/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> findEmployees() throws JsonProcessingException {
        return new ResponseEntity(employeeService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEmployees() {
        return new ResponseEntity(employeeService.getAllEmployee(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v3/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEmployeesData() {
        return new ResponseEntity(employeeService.getAllEmployeeData(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v4/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEmployeesByPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity(employeeService.getAllEmployeeByPage(page,size), HttpStatus.OK);
    }

    @GetMapping("/v5/employees")
    public ResponseEntity<Object> findEmployeeBetweenEmpId(@RequestParam("empId1") long empId1,
                                @RequestParam("empId2") long empId2) {
        return new ResponseEntity(employeeService.findByIdBetween(empId1,empId2), HttpStatus.OK);
    }

    @GetMapping("/v1/employees/{id}")
    public ResponseEntity<Object> findEmployee(@PathVariable("id") Long empId) throws JsonProcessingException {
        return new ResponseEntity(employeeService.findByEmpId(empId), HttpStatus.OK);
    }

    @PostMapping("/v1/employees")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee employee){
        return new ResponseEntity(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    @PostMapping("/v2/employees")
    public ResponseEntity<Object> addEmployee1(@RequestBody Employee employee) {
        employeeService.insertEmployeeNative(employee);
        return new ResponseEntity("新增成功", HttpStatus.OK);
    }

    @PostMapping("/v3/employees")
    public ResponseEntity<Object> addEmployee2(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return new ResponseEntity("新增成功", HttpStatus.OK);
    }

    @DeleteMapping("/v1/employees/{id}")
    public ResponseEntity<Object> delEmployee(@PathVariable("id") Long empId) {
        return new ResponseEntity(employeeService.deleteEmployee(empId), HttpStatus.OK);
    }

    @DeleteMapping("/v2/employees/{id}")
    public ResponseEntity<Object> delEmployee1(@PathVariable("id") Long empId) {
        employeeService.deleteEmployeeNative(empId);
        return new ResponseEntity("刪除成功", HttpStatus.OK);
    }

    @DeleteMapping("/v3/employees/{id}")
    public ResponseEntity<Object> delEmployee2(@PathVariable("id") Long empId) {
        employeeService.dropEmployee(empId);
        return new ResponseEntity("刪除成功", HttpStatus.OK);
    }

    @PutMapping("/v1/employees/{id}")
    public ResponseEntity<Object> putEmployee(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        emp.setEmpName(employee.getEmpName());
        emp.setEmpBirthDate(employee.getEmpBirthDate());
        emp.setGender(employee.getGender());

        return new ResponseEntity(employeeService.saveEmployee(emp), HttpStatus.OK);
    }

    @PutMapping("/v2/employees/{id}")
    public ResponseEntity<Object> updateEmployee2(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        employeeService.updateEmployeeNative(empId, employee.getEmpName());
        return new ResponseEntity("更新成功", HttpStatus.OK);
    }

    @PutMapping("/v3/employees/{id}")
    public ResponseEntity<Object> updateEmployee3(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        employeeService.modifyEmployee(empId, employee.getEmpName());
        return new ResponseEntity("更新成功", HttpStatus.OK);
    }

    @PatchMapping("/v1/employees/{id}")
    public ResponseEntity<Object> patchEmployee(@PathVariable("id") Long empId, @RequestBody Employee employee) throws JsonProcessingException {
        Employee emp = employeeService.findByEmpId(empId);
        if(emp == null) return new ResponseEntity("employee not found.", HttpStatus.OK);

        if(employee.getEmpName() != null) emp.setEmpName(employee.getEmpName());
        if(employee.getEmpBirthDate() != null) emp.setEmpBirthDate(employee.getEmpBirthDate());
        if(employee.getGender() != null) emp.setGender(employee.getGender());

        return new ResponseEntity(employeeService.saveEmployee(emp), HttpStatus.OK);
    }

    @GetMapping("/v1/titles")
    public ResponseEntity<Object> findTitles(@RequestParam("title") String title) {
        return new ResponseEntity(employeeService.findByTitle(title), HttpStatus.OK);
    }
    @GetMapping("/v2/titles")
    public ResponseEntity<Object> findTitlesNative(@RequestParam("title") String title) {
        return new ResponseEntity(employeeService.findByTitleNative(title), HttpStatus.OK);
    }
    @GetMapping("/v3/titles")
    public ResponseEntity<Object> findTitles(@RequestParam("title") String title,
                                             @RequestParam("fromDate") String fromDate,
                                             @RequestParam("toDate") String toDate,
                                             @RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        Titles t = new Titles();
        t.setTitle(title);
        t.setFromDate(fromDate);
        t.setToDate(toDate);

        return new ResponseEntity(employeeService.findAllTitle(t, PageRequest.of(page,size, Sort.by("fromDate").ascending())), HttpStatus.OK);
    }
}
