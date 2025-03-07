package com.sogo.ee.midd.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIOrganizationDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIOrganization;
import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;
import com.sogo.ee.midd.model.entity.APIOrganizationArchived;
import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.repository.APIOrganizationActionLogRepository;
import com.sogo.ee.midd.repository.APIOrganizationArchivedRepository;
import com.sogo.ee.midd.repository.APIOrganizationRelationRepository;
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

    @Autowired
    private APIEmployeeInfoRepository employeeInfoRepo;

    @Autowired
    private APIOrganizationRelationRepository organizationRelationRepo;

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
                // List<APIOrganizationArchived> archivedList = archiveCurrentData();
                // log.info("Archived list size: " + archivedList.size());

                // 步驟 2：清空當前表格並插入新數據
                organizationRepo.truncateTable();
                log.info("organizationRepo Table truncated");

                // 步驟 3：更新需要更新的資料狀態 (C:新增, U:更新)
                // updateStatusForNewData(newOrganizationList);
                // log.info("Status updated for new data");

                // 步驟 4：保存新數據
                List<APIOrganization> savedList = organizationRepo.saveAll(newOrganizationList);
                log.info("Saved new organization list size: " + savedList.size());

                // 步驟 5：驗證筆數
                // List<APIOrganization> verifiedList = organizationRepo.findAll();
                // log.info("Verified saved organization list size: " + verifiedList.size());

                // 步驟 6：產生操作日誌供 API 使用
                // generateActionLogs();
            } else {
                log.warn("No new organization data to process");
            }
        } catch (Exception e) {
            log.error("Error in processOrganization", e);
            throw e;
        }

        log.info("Completed processOrganization");
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

    @Transactional
    public void compareAndProcessOrganization(ResponseEntity<String> response) throws Exception {
        // Step 1: 取得並解析 json file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        APIOrganizationDto apiOrganizationResponse = objectMapper.readValue(response.getBody(),
                APIOrganizationDto.class);
        List<APIOrganization> organizationList = apiOrganizationResponse.getResult();

        // Step 2: 與資料庫比對每一筆資料, 先確認資料庫是否存在,; 若不存在, a) 新增資料, b) 則在 Acton Log 增加新增資訊;
        // 若存在：a) 更新資料, b) 則在 Acton Log 增加修改資訊
        APIOrganization dbOrg = null;
        List<APIOrganizationActionLog> actionLogList = new ArrayList<>();
        for (APIOrganization apiOrg : organizationList) {
            dbOrg = organizationRepo.findByOrgCode(apiOrg.getOrgCode());
            if (dbOrg == null) {
                organizationRepo.save(apiOrg);
                APIOrganizationActionLog actionLog = new APIOrganizationActionLog(apiOrg.getOrgCode(), "C",
                        "org_code", null, apiOrg.getOrgCode());
                actionLogList.add(actionLog);
            } else {

                Field[] fields = APIOrganization.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals("id") || field.getName().equals("status")) {
                        continue;
                    }
                    field.setAccessible(true);
                    try {
                        Object newValue = field.get(apiOrg);
                        Field oldField = null;
                        try {
                            oldField = APIOrganization.class.getDeclaredField(field.getName());
                            oldField.setAccessible(true);
                        } catch (NoSuchFieldException e) {
                            log.warn("Field {} not found in APIOrganization", field.getName());
                            continue;
                        }

                        Object oldValue = oldField.get(dbOrg);

                        if (!Objects.equals(newValue, oldValue)) {
                            actionLogList.add(new APIOrganizationActionLog(
                                    apiOrg.getOrgCode(),
                                    "U",
                                    field.getName(),
                                    oldValue != null ? oldValue.toString() : null,
                                    newValue != null ? newValue.toString() : null));
                        }
                    } catch (IllegalAccessException e) {
                        log.error("Error accessing field: " + field.getName(), e);
                    }

                }
                // 存在的話，將 apiOrg 的屬性複製給 dbOrg
                BeanUtils.copyProperties(apiOrg, dbOrg, "id", "status");
                organizationRepo.save(dbOrg);
            }
        }
        log.info("actionLogList info: " + actionLogList.toString());
        actionLogRepo.saveAll(actionLogList);
    }

    @Transactional(readOnly = true)
    public List<String> getEmptyOrganizationDNs(String orgTreeType) {
        if (orgTreeType == null || orgTreeType.isEmpty()) {
            orgTreeType = "0"; // 預設組織樹類型
        }

        // 1. 取得所有組織關係，按照指定的組織樹類型
        List<APIOrganizationRelation> allOrgRelations = organizationRelationRepo.findByOrgTreeType(orgTreeType);

        List<String> emptyOrgDNs = new ArrayList<>();

        // 2. 建立一個 Map 來追蹤每個組織的子組織
        Map<String, List<String>> parentToChildrenMap = new HashMap<>();

        // 填充 parentToChildrenMap
        for (APIOrganizationRelation relation : allOrgRelations) {
            String parentOrgCode = relation.getParentOrgCode();
            if (parentOrgCode != null && !parentOrgCode.isEmpty()) {
                if (!parentToChildrenMap.containsKey(parentOrgCode)) {
                    parentToChildrenMap.put(parentOrgCode, new ArrayList<>());
                }
                parentToChildrenMap.get(parentOrgCode).add(relation.getOrgCode());
            }
        }

        for (APIOrganizationRelation relation : allOrgRelations) {
            String orgCode = relation.getOrgCode();

            // 檢查是否有子組織
            boolean hasChildren = parentToChildrenMap.containsKey(orgCode) &&
                    !parentToChildrenMap.get(orgCode).isEmpty();

            // 如果有子組織，則跳過
            if (hasChildren) {
                continue;
            }

            // 3. 檢查這個組織是否有任何員工
            List<APIEmployeeInfo> employees = employeeInfoRepo.findEmployeesByOrgCode(orgCode, orgTreeType);

            // 只計算在職員工(employedStatus = "1")
            long activeEmployeeCount = employees.stream()
                    .filter(e -> "1".equals(e.getEmployedStatus()))
                    .count();

            // 4. 如果沒有員工且沒有子組織，則加入結果列表
            if (activeEmployeeCount == 0) {
                String dn = buildOrganizationDN(relation, allOrgRelations);
                emptyOrgDNs.add(dn);
            }
        }

        return emptyOrgDNs;
    }

    private String buildOrganizationDN(APIOrganizationRelation relation,
            List<APIOrganizationRelation> allRelations) {
        StringBuilder dn = new StringBuilder();

        // 開始建構 DN，從當前組織開始
        dn.append("OU=").append(relation.getOrgName());

        // 遞迴建構父組織的 DN 部分
        String currentParentOrgCode = relation.getParentOrgCode();
        while (currentParentOrgCode != null && !currentParentOrgCode.isEmpty()) {
            // 使用一個新的變數存儲當前的父組織代碼，這樣 Lambda 表達式可以安全地使用它
            final String parentOrgCodeForFilter = currentParentOrgCode;

            // 找到父組織的關係資料
            Optional<APIOrganizationRelation> parentRelation = allRelations.stream()
                    .filter(r -> r.getOrgCode().equals(parentOrgCodeForFilter))
                    .findFirst();

            if (parentRelation.isPresent()) {
                // 加入父組織名稱至 DN
                dn.append(",OU=").append(parentRelation.get().getOrgName());
                // 更新為更上一層的父組織
                currentParentOrgCode = parentRelation.get().getParentOrgCode();
            } else {
                // 找不到父組織，結束遞迴
                break;
            }
        }

        // 加上基礎 DN
        dn.append(",DC=sogo,DC=net");

        return dn.toString();
    }
}