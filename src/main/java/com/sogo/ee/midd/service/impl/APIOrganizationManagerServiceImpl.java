package com.sogo.ee.midd.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationManagerDto;
import com.sogo.ee.midd.model.entity.APIOrganizationManager;
import com.sogo.ee.midd.repository.APIOrganizationManagerRepository;
import com.sogo.ee.midd.service.APIOrganizationManagerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIOrganizationManagerServiceImpl implements APIOrganizationManagerService {

    @Autowired
    private APIOrganizationManagerRepository orgManagerRepo;

    @Override
    @Transactional
    public void initOrganizationManager(ResponseEntity<String> response) throws Exception {
        // 解析 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        APIOrganizationManagerDto apiOrganizationManagerDto = objectMapper.readValue(response.getBody(),
                APIOrganizationManagerDto.class);

        List<APIOrganizationManager> newOrganizationManagerList = apiOrganizationManagerDto.getResult();
        // 儲存 company 資料
        orgManagerRepo.truncateTable();
        orgManagerRepo.saveAll(newOrganizationManagerList);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void processOrganizationManager(ResponseEntity<String> response) throws Exception {
        log.info("Starting processOrganizationManager");

        try {
            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationManagerDto apiOrganizationManagerDto = objectMapper.readValue(response.getBody(),
                    APIOrganizationManagerDto.class);

            List<APIOrganizationManager> newOrganizationManagerList = apiOrganizationManagerDto.getResult();
            log.info("Parsed newOrganizationManagerList list size: "
                    + (newOrganizationManagerList != null ? newOrganizationManagerList.size() : "null"));

            orgManagerRepo.truncateTable();
            orgManagerRepo.saveAll(newOrganizationManagerList);

        } catch (Exception e) {
            log.error("Error in processOrganizationManager", e);
            throw e; // 重新拋出異常，確保事務 roll back
        }

        log.info("Completed processOrganizationManager");
    }

    public boolean existsByEmployeeNoAndOrgCode(String employeeNo, String orgCode) {

        return orgManagerRepo.existsByEmployeeNoAndOrgCode(employeeNo, orgCode);

    }

}
