package com.example.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private long deptId ;

    @Column(name = "dept_name")
    private String deptName ;
}
