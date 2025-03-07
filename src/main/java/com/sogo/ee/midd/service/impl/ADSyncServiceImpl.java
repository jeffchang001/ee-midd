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

import com.sogo.ee.midd.model.dto.ADSyncDto;
import com.sogo.ee.midd.model.dto.OrganizationHierarchyDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;
import com.sogo.ee.midd.repository.ADSyncRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.service.ADSyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ADSyncServiceImpl implements ADSyncService {

    @Autowired
    private ADSyncRepository adSyncRepository;

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ADSyncDto> getADSyncData(String baseDate) {
        List<APIEmployeeInfoActionLog> actionLogs = adSyncRepository.findByCreatedDate(baseDate.toString());
        log.info("Found {} action logs", actionLogs.size());

        Map<Long, List<APIEmployeeInfoActionLog>> actionLogMap = actionLogs.stream()
                .collect(Collectors.groupingBy(APIEmployeeInfoActionLog::getId));
        log.info("Found {} actionLogMap logs", actionLogMap.size());

        List<ADSyncDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<APIEmployeeInfoActionLog>> entry : actionLogMap.entrySet()) {
            List<APIEmployeeInfoActionLog> logs = entry.getValue();

            ADSyncDto adSyncDto = new ADSyncDto();
            adSyncDto.setEmployeeNo(logs.get(0).getEmployeeNo());

            APIEmployeeInfo employeeInfo = employeeInfoRepository.findByEmployeeNo(logs.get(0).getEmployeeNo());
            if (employeeInfo != null) {
                adSyncDto.setEmployeeInfo(employeeInfo);
                adSyncDto.setOrgHierarchyDto(getOrganizationHierarchy(logs.get(0).getEmployeeNo()));
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
    private List<OrganizationHierarchyDto> getOrganizationHierarchy(String employeeNo) {
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
}
