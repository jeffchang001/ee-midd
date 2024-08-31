package com.sogo.ee.midd.dto;

import java.util.List;
import java.util.Map;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;

public class ADSyncDto {
    private List<EmployeeInfo> createdEmployees;
    private List<EmployeeInfo> deletedEmployees;
    private Map<String, List<APIEmployeeInfoActionLog>> updatedEmployees;

    // 省略 getter 和 setter 方法
}

class EmployeeInfo {
    private String employeeNo;
    private String name;
    private List<String> organizationHierarchy;

    // 省略 getter 和 setter 方法
}