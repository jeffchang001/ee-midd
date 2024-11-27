package com.sogo.ee.midd.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationRelationDto;
import com.sogo.ee.midd.model.dto.OrganizationChangeImpactDto;
import com.sogo.ee.midd.model.dto.WholeOrgTreeDto;
import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
import com.sogo.ee.midd.model.entity.APIOrganizationRelationActionLog;
import com.sogo.ee.midd.model.entity.APIOrganizationRelationArchived;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.repository.APIOrganizationRelationActionLogRepository;
import com.sogo.ee.midd.repository.APIOrganizationRelationArchivedRepository;
import com.sogo.ee.midd.repository.APIOrganizationRelationRepository;
import com.sogo.ee.midd.service.APIOrganizationRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIOrganizationRelationServiceImpl implements APIOrganizationRelationService {

    @Autowired
    private APIOrganizationRelationRepository organizationRelationRepo;

    @Autowired
    private APIOrganizationRelationArchivedRepository organizationRelationArchivedRepo;

    @Autowired
    private APIOrganizationRelationActionLogRepository organizationRelationActionLogRepo;

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception {
        log.info("Starting processOrganizationRelation");

        try {
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationRelationDto apiOrganizationRelationResponse = objectMapper.readValue(response.getBody(),
                    APIOrganizationRelationDto.class);

            List<APIOrganizationRelation> newOrganizationRelationList = apiOrganizationRelationResponse.getResult();
            log.info("Parsed OrganizationRelation list size: " + newOrganizationRelationList.size());

             // 步驟 1：將原 table 數據存至 Archived
            List<APIOrganizationRelationArchived> archivedList = archiveCurrentData();

            
            List<OrganizationChangeImpactDto> impacts = compareAndGenerateActionLogs(newOrganizationRelationList,
                    archivedList);
            updateOrganizationRelations(newOrganizationRelationList);

            // 照理說, employee 應該已經在先同步完成
            // updateEmployeeOrganizations(impacts);

        } catch (Exception e) {
            log.error("Error in processOrganizationRelation", e);
            throw e;
        }

        log.info("Completed processOrganizationRelation");
    }

    private List<APIOrganizationRelationArchived> archiveCurrentData() {
        List<APIOrganizationRelation> currentRelations = organizationRelationRepo.findAll();
        List<APIOrganizationRelationArchived> archivedList = currentRelations.stream()
                .map(APIOrganizationRelationArchived::fromAPIOrganizationRelation)
                .collect(Collectors.toList());

        organizationRelationArchivedRepo.truncateTable();
        return organizationRelationArchivedRepo.saveAll(archivedList);
    }

    private List<OrganizationChangeImpactDto> compareAndGenerateActionLogs(
            List<APIOrganizationRelation> newList,
            List<APIOrganizationRelationArchived> oldList) {
        Map<String, APIOrganizationRelationArchived> oldMap = oldList.stream()
                .collect(Collectors.toMap(APIOrganizationRelationArchived::getOrgCode, o -> o));

        List<OrganizationChangeImpactDto> impacts = new ArrayList<>();

        for (APIOrganizationRelation newRelation : newList) {
            APIOrganizationRelationArchived oldRelation = oldMap.remove(newRelation.getOrgCode());
            if (oldRelation == null) {
                generateActionLog(newRelation, "C", "OrgCode", null, newRelation.getOrgCode());
                impacts.add(new OrganizationChangeImpactDto(newRelation.getOrgCode(), null,
                        newRelation.getParentOrgCode()));
            } else {
                impacts.addAll(compareAndLogChanges(newRelation, oldRelation));
            }
        }

        for (APIOrganizationRelationArchived deletedRelation : oldMap.values()) {
            generateActionLog(deletedRelation, "D", "OrgCode", deletedRelation.getOrgCode(), null);
            impacts.add(new OrganizationChangeImpactDto(deletedRelation.getOrgCode(),
                    deletedRelation.getParentOrgCode(), null));
        }

        return impacts;
    }

    private List<OrganizationChangeImpactDto> compareAndLogChanges(APIOrganizationRelation newRelation,
            APIOrganizationRelationArchived oldRelation) {
        List<OrganizationChangeImpactDto> impacts = new ArrayList<>();

        if (!Objects.equals(newRelation.getParentOrgCode(), oldRelation.getParentOrgCode())) {
            generateActionLog(newRelation, "U", "ParentOrgCode", oldRelation.getParentOrgCode(),
                    newRelation.getParentOrgCode());
            impacts.add(new OrganizationChangeImpactDto(newRelation.getOrgCode(), oldRelation.getParentOrgCode(),
                    newRelation.getParentOrgCode()));
        }

        // 比較其他可能發生變化的欄位
        if (!Objects.equals(newRelation.getOrgName(), oldRelation.getOrgName())) {
            generateActionLog(newRelation, "U", "OrgName", oldRelation.getOrgName(), newRelation.getOrgName());
        }

        if (!Objects.equals(newRelation.getOrgTreeType(), oldRelation.getOrgTreeType())) {
            generateActionLog(newRelation, "U", "OrgTreeType", oldRelation.getOrgTreeType(),
                    newRelation.getOrgTreeType());
        }

        if (!Objects.equals(newRelation.getCompanyCode(), oldRelation.getCompanyCode())) {
            generateActionLog(newRelation, "U", "CompanyCode", oldRelation.getCompanyCode(),
                    newRelation.getCompanyCode());
        }

        if (!Objects.equals(newRelation.getCompanyPartyId(), oldRelation.getCompanyPartyId())) {
            generateActionLog(newRelation, "U", "CompanyPartyId", String.valueOf(oldRelation.getCompanyPartyId()),
                    String.valueOf(newRelation.getCompanyPartyId()));
        }

        if (!Objects.equals(newRelation.getTenantId(), oldRelation.getTenantId())) {
            generateActionLog(newRelation, "U", "TenantId", oldRelation.getTenantId(), newRelation.getTenantId());
        }

        return impacts;
    }

    private void generateActionLog(Object relation, String action, String fieldName, String oldValue, String newValue) {
        APIOrganizationRelationActionLog log = new APIOrganizationRelationActionLog(
                relation instanceof APIOrganizationRelation ? ((APIOrganizationRelation) relation).getOrgCode()
                        : ((APIOrganizationRelationArchived) relation).getOrgCode(),
                action,
                fieldName,
                oldValue,
                newValue);
        organizationRelationActionLogRepo.save(log);
    }

    private void updateOrganizationRelations(List<APIOrganizationRelation> newRelations) {
        organizationRelationRepo.truncateTable();;
        organizationRelationRepo.saveAll(newRelations);
    }


    @Override
    public List<WholeOrgTreeDto> fetchOrganizationRelationByorgTreeType(String treeType) throws Exception {
        List<APIOrganizationRelation> apiOrganizationRelationList = organizationRelationRepo
                .findByOrgTreeType(treeType);
        log.info("apiOrganizationRelationList size: " + apiOrganizationRelationList.size());
        List<WholeOrgTreeDto> wholeOrgTreeDtoList = new ArrayList<>();
        for (APIOrganizationRelation apiOrganizationRelation : apiOrganizationRelationList) {
            WholeOrgTreeDto wholeOrgTreeDto = new WholeOrgTreeDto();
            wholeOrgTreeDto.setOrgCode(apiOrganizationRelation.getOrgCode());
            wholeOrgTreeDto.setOrgName(apiOrganizationRelation.getOrgName());
            wholeOrgTreeDto.setParentOrgCode(apiOrganizationRelation.getParentOrgCode());
            wholeOrgTreeDto.setOrgLevel(0);
            wholeOrgTreeDto.setEmployeeInfoList(
                    employeeInfoRepo.findEmployeesByOrgCode(apiOrganizationRelation.getOrgCode(), treeType));
            wholeOrgTreeDtoList.add(wholeOrgTreeDto);
        }
        return wholeOrgTreeDtoList;
    }
}