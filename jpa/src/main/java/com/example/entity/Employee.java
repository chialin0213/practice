package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;
    @Column(name = "emp_name")
    private String empName;
    @Column(name = "emp_team")
    private String empTeam;

    @Column(name = "emp_birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date empBirthDate;
}
