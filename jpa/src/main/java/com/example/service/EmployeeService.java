package com.example.service;

import com.example.entity.Dept;
import com.example.entity.Employee;
import com.example.entity.Titles;
import com.example.repository.DeptRepository;
import com.example.repository.EmployeeRepository;
import com.example.repository.TitlesRepository;
import com.example.vo.EmployeeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {
    private final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final String EMPLOYEES_CACHE_KEY = "employee:all";
    private final String EMPLOYEE_CACHE_KEY = "employee:";
    private ObjectMapper objectMapper = new ObjectMapper() ;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RedisService redisService ;

    @Autowired
    private EmployeeRepository employeeRepository ;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private TitlesRepository titlesRepository ;

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
        List<Dept> dept = deptRepository.findByDeptNoNquery(employee.getDeptNo());

        if(dept == null || dept.isEmpty()) throw new RuntimeException("dept not exist");

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

    public List<Employee> getAllEmployeeByPage(int page, int size){
        Page<Employee> pageResult =employeeRepository.findAll(PageRequest.of(page,  // 查詢的頁數，從0起算
                size, // 查詢的每頁筆數
                Sort.by("empId").ascending()));

        return pageResult.getContent();
    }

    public List<Employee> findByIdBetween(long empId, long empId2){
        return employeeRepository.findByIdBetween(empId, empId2);
    }

    public List<Titles> findByTitle(String title){
        return titlesRepository.findByTitle(title);
    }

    public List<Titles> findByTitleNative(String title){
        return titlesRepository.findByTitleNative(title);
    }

    public List<Titles> findAllTitle(Titles titleDto, Pageable pageable){
        return titlesRepository.findAllByPage(titleDto, pageable).getContent() ;
    }

    public void updateEmployeeNative(long empId, String empName){
        employeeRepository.updateEmployee(empId, empName);
    }

    public void deleteEmployeeNative(long empId){
        employeeRepository.deleteEmployee(empId);
    }

    public void insertEmployeeNative(Employee employee){
        employeeRepository.insertEmployee(employee.getEmpName(),sdFormat.format(employee.getEmpBirthDate()),employee.getGender());
    }

    public void modifyEmployee(long empId, String empName){
        employeeRepository.modifyEmployee(empId, empName);
    }

    public void dropEmployee(long empId){
        employeeRepository.dropEmployee(empId);
    }

    @Transactional
    public void addEmployee(Employee employee){
        entityManager.createNativeQuery("INSERT INTO Employee (emp_name, emp_birth_date, gender) VALUES (?,?,?)")
                .setParameter(1, employee.getEmpName())
                .setParameter(2, employee.getEmpBirthDate())
                .setParameter(3, employee.getGender())
                .executeUpdate();
    }
}
