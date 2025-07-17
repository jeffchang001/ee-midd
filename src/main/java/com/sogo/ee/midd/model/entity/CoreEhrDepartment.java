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
@Table(name = "view_core_ehr_department")
public class CoreEhrDepartment {

    @Id
    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "org_abbr_name")
    private String orgAbbrName;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "manager_employee_no")
    private String managerEmployeeNo;

    @Column(name = "manager_full_name")
    private String managerFullName;

    @Column(name = "parent_org_code")
    private String parentOrgCode;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "is_virtual")
    private Boolean isVirtual;

}
