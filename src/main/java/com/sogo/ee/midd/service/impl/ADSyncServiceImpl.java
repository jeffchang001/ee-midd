package com.sogo.ee.midd.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogo.ee.midd.model.dto.ADEmployeeSyncDto;
import com.sogo.ee.midd.model.dto.ADOrganizationSyncDto;
import com.sogo.ee.midd.model.dto.OrganizationHierarchyDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;
import com.sogo.ee.midd.model.entity.APIOrganization;
import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;
import com.sogo.ee.midd.repository.APIEmployeeInfoActionLogRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.repository.APIOrganizationActionLogRepository;
import com.sogo.ee.midd.repository.APIOrganizationRepository;
import com.sogo.ee.midd.service.ADSyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ADSyncServiceImpl implements ADSyncService {

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepository;

    @Autowired
    private APIEmployeeInfoActionLogRepository employeeInfoActionLogRepository;

    @Autowired
    private APIOrganizationRepository organizationRepository;

    @Autowired
    private APIOrganizationActionLogRepository organizationActionLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ADEmployeeSyncDto> getADEmployeeSyncData(String baseDate) {
        List<APIEmployeeInfoActionLog> actionLogs = employeeInfoActionLogRepository
                .findByCreatedDate(baseDate.toString());
        log.info("Found {} action logs", actionLogs.size());

        Map<Long, List<APIEmployeeInfoActionLog>> actionLogMap = actionLogs.stream()
                .collect(Collectors.groupingBy(APIEmployeeInfoActionLog::getId));
        log.info("Found {} actionLogMap logs", actionLogMap.size());

        List<ADEmployeeSyncDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<APIEmployeeInfoActionLog>> entry : actionLogMap.entrySet()) {
            List<APIEmployeeInfoActionLog> logs = entry.getValue();

            ADEmployeeSyncDto adSyncDto = new ADEmployeeSyncDto();
            adSyncDto.setEmployeeNo(logs.get(0).getEmployeeNo());

            APIEmployeeInfo employeeInfo = employeeInfoRepository.findByEmployeeNo(logs.get(0).getEmployeeNo());
            if (employeeInfo != null) {
                adSyncDto.setEmployeeInfo(employeeInfo);
                adSyncDto.setOrgHierarchyDto(getOrganizationHierarchyByEmployeeNo(logs.get(0).getEmployeeNo()));
            }

            String action = logs.get(0).getAction();
            adSyncDto.setAction(action);

            if ("U".equals(action)) {
                Map<String, String> updatedFields = new HashMap<>();
                for (APIEmployeeInfoActionLog log : logs) {
                    updatedFields.put(log.getFieldName(), log.getNewValue());
                }
                adSyncDto.setUpdatedFields(updatedFields);
            }

            result.add(adSyncDto);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<OrganizationHierarchyDto> getOrganizationHierarchyByEmployeeNo(String employeeNo) {
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT * FROM get_employee_no_by_org_hierarchy(:employeeNo, '0')")
                .setParameter("employeeNo", employeeNo)
                .getResultList();

        return results.stream().map(result -> {
            OrganizationHierarchyDto org = new OrganizationHierarchyDto();
            org.setOrgCode((String) result[8]);
            org.setOrgName((String) result[9]);
            org.setParentOrgCode((String) result[10]);
            org.setOrgLevel((Integer) result[11]);
            return org;
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<OrganizationHierarchyDto> getOrganizationHierarchyByOrgCode(String orgCode) {
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT * FROM get_org_hierarchy_by_org_code(:orgCode, '0')")
                .setParameter("orgCode", orgCode)
                .getResultList();

        return results.stream().map(result -> {
            OrganizationHierarchyDto org = new OrganizationHierarchyDto();
            org.setOrgCode((String) result[0]); // org_code
            org.setOrgName((String) result[1]); // org_name
            org.setParentOrgCode((String) result[3]); // parent_org_code
            org.setOrgLevel((Integer) result[4]); // level
            return org;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ADOrganizationSyncDto> getADOrganizationSyncData(String baseDate) throws Exception {
        // 取得指定日期的組織操作日誌
        List<APIOrganizationActionLog> actionLogs = organizationActionLogRepository
                .findByCreatedDate(baseDate.toString());
        log.info("找到 {} 筆組織操作日誌", actionLogs.size());

        // 按照組織代碼分組
        Map<String, List<APIOrganizationActionLog>> actionLogMap = actionLogs.stream()
                .collect(Collectors.groupingBy(APIOrganizationActionLog::getOrgCode));
        log.info("找到 {} 個不同組織的變更", actionLogMap.size());

        List<ADOrganizationSyncDto> result = new ArrayList<>();

        // 處理每個組織的變更資料
        for (Map.Entry<String, List<APIOrganizationActionLog>> entry : actionLogMap.entrySet()) {
            String orgCode = entry.getKey();
            List<APIOrganizationActionLog> logs = entry.getValue();

            ADOrganizationSyncDto adOrgSyncDto = new ADOrganizationSyncDto();
            adOrgSyncDto.setOrgCode(orgCode);

            // 獲取組織資訊
            APIOrganization organization = organizationRepository.findByOrgCode(orgCode);
            if (organization != null) {
                adOrgSyncDto.setOrganization(organization);
                // 獲取組織層次結構
                adOrgSyncDto.setOrgHierarchyDto(getOrganizationHierarchyByOrgCode(orgCode));
            }

            // 判斷操作類型 (C: 新增, U: 更新, D: 刪除)
            String action = logs.get(0).getAction();
            adOrgSyncDto.setAction(action);

            // 如果是更新操作，則收集更新的欄位
            if ("U".equals(action)) {
                Map<String, String> updatedFields = new HashMap<>();
                for (APIOrganizationActionLog log : logs) {
                    updatedFields.put(log.getFieldName(), log.getNewValue());
                }
                adOrgSyncDto.setUpdatedFields(updatedFields);
            }

            result.add(adOrgSyncDto);
        }

        return result;

    }
}
