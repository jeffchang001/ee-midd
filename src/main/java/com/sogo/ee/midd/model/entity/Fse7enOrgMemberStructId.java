package com.sogo.ee.midd.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Fse7enOrgMemberStruct 的複合主鍵類
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fse7enOrgMemberStructId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 員工編號
     */
    private String employeeNo;

    /**
     * 組織代碼
     */
    private String orgCode;


    private String isMainJob;
} 