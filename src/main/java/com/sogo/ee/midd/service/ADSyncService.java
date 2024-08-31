package com.sogo.ee.midd.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogo.ee.midd.dto.ADSyncDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;
import com.sogo.ee.midd.repository.APIEmployeeInfoActionLogRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;

@Service
public class ADSyncService {

    @Autowired
    private APIEmployeeInfoActionLogRepository actionLogRepository;

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepository;

    @Autowired
    private APIOrganizationRelationService organizationService;

    // public ADSyncDto getADSyncData() {
    //     ADSyncDto adSyncDto = new ADSyncDto();

    //     List<APIEmployeeInfoActionLog> allLogs = actionLogRepository.findAll();

    //     adSyncDto.setCreatedEmployees(processCreatedEmployees(allLogs));
    //     adSyncDto.setDeletedEmployees(processDeletedEmployees(allLogs));
    //     adSyncDto.setUpdatedEmployees(processUpdatedEmployees(allLogs));

    //     return adSyncDto;
    // }

    // private List<EmployeeInfo> processCreatedEmployees(List<APIEmployeeInfoActionLog> allLogs) {
    //     return allLogs.stream()
    //             .filter(log -> "C".equals(log.getAction()))
    //             .map(log -> {
    //                 APIEmployeeInfo employeeInfo = employeeInfoRepository.findByEmployeeNo(log.getEmployeeNo());
    //                 EmployeeInfo info = new EmployeeInfo();
    //                 info.setEmployeeNo(employeeInfo.getEmployeeNo());
    //                 info.setName(employeeInfo.getName());
    //                 info.setOrganizationHierarchy(organizationService.getEmployeeNoByOrgHierarchy(employeeInfo.getEmployeeNo(), "0"));
    //                 return info;
    //             })
    //             .collect(Collectors.toList());
    // }

    // private List<EmployeeInfo> processDeletedEmployees(List<APIEmployeeInfoActionLog> allLogs) {
    //     return allLogs.stream()
    //             .filter(log -> "D".equals(log.getAction()))
    //             .map(log -> {
    //                 APIEmployeeInfo employeeInfo = employeeInfoRepository.findByEmployeeNo(log.getEmployeeNo());
    //                 EmployeeInfo info = new EmployeeInfo();
    //                 info.setEmployeeNo(employeeInfo.getEmployeeNo());
    //                 info.setName(employeeInfo.getName());
    //                 return info;
    //             })
    //             .collect(Collectors.toList());
    // }

    // private Map<String, List<APIEmployeeInfoActionLog>> processUpdatedEmployees(List<APIEmployeeInfoActionLog> allLogs) {
    //     return allLogs.stream()
    //             .filter(log -> "U".equals(log.getAction()))
    //             .collect(Collectors.groupingBy(APIEmployeeInfoActionLog::getEmployeeNo));
    // }
}