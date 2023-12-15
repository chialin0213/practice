package com.example.repository;

import com.example.entity.Employee;
import com.example.vo.EmployeeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    void deleteById(Long id);

    @Query(value="SELECT * FROM EMPLOYEE e WHERE e.EMP_ID BETWEEN ?1 AND ?2 " ,nativeQuery = true)
    List<Employee> findByIdBetween(Long number1, Long number2);

    Employee findByEmpName(String name);

    @Query("Select e From Employee e")
    List<Employee> getAllEmployee();

    //將查詢結果封裝成 EmployeeData 物件
    @Query("SELECT NEW com.example.vo.EmployeeData(e.empId, e.empName) FROM Employee e")
    List<EmployeeData> getEmployeeData();
}
