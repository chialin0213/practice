package com.example.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Dept {
    @Id
    @Column(name = "dept_no")
    private String deptNo ;

    @Column(name = "dept_name")
    private String deptName ;
}
