package com.sogo.ee.midd.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationRelationDto;
import com.sogo.ee.midd.model.dto.WholeOrgTreeDto;
import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.repository.APIOrganizationRelationRepository;
import com.sogo.ee.midd.service.APIOrganizationRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIOrganizationRelationServiceImpl implements APIOrganizationRelationService {

    @Autowired
    private APIOrganizationRelationRepository organizationRelationRepo;

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepo;

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception {
        log.info("Starting processOrganizationRelation");

        try {
            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationRelationDto apiOrganizationRelationResponse = objectMapper.readValue(response.getBody(),
                    APIOrganizationRelationDto.class);

            List<APIOrganizationRelation> apiOrganizationRelationList = apiOrganizationRelationResponse.getResult();
            log.info("Parsed OrganizationRelation list size: "
                    + (apiOrganizationRelationList != null ? apiOrganizationRelationList.size() : "null"));
            
            organizationRelationRepo.truncateTable();

            organizationRelationRepo.saveAll(apiOrganizationRelationList);

        } catch (Exception e) {
            log.error("Error in processOrganizationRelation", e);
            throw e; // 重新拋出異常，確保事務 roll back
        }

        log.info("Completed processOrganizationRelation");
    }

    @Override
    public List<WholeOrgTreeDto> fetchOrganizationRelationByorgTreeType(String orgTreeType) throws Exception {
        List<APIOrganizationRelation> apiOrganizationRelationList = organizationRelationRepo
                .findByOrgTreeType(orgTreeType);

        List<WholeOrgTreeDto> wholeOrgTreeDtoList = new ArrayList<WholeOrgTreeDto>();
        WholeOrgTreeDto wholeOrgTreeDto = null;
        for (APIOrganizationRelation apiOrganizationRelation : apiOrganizationRelationList) {
            wholeOrgTreeDto = new WholeOrgTreeDto();

            wholeOrgTreeDto.setOrgCode(apiOrganizationRelation.getOrgCode());
            wholeOrgTreeDto.setOrgName(apiOrganizationRelation.getOrgName());
            wholeOrgTreeDto.setParentOrgCode(apiOrganizationRelation.getParentOrgCode());
            wholeOrgTreeDto.setOrgLevel(0);
            wholeOrgTreeDto.setEmployeeInfoList(
                    employeeInfoRepo.findEmployeesByOrgCode(apiOrganizationRelation.getOrgCode(), orgTreeType));

            wholeOrgTreeDtoList.add(wholeOrgTreeDto);
        }
        return wholeOrgTreeDtoList;
    }
}
