package com.sogo.ee.midd.model.dto;

import java.util.List;

import com.sogo.ee.midd.model.entity.APIEmployeeInfo;

import lombok.Data;

@Data
public class WholeOrgTreeDto {
    private String orgCode;
    private String orgName;
    private String parentOrgCode;
    private int orgLevel;
    private List<APIEmployeeInfo> employeeInfoList;
}
