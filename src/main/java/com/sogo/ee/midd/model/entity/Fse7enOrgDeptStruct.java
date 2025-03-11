package com.sogo.ee.midd.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 部門結構資訊實體類
 * 對應 fse7en_org_deptstruct materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fse7en_org_deptstruct")
public class Fse7enOrgDeptStruct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 組織代碼
     */
    @Id
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 父組織代碼
     */
    @Column(name = "parent_org_code")
    private String parentOrgCode;

    /**
     * 等級ID
     */
    @Column(name = "grade_id")
    private String gradeId;

    /**
     * 等級編號
     */
    @Column(name = "grade_num")
    private String gradeNum;

    /**
     * 頂層組織代碼
     */
    @Column(name = "top_orgtree_code")
    private String topOrgtreeCode;
} 