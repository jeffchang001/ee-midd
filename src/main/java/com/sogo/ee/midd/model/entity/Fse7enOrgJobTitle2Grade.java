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
 * 職稱對應等級資訊實體類
 * 對應 fse7en_org_jobtitle2grade materialized view
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Subselect("SELECT * FROM fse7en_org_jobtitle2grade")
public class Fse7enOrgJobTitle2Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 職稱代碼
     */
    @Id
    @Column(name = "job_title_code")
    private String jobTitleCode;

    /**
     * 職稱名稱
     */
    @Column(name = "job_title_name")
    private String jobTitleName;

    /**
     * 職稱等級
     */
    @Column(name = "job_grade")
    private String jobGrade;
} 