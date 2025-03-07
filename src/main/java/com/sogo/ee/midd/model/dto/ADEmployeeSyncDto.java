package com.sogo.ee.midd.model.dto;

import java.util.List;
import java.util.Map;

import com.sogo.ee.midd.model.entity.APIEmployeeInfo;

import lombok.Data;

@Data
public class ADEmployeeSyncDto {

    private String employeeNo;
    private String action;
    private APIEmployeeInfo employeeInfo;
    private List<OrganizationHierarchyDto> orgHierarchyDto;
    private Map<String, String> updatedFields;

}