package com.sogo.ee.midd.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationDto;
import com.sogo.ee.midd.model.entity.APIOrganization;
import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;
import com.sogo.ee.midd.model.entity.APIOrganizationArchived;
import com.sogo.ee.midd.repository.APIOrganizationActionLogRepository;
import com.sogo.ee.midd.repository.APIOrganizationArchivedRepository;
import com.sogo.ee.midd.repository.APIOrganizationRepository;
import com.sogo.ee.midd.service.APIOrganizationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIOrganizationServiceImpl implements APIOrganizationService {

    @Autowired
    private APIOrganizationRepository organizationRepo;

    @Autowired
    private APIOrganizationArchivedRepository archivedRepo;

    @Autowired
    private APIOrganizationActionLogRepository actionLogRepo;

    @Transactional
    public void processOrganization(ResponseEntity<String> response) throws Exception {
        log.info("Starting processOrganization");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            APIOrganizationDto apiOrganizationResponse = objectMapper.readValue(response.getBody(),
                    APIOrganizationDto.class);

            List<APIOrganization> newOrganizationList = apiOrganizationResponse.getResult();
            log.info("Parsed organization list size: "
                    + (newOrganizationList != null ? newOrganizationList.size() : "null"));

            if (newOrganizationList != null && !newOrganizationList.isEmpty()) {

                // 步驟 1：將原 table 數據存至 Archived
                List<APIOrganizationArchived> archivedList = archiveCurrentData();
                log.info("Archived list size: " + archivedList.size());

                // 步驟 2：清空當前表格並插入新數據
                organizationRepo.truncateTable();
                log.info("organizationRepo Table truncated");

                // 步驟 3：更新需要更新的資料狀態 (C:新增, U:更新)
                updateStatusForNewData(newOrganizationList);
                log.info("Status updated for new data");

                // 步驟 4：保存新數據
                List<APIOrganization> savedList = organizationRepo.saveAll(newOrganizationList);
                log.info("Saved new organization list size: " + savedList.size());

                // 步驟 5：驗證筆數
                List<APIOrganization> verifiedList = organizationRepo.findAll();
                log.info("Verified saved organization list size: " + verifiedList.size());

                // 步驟 6：產生操作日誌供 API 使用
                generateActionLogs();
            } else {
                log.warn("No new organization data to process");
            }
        } catch (Exception e) {
            log.error("Error in processOrganization", e);
            throw e;
        }

        log.info("Completed processOrganization");
    }

    private List<APIOrganizationArchived> archiveCurrentData() {
        List<APIOrganization> currentOrganizationList = organizationRepo.findAll();
        List<APIOrganizationArchived> archivedList = currentOrganizationList.stream()
                .map(this::convertToArchived)
                .collect(Collectors.toList());

        List<APIOrganizationArchived> theAPIOrganizationArchived = archivedRepo.findAll();

        log.debug("currentOrganizationList===" + currentOrganizationList.size());
        log.debug("archivedList===" + archivedList.size());
        log.debug("theAPIOrganizationArchived===" + theAPIOrganizationArchived.size());

        if (!theAPIOrganizationArchived.isEmpty()) {
            archivedRepo.truncateTable();
            log.info("Archived table truncated");
        }

        return archivedRepo.saveAll(archivedList);
    }

    private APIOrganizationArchived convertToArchived(APIOrganization info) {
        APIOrganizationArchived archived = new APIOrganizationArchived();
        Field[] fields = APIOrganization.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Field archivedField = APIOrganizationArchived.class.getDeclaredField(field.getName());
                archivedField.setAccessible(true);
                archivedField.set(archived, field.get(info));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return archived;
    }

    private void updateStatusForNewData(List<APIOrganization> newList) {
        LocalDate today = LocalDate.now();
        for (APIOrganization org : newList) {
            LocalDate createdDate = org.getDataCreatedDate().toLocalDate();
            LocalDate modifiedDate = org.getDataModifiedDate().toLocalDate();

            if (today.equals(createdDate) && today.equals(modifiedDate)) {
                org.setStatus("C");
            } else if ((createdDate.isBefore(today) || createdDate.equals(today)) && modifiedDate.equals(today)) {
                org.setStatus("U");
            }

            organizationRepo.save(org);
        }
    }

    private void generateActionLogs() {
        List<APIOrganization> createAPIOrganizationList = organizationRepo.findByStatus("C");
        List<APIOrganization> updateAPIOrganizationList = organizationRepo.findByStatus("U");

        for (APIOrganization apiOrganization : createAPIOrganizationList) {
            APIOrganizationArchived tmpOrgArchived = archivedRepo.findByOrgCode(apiOrganization.getOrgCode());
            List<APIOrganizationActionLog> tmpActionLogList = actionLogRepo
                    .findByOrgCodeAndActionAndIsSync(apiOrganization.getOrgCode(), "C", false);
            if (tmpOrgArchived == null && tmpActionLogList.isEmpty()) {
                APIOrganizationActionLog tmpActionLog = new APIOrganizationActionLog(apiOrganization.getOrgCode(), "C",
                        "org_code", null, apiOrganization.getOrgCode());
                actionLogRepo.save(tmpActionLog);
            }
        }

        for (APIOrganization apiOrganization : updateAPIOrganizationList) {
            APIOrganizationArchived tmpOrgArchived = archivedRepo.findByOrgCode(apiOrganization.getOrgCode());
            List<APIOrganizationActionLog> tmpActionLogList = actionLogRepo
                    .findByOrgCodeAndActionAndIsSync(apiOrganization.getOrgCode(), "U", false);
            if (tmpOrgArchived != null && tmpActionLogList.isEmpty()) {
                List<APIOrganizationActionLog> newActionLogs = new ArrayList<>();
                compareAndLogChanges(apiOrganization, tmpOrgArchived, newActionLogs);
                actionLogRepo.saveAll(newActionLogs);
            } else {
                apiOrganization.setStatus("E");
                organizationRepo.save(apiOrganization);
            }
        }
    }

    private void compareAndLogChanges(APIOrganization newInfo, APIOrganizationArchived oldInfo,
            List<APIOrganizationActionLog> actionLogs) {
        Field[] fields = APIOrganization.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("id") || field.getName().equals("status")) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object newValue = field.get(newInfo);

                Field oldField = null;
                try {
                    oldField = APIOrganizationArchived.class.getDeclaredField(field.getName());
                    oldField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    log.warn("Field {} not found in APIOrganizationArchived", field.getName());
                    continue;
                }

                Object oldValue = oldField.get(oldInfo);

                if (!Objects.equals(newValue, oldValue)) {
                    actionLogs.add(new APIOrganizationActionLog(
                            newInfo.getOrgCode(),
                            "U",
                            field.getName(),
                            oldValue != null ? oldValue.toString() : null,
                            newValue != null ? newValue.toString() : null));
                }
            } catch (IllegalAccessException e) {
                log.error("Error accessing field: " + field.getName(), e);
            }
        }
    }

    @Transactional
    public void initOrganization(ResponseEntity<String> response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        APIOrganizationDto apiOrganizationResponse = objectMapper.readValue(response.getBody(),
                APIOrganizationDto.class);

        List<APIOrganization> newOrganizationList = apiOrganizationResponse.getResult();
        organizationRepo.truncateTable();
        organizationRepo.saveAll(newOrganizationList);

        List<APIOrganizationArchived> archivedList = newOrganizationList.stream()
                .map(this::convertToArchived)
                .collect(Collectors.toList());

        archivedRepo.truncateTable();
        archivedRepo.saveAll(archivedList);
    }
}