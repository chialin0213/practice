package com.example.repository;

import com.example.entity.Dept;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Long> {
    Optional<Dept> findByDeptNo(String deptNo);

    @Query(value = "SELECT d.* FROM dept d WHERE d.dept_no=:deptNo", nativeQuery = true)
    List<Dept> findByDeptNoNquery(@Param("deptNo") String deptNo);
}
