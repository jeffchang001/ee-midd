package com.sogo.ee.midd.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationDto;
import com.sogo.ee.midd.model.entity.APIOrganization;
import com.sogo.ee.midd.repository.APIOrganizationRepository;
import com.sogo.ee.midd.service.APIOrganizationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIOrganizationServiceImpl implements APIOrganizationService {

    @Autowired
    private APIOrganizationRepository orgRepo;

    @Override
    @Transactional
    public void initOrganization(ResponseEntity<String> response) throws Exception {
        // 解析 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        APIOrganizationDto apiOrganizationDto = objectMapper.readValue(response.getBody(),
                APIOrganizationDto.class);

        List<APIOrganization> newOrganizationList = apiOrganizationDto.getResult();
        // 儲存 company 資料
        orgRepo.truncateTable();
        orgRepo.saveAll(newOrganizationList);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void processOrganization(ResponseEntity<String> response) throws Exception {
        log.info("Starting processOrganization");

        try {
            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationDto apiOrganizationDto = objectMapper.readValue(response.getBody(),
                    APIOrganizationDto.class);

            List<APIOrganization> newOrganizationList = apiOrganizationDto.getResult();
            log.info("Parsed newOrganizationList list size: "
                    + (newOrganizationList != null ? newOrganizationList.size() : "null"));

            orgRepo.truncateTable();
            orgRepo.saveAll(newOrganizationList);

        } catch (Exception e) {
            log.error("Error in processOrganization", e);
            throw e; // 重新拋出異常，確保事務 roll back
        }

        log.info("Completed processOrganization");
    }

}
