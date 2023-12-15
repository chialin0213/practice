package com.example.repository;

import com.example.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeptRepository extends JpaRepository<Dept, Long> {
    Optional<Dept> findByDeptName(String name);
}
