package com.sogo.ee.midd.model.dto;

import lombok.Data;

@Data
public class OrganizationHierarchyDto {
    private String orgCode;
    private String orgName;
    private String parentOrgCode;
    private int orgLevel;
}
