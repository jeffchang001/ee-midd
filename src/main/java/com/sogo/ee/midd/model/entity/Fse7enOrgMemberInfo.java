package com.sogo.ee.midd.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 成員資訊實體類
 * 對應 fse7en_org_memberinfo materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Subselect("SELECT * FROM fse7en_org_memberinfo")
public class Fse7enOrgMemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 員工編號
     */
    @Id
    @Column(name = "employee_no")
    private String employeeNo;

    /**
     * 員工全名
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 分機號碼
     */
    @Column(name = "ext_no")
    private String extNo;

    /**
     * 電子郵件地址
     */
    @Column(name = "email_address")
    private String emailAddress;

    /**
     * 是否離職 (0:在職, 1:離職)
     */
    @Column(name = "isterminated")
    private String isTerminated;

    /**
     * 入職日期
     */
    @Column(name = "hire_date")
    private Date hireDate;

    /**
     * Azure 帳號
     */
    @Column(name = "azureaccount")
    private String azureAccount;
}