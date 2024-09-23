package com.sogo.ee.midd.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APICompanyDto;
import com.sogo.ee.midd.model.entity.APICompany;
import com.sogo.ee.midd.repository.APICompanyRepository;
import com.sogo.ee.midd.service.APICompanyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APICompanyServiceImpl implements APICompanyService {

    @Autowired
    private APICompanyRepository companyRepo;

    @Override
    @Transactional
    public void initCompany(ResponseEntity<String> response) throws Exception {
        // 解析 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        APICompanyDto apiCompanyDto = objectMapper.readValue(response.getBody(),
                APICompanyDto.class);

        List<APICompany> newCompanyList = apiCompanyDto.getResult();
        // 儲存 company 資料
        companyRepo.truncateTable();
        companyRepo.saveAll(newCompanyList);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void processCompany(ResponseEntity<String> response) throws Exception {
        log.info("Starting processCompany");

        try {
            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APICompanyDto apiCompanyDto = objectMapper.readValue(response.getBody(),
                APICompanyDto.class);

                List<APICompany> newCompanyList = apiCompanyDto.getResult();
            log.info("Parsed newCompanyList list size: "
                    + (newCompanyList != null ? newCompanyList.size() : "null"));

            companyRepo.truncateTable();
            companyRepo.saveAll(newCompanyList);

        } catch (Exception e) {
            log.error("Error in processOrganizationRelation", e);
            throw e; // 重新拋出異常，確保事務 roll back
        }

        log.info("Completed processCompany");
    }

}
