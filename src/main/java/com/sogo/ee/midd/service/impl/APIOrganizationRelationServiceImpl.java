package com.sogo.ee.midd.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationRelationDto;
import com.sogo.ee.midd.model.dto.OrganizationHierarchyDto;
import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
import com.sogo.ee.midd.repository.APIOrganizationRelationRepository;
import com.sogo.ee.midd.service.APIOrganizationRelationService;

@Service
public class APIOrganizationRelationServiceImpl implements APIOrganizationRelationService {

    private static final Logger logger = LoggerFactory.getLogger(APIOrganizationRelationServiceImpl.class);

    @Autowired
    private APIOrganizationRelationRepository organizationRelationRepo;

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception {
        logger.info("Starting processOrganizationRelation");

        try {
            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationRelationDto apiOrganizationRelationResponse = objectMapper.readValue(response.getBody(),
                    APIOrganizationRelationDto.class);

            List<APIOrganizationRelation> apiOrganizationRelationList = apiOrganizationRelationResponse.getResult();
            logger.info("Parsed OrganizationRelation list size: "
                    + (apiOrganizationRelationList != null ? apiOrganizationRelationList.size() : "null"));

            organizationRelationRepo.saveAll(apiOrganizationRelationList);

        } catch (Exception e) {
            logger.error("Error in processOrganizationRelation", e);
            throw e; // 重新拋出異常，確保事務 roll back
        }

        logger.info("Completed processOrganizationRelation");
    }

    @Override
    public List<OrganizationHierarchyDto> fetchOrganizationRelationByorgTreeType(String orgTreeType) throws Exception {
        List<APIOrganizationRelation> apiOrganizationRelationList = organizationRelationRepo
                .findByOrgTreeType(orgTreeType);

        // Convert apiOrganizationRelationList to OrganizationHierarchyDtoList
        List<OrganizationHierarchyDto> organizationHierarchyDtoList = apiOrganizationRelationList.stream()
                .map(apiOrganizationRelation -> {
                    OrganizationHierarchyDto dto = new OrganizationHierarchyDto();
                    dto.setOrgCode(apiOrganizationRelation.getOrgCode());
                    dto.setOrgName(apiOrganizationRelation.getOrgName());
                    dto.setParentOrgCode(apiOrganizationRelation.getParentOrgCode());
                    dto.setOrgLevel(0); // 設定 orgLevel 為 0
                    return dto;
                }).toList();

        return organizationHierarchyDtoList;
    }
}
