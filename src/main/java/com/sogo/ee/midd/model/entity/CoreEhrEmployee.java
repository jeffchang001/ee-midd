package com.sogo.ee.midd.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "view_core_ehr_employee")
public class CoreEhrEmployee {

    @Id
    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "formula_org_code")
    private String formulaOrgCode;

    @Column(name = "job_title_code")
    private String jobTitleCode;

    @Column(name = "job_title_name")
    private String jobTitleName;

    @Column(name = "position_code")
    private String positionCode;

    @Column(name = "job_grade_code")
    private String jobGradeCode;

    @Column(name = "job_level_code")
    private String jobLevelCode;

    @Column(name = "hire_date")
    private String hireDate;

    @Column(name = "resignation_date")
    private String resignationDate;

    @Column(name = "manager_employee_no")
    private String managerEmployeeNo;

    @Column(name = "manager_full_name")
    private String managerFullName;

    @Column(name = "employed_status")
    private String employedStatus;

    @Column(name = "email_address")
    private String emailAddress;

}
