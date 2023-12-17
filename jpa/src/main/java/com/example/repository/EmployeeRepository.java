package com.example.repository;

import com.example.entity.Employee;
import com.example.vo.EmployeeData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    void deleteById(Long id);

    Employee findByEmpName(String name);

    @Query(value="SELECT * FROM EMPLOYEE e WHERE e.EMP_ID BETWEEN ?1 AND ?2 " ,nativeQuery = true)
    List<Employee> findByIdBetween(Long number1, Long number2);

    @Query(value = "update employee set emp_name=:empName where emp_id=:empId", nativeQuery = true)
    @Transactional
    @Modifying
    void updateEmployee(@Param("empId") long empId, @Param("empName") String empName);

    @Query(value = "delete from employee where emp_id=:empId", nativeQuery = true)
    @Transactional
    @Modifying
    void deleteEmployee(@Param("empId") long empId);

    @Modifying
    @Transactional
    @Query("delete from Employee e where e.empId=:empId")
    void dropEmployee(@Param("empId") long empId);

    @Query(value = "insert into employee (emp_name, emp_birth_date, gender) VALUES (?1, ?2, ?3)", nativeQuery = true)
    @Modifying
    @Transactional
    void insertEmployee(String empName, String empBirthDate, String gender);

    @Query("Select e From Employee e")
    List<Employee> getAllEmployee();

    @Modifying
    @Transactional
    @Query("update Employee e set e.empName=:empName where e.empId=:empId")
    void modifyEmployee(@Param("empId") long empId, @Param("empName") String empName);

    //將查詢結果封裝成 EmployeeData 物件
    @Query("SELECT NEW com.example.vo.EmployeeData(e.empId, e.empName) FROM Employee e")
    List<EmployeeData> getEmployeeData();

}
