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

@Service
public class ADSyncServiceImpl implements ADSyncService {

    @Autowired
    private ADSyncRepository adSyncRepository;

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ADSyncDto> getADSyncData() {
        List<APIEmployeeInfoActionLog> actionLogs = adSyncRepository.findByIsSync(false);
        Map<String, List<APIEmployeeInfoActionLog>> actionLogMap = actionLogs.stream()
                .collect(Collectors.groupingBy(APIEmployeeInfoActionLog::getEmployeeNo));

        List<ADSyncDto> result = new ArrayList<>();

        for (Map.Entry<String, List<APIEmployeeInfoActionLog>> entry : actionLogMap.entrySet()) {
            String employeeNo = entry.getKey();
            List<APIEmployeeInfoActionLog> logs = entry.getValue();

            ADSyncDto adSyncDto = new ADSyncDto();
            adSyncDto.setEmployeeNo(employeeNo);

            APIEmployeeInfo employeeInfo = employeeInfoRepository.findByEmployeeNo(employeeNo);
            if (employeeInfo != null) {
                adSyncDto.setEmployeeInfo(employeeInfo);
                adSyncDto.setOrgHierarchyDto(getOrganizationHierarchy(employeeNo));
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
