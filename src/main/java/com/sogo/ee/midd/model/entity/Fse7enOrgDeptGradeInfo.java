package com.sogo.ee.midd.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部門等級資訊實體類
 * 對應 fse7en_org_deptgradeinfo materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Subselect("SELECT * FROM fse7en_org_deptgradeinfo")
public class Fse7enOrgDeptGradeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 等級ID
     */
    @Id
    @Column(name = "grade_id")
    private String gradeId;

    /**
     * 等級編號
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 等級編號
     */
    @Column(name = "grade_num")
    private String gradeNum;

}