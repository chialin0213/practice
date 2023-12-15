package com.example.service;

import com.example.entity.Dept;
import com.example.entity.Employee;
import com.example.repository.DeptRepository;
import com.example.repository.EmployeeRepository;
import com.example.vo.EmployeeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {
    private final String EMPLOYEES_CACHE_KEY = "employee:all";
    private final String EMPLOYEE_CACHE_KEY = "employee:";
    private ObjectMapper objectMapper = new ObjectMapper() ;

    @Autowired
    private RedisService redisService ;

    @Autowired
    private EmployeeRepository employeeRepository ;

    @Autowired
    private DeptRepository deptRepository;

    public List<Employee> findAll() throws JsonProcessingException {
        if(redisService.exists(EMPLOYEES_CACHE_KEY)){
            return objectMapper.readValue(redisService.get(EMPLOYEES_CACHE_KEY), new TypeReference<List<Employee>>(){});
        }else{
            List<Employee> employees = employeeRepository.findAll();
            redisService.set(EMPLOYEES_CACHE_KEY, objectMapper.writeValueAsString(employees));
            return employees;
        }
    }

    public Employee findByEmpId(Long empId) throws JsonProcessingException {
        String KEY = EMPLOYEE_CACHE_KEY + empId ;

        if(redisService.exists(KEY)){
            return objectMapper.readValue(redisService.get(KEY), new TypeReference<Employee>(){});
        }else{
            Optional<Employee> employee = employeeRepository.findById(empId);
            Employee emp = employee.orElseGet(() -> new Employee());

            if(employee.isPresent())
                redisService.set(KEY, objectMapper.writeValueAsString(emp), 3600L, TimeUnit.SECONDS);
            else
                redisService.set(KEY, objectMapper.writeValueAsString(emp), 600L, TimeUnit.SECONDS);

            return emp;
        }
    }

    @Transactional
    public Employee saveEmployee(Employee employee){
        Employee emp = employeeRepository.save(employee);

        Optional<Dept> dept = deptRepository.findByDeptName(emp.getEmpTeam());
        if(!dept.isPresent()) throw new RuntimeException("dept not exist");

        return emp ;
    }

    public boolean deleteEmployee(Long empId){
        boolean isOk = false ;
        try {
            employeeRepository.deleteById(empId);
            isOk = true ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOk ;
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.getAllEmployee();
    }

    public List<EmployeeData> getAllEmployeeData() {
        return employeeRepository.getEmployeeData();
    }
}
