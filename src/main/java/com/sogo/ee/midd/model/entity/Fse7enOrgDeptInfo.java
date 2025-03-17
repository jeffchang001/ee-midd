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
 * 部門資訊實體類
 * 對應 fse7en_org_deptinfo materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Subselect("SELECT * FROM fse7en_org_deptinfo")
public class Fse7enOrgDeptInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 組織代碼
     */
    @Id
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 組織名稱
     */
    @Column(name = "org_name")
    private String orgName;
} 