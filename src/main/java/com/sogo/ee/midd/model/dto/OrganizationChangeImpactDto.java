package com.sogo.ee.midd.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationChangeImpactDto {
    private String orgCode;
    private String oldParentOrgCode;
    private String newParentOrgCode;
}