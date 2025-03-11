package com.sogo.ee.midd.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 成員結構資訊實體類
 * 對應 fse7en_org_memberstruct materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fse7en_org_memberstruct")
@IdClass(Fse7enOrgMemberStructId.class)
public class Fse7enOrgMemberStruct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 員工編號
     */
    @Id
    @Column(name = "employee_no")
    private String employeeNo;

    /**
     * 組織代碼
     */
    @Id
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 員工全名
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 組織名稱
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 職稱代碼
     */
    @Column(name = "job_title_code")
    private String jobTitleCode;

    /**
     * 職稱等級
     */
    @Column(name = "job_grade")
    private String jobGrade;

    /**
     * 是否為主要職位 (0:否, 1:是)
     */
    @Column(name = "is_main_job")
    private String isMainJob;

    /**
     * 是否有審批權限 (0:否, 1:是)
     */
    @Column(name = "approve_right")
    private String approveRight;

    /**
     * 是否啟用 (0:否, 1:是)
     */
    @Column(name = "enable")
    private String enable;

    /**
     * 指導者
     */
    @Column(name = "instructor")
    private String instructor;
} 