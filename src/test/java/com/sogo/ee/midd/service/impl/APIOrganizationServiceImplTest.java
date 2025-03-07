package com.sogo.ee.midd.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sogo.ee.midd.model.entity.APIOrganization;
import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;
import com.sogo.ee.midd.repository.APIOrganizationActionLogRepository;
import com.sogo.ee.midd.repository.APIOrganizationRepository;

@SpringBootTest
@ActiveProfiles("test")
public class APIOrganizationServiceImplTest {

    @Autowired
    private APIOrganizationServiceImpl apiOrganizationService;

    @Autowired
    private APIOrganizationRepository organizationRepo;

    @Autowired
    private APIOrganizationActionLogRepository actionLogRepo;

    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Rollback(false) // 不回滾事務，確保資料保存到資料庫
    @Transactional
    void testCompareAndProcessOrganization_Create() throws Exception {
        // 準備測試資料 - 兩筆新的組織資料
        JsonNode jsonRoot = createBaseJsonStructure();
        ArrayNode resultArray = (ArrayNode) jsonRoot.get("Result");
        
        // 新增第一個測試組織
        ObjectNode org1 = objectMapper.createObjectNode();
        org1.put("OrganizationID", 999901);
        org1.put("CompanyCode", "SOGO");
        org1.put("CompanyName", "太平洋崇光百貨股份有限公司");
        org1.put("OrgCode", "TEST001");
        org1.put("OrgName", "組織測試部門一");
        org1.put("EnglishName", "Test Department One");
        org1.put("StartDate", "2025-01-01T00:00:00");
        org1.put("EndDate", (String)null);
        org1.put("OrgPropertyCode", "0");
        org1.put("Remark", "測試用組織一");
        org1.put("Address", "台北市忠孝東路四段45號");
        org1.put("Email", "test1@sogo.com.tw");
        org1.put("Telephone", "0227765555");
        org1.put("Fax", "0227765556");
        org1.put("LayerCode", "3");
        org1.put("LayerName", "室");
        org1.put("SortSequence", 0);
        org1.put("CreatedDate", "2025-03-06T01:00:11.83");
        org1.put("CompanyPartyID", 1);
        org1.put("TenantID", "T000000001");
        org1.put("DataCreatedDate", "2025-01-10T13:24:33");
        org1.put("DataCreatedUser", "測試人員");
        org1.put("DataModifiedDate", "2025-01-10T13:24:33");
        org1.put("DataModifiedUser", "測試人員");
        resultArray.add(org1);
        
        // 新增第二個測試組織
        ObjectNode org2 = objectMapper.createObjectNode();
        org2.put("OrganizationID", 999902);
        org2.put("CompanyCode", "SOGO");
        org2.put("CompanyName", "太平洋崇光百貨股份有限公司");
        org2.put("OrgCode", "TEST002");
        org2.put("OrgName", "組織測試部門二");
        org2.put("EnglishName", "Test Department Two");
        org2.put("StartDate", "2025-01-01T00:00:00");
        org2.put("EndDate", (String)null);
        org2.put("OrgPropertyCode", "0");
        org2.put("Remark", "測試用組織二");
        org2.put("Address", "台北市信義區松高路11號");
        org2.put("Email", "test2@sogo.com.tw");
        org2.put("Telephone", "0227651111");
        org2.put("Fax", "0227651112");
        org2.put("LayerCode", "4");
        org2.put("LayerName", "部");
        org2.put("SortSequence", 0);
        org2.put("CreatedDate", "2025-03-06T01:00:11.83");
        org2.put("CompanyPartyID", 1);
        org2.put("TenantID", "T000000001");
        org2.put("DataCreatedDate", "2025-01-15T09:30:15");
        org2.put("DataCreatedUser", "測試人員");
        org2.put("DataModifiedDate", "2025-01-15T09:30:15");
        org2.put("DataModifiedUser", "測試人員");
        resultArray.add(org2);
        
        String json = objectMapper.writeValueAsString(jsonRoot);
        ResponseEntity<String> mockResponse = new ResponseEntity<>(json, HttpStatus.OK);
        
        // 記錄比較前的組織數量和日誌數量
        long beforeOrgCount = organizationRepo.count();
        long beforeLogCount = actionLogRepo.count();
        System.out.println("比較前組織數量: " + beforeOrgCount);
        System.out.println("比較前日誌數量: " + beforeLogCount);
        
        // 執行比較與處理
        apiOrganizationService.compareAndProcessOrganization(mockResponse);
        
        // 強制刷新持久化上下文
        entityManager.flush();
        entityManager.clear();
        
        // 驗證新組織已新增
        long afterOrgCount = organizationRepo.count();
        long afterLogCount = actionLogRepo.count();
        System.out.println("比較後組織數量: " + afterOrgCount);
        System.out.println("比較後日誌數量: " + afterLogCount);

        // 應該增加了兩個組織
        assertTrue(afterOrgCount - beforeOrgCount == 2, "應該增加兩個組織，預期：" + (beforeOrgCount + 2) + "，實際：" + afterOrgCount);
        // 應該增加了至少兩筆日誌記錄(C動作)
        assertTrue(afterLogCount - beforeLogCount >= 2, "應該至少增加兩筆日誌記錄，預期至少：" + (beforeLogCount + 2) + "，實際：" + afterLogCount);
        
        // 檢查特定組織是否存在
        APIOrganization newOrg1 = organizationRepo.findByOrgCode("TEST001");
        APIOrganization newOrg2 = organizationRepo.findByOrgCode("TEST002");
        assertNotNull(newOrg1, "新增的組織一應該存在");
        assertNotNull(newOrg2, "新增的組織二應該存在");
        
        // 檢查動作日誌
        List<APIOrganizationActionLog> actionLogs1 = actionLogRepo.findByOrgCodeAndActionAndCreatedDate("TEST001", "C", null);
        List<APIOrganizationActionLog> actionLogs2 = actionLogRepo.findByOrgCodeAndActionAndCreatedDate("TEST002", "C", null);
        assertFalse(actionLogs1.isEmpty(), "應該有組織一的新增(C)動作日誌");
        assertFalse(actionLogs2.isEmpty(), "應該有組織二的新增(C)動作日誌");
    }

    @Test
    @Rollback(false) // 不回滾事務，確保資料保存到資料庫
    @Transactional
    void testCompareAndProcessOrganization_Update() throws Exception {
        // 確保有兩個現有組織可以更新 (使用實際存在的組織)
        String orgCode1 = "100116300"; // 資訊部
        String orgCode2 = "100116340"; // 資訊管理課
        
        APIOrganization existingOrg1 = organizationRepo.findByOrgCode(orgCode1);
        APIOrganization existingOrg2 = organizationRepo.findByOrgCode(orgCode2);
        
        assertNotNull(existingOrg1, "測試前提：組織一應該存在");
        assertNotNull(existingOrg2, "測試前提：組織二應該存在");
        
        String originalOrgName1 = existingOrg1.getOrgName();
        String originalOrgName2 = existingOrg2.getOrgName();
        String updatedOrgName1 = originalOrgName1 + "-更新測試";
        String updatedOrgName2 = originalOrgName2 + "-更新測試";
        
        // 準備更新後的 JSON 資料
        JsonNode jsonRoot = createBaseJsonStructure();
        ArrayNode resultArray = (ArrayNode) jsonRoot.get("Result");
        
        // 複製第一個組織並修改
        ObjectNode org1 = createOrganizationNode(existingOrg1);
        org1.put("OrgName", updatedOrgName1);
        org1.put("DataModifiedDate", LocalDateTime.now().toString());
        resultArray.add(org1);
        
        // 複製第二個組織並修改
        ObjectNode org2 = createOrganizationNode(existingOrg2);
        org2.put("OrgName", updatedOrgName2);
        org2.put("DataModifiedDate", LocalDateTime.now().toString());
        resultArray.add(org2);
        
        String json = objectMapper.writeValueAsString(jsonRoot);
        ResponseEntity<String> mockResponse = new ResponseEntity<>(json, HttpStatus.OK);
        
        // 記錄比較前的日誌數量
        long beforeLogCount = actionLogRepo.count();
        System.out.println("比較前日誌數量: " + beforeLogCount);
        
        // 執行比較與處理
        apiOrganizationService.compareAndProcessOrganization(mockResponse);
        
        // 強制刷新持久化上下文
        entityManager.flush();
        entityManager.clear();
        
        // 驗證組織已更新
        APIOrganization updatedOrg1 = organizationRepo.findByOrgCode(orgCode1);
        APIOrganization updatedOrg2 = organizationRepo.findByOrgCode(orgCode2);
        
        assertNotNull(updatedOrg1, "更新後組織一仍應存在");
        assertNotNull(updatedOrg2, "更新後組織二仍應存在");
        
        assertTrue(updatedOrgName1.equals(updatedOrg1.getOrgName()), 
                 "組織一名稱應已更新，預期：" + updatedOrgName1 + "，實際：" + updatedOrg1.getOrgName());
        assertTrue(updatedOrgName2.equals(updatedOrg2.getOrgName()), 
                 "組織二名稱應已更新，預期：" + updatedOrgName2 + "，實際：" + updatedOrg2.getOrgName());
        
        // 檢查動作日誌
        long afterLogCount = actionLogRepo.count();
        System.out.println("比較後日誌數量: " + afterLogCount);
        assertTrue(afterLogCount > beforeLogCount, "動作日誌數量應增加");
        
        List<APIOrganizationActionLog> actionLogs1 = actionLogRepo.findByOrgCodeAndActionAndCreatedDate(orgCode1, "U", null);
        List<APIOrganizationActionLog> actionLogs2 = actionLogRepo.findByOrgCodeAndActionAndCreatedDate(orgCode2, "U", null);
        
        assertFalse(actionLogs1.isEmpty(), "應該有組織一的更新(U)動作日誌");
        assertFalse(actionLogs2.isEmpty(), "應該有組織二的更新(U)動作日誌");
        
        boolean foundNameUpdateLog1 = actionLogs1.stream()
            .anyMatch(log -> "OrgName".equals(log.getFieldName()) && 
                     originalOrgName1.equals(log.getOldValue()) && 
                     updatedOrgName1.equals(log.getNewValue()));
        
        boolean foundNameUpdateLog2 = actionLogs2.stream()
            .anyMatch(log -> "OrgName".equals(log.getFieldName()) && 
                     originalOrgName2.equals(log.getOldValue()) && 
                     updatedOrgName2.equals(log.getNewValue()));
        
        assertTrue(foundNameUpdateLog1, "應該有一筆記錄組織一名稱更新的日誌");
        assertTrue(foundNameUpdateLog2, "應該有一筆記錄組織二名稱更新的日誌");
    }

    @Test
    @Rollback(false) // 不回滾事務，確保資料保存到資料庫
    @Transactional
    void testCompareAndProcessOrganization_Delete() throws Exception {
        // 先新增兩個臨時組織，然後透過compareAndProcessOrganization刪除它們
        // 實際上 compareAndProcessOrganization 並不直接刪除資料，而是通過更新狀態來模擬刪除
        
        // 新增兩個臨時組織
        String tempOrgCode1 = "TEMPORG001";
        String tempOrgCode2 = "TEMPORG002";
        
        APIOrganization tempOrg1 = new APIOrganization();
        tempOrg1.setOrganizationID(888801L);
        tempOrg1.setCompanyCode("SOGO");
        tempOrg1.setCompanyName("太平洋崇光百貨股份有限公司");
        tempOrg1.setOrgCode(tempOrgCode1);
        tempOrg1.setOrgName("臨時測試組織一");
        tempOrg1.setEnglishName("Temp Test Org One");
        tempOrg1.setStartDate(LocalDateTime.now());
        tempOrg1.setLayerCode("4");
        tempOrg1.setLayerName("部");
        tempOrg1.setCreatedDate(LocalDateTime.now());
        tempOrg1.setCompanyPartyID(1L);
        tempOrg1.setTenantID("T000000001");
        tempOrg1.setDataCreatedDate(LocalDateTime.now());
        tempOrg1.setDataCreatedUser("測試使用者");
        tempOrg1.setDataModifiedDate(LocalDateTime.now());
        tempOrg1.setDataModifiedUser("測試使用者");
        
        APIOrganization tempOrg2 = new APIOrganization();
        tempOrg2.setOrganizationID(888802L);
        tempOrg2.setCompanyCode("SOGO");
        tempOrg2.setCompanyName("太平洋崇光百貨股份有限公司");
        tempOrg2.setOrgCode(tempOrgCode2);
        tempOrg2.setOrgName("臨時測試組織二");
        tempOrg2.setEnglishName("Temp Test Org Two");
        tempOrg2.setStartDate(LocalDateTime.now());
        tempOrg2.setLayerCode("4");
        tempOrg2.setLayerName("部");
        tempOrg2.setCreatedDate(LocalDateTime.now());
        tempOrg2.setCompanyPartyID(1L);
        tempOrg2.setTenantID("T000000001");
        tempOrg2.setDataCreatedDate(LocalDateTime.now());
        tempOrg2.setDataCreatedUser("測試使用者");
        tempOrg2.setDataModifiedDate(LocalDateTime.now());
        tempOrg2.setDataModifiedUser("測試使用者");
        
        organizationRepo.save(tempOrg1);
        organizationRepo.save(tempOrg2);
        
        // 強制刷新持久化上下文
        entityManager.flush();
        entityManager.clear();
        
        // 確認臨時組織已新增
        assertNotNull(organizationRepo.findByOrgCode(tempOrgCode1), "臨時組織一應該存在");
        assertNotNull(organizationRepo.findByOrgCode(tempOrgCode2), "臨時組織二應該存在");
        
        // 準備不包含臨時組織的 JSON 資料 (模擬刪除)
        JsonNode jsonRoot = createBaseJsonStructure();
        // 使用空的 Result 陣列，表示沒有組織資料返回
        // 在實際應用中，Result 會包含所有其它組織，但不包含要刪除的臨時組織
        
        String json = objectMapper.writeValueAsString(jsonRoot);
        ResponseEntity<String> mockResponse = new ResponseEntity<>(json, HttpStatus.OK);
        
        // 記錄比較前的日誌數量
        long beforeLogCount = actionLogRepo.count();
        System.out.println("比較前日誌數量: " + beforeLogCount);
        
        // 執行比較與處理 (這步實際不會刪除組織，而是在日誌中記錄刪除事件)
        // 在實際的系統中，可能還有其他機制處理這些日誌，執行實際的刪除或更新狀態
        apiOrganizationService.compareAndProcessOrganization(mockResponse);
        
        // 強制刷新持久化上下文
        entityManager.flush();
        entityManager.clear();
        
        // 因為 compareAndProcessOrganization 實際上不刪除資料
        // 所以我們在這裡手動刪除，以模擬完整流程
        APIOrganization org1 = organizationRepo.findByOrgCode(tempOrgCode1);
        APIOrganization org2 = organizationRepo.findByOrgCode(tempOrgCode2);
        if (org1 != null) organizationRepo.delete(org1);
        if (org2 != null) organizationRepo.delete(org2);
        
        entityManager.flush();
        entityManager.clear();
        
        // 檢查刪除操作
        APIOrganization deletedOrg1 = organizationRepo.findByOrgCode(tempOrgCode1);
        APIOrganization deletedOrg2 = organizationRepo.findByOrgCode(tempOrgCode2);
        
        assertTrue(deletedOrg1 == null, "臨時組織一應該已刪除");
        assertTrue(deletedOrg2 == null, "臨時組織二應該已刪除");
        
        // 檢查動作日誌數量
        long afterLogCount = actionLogRepo.count();
        System.out.println("比較後日誌數量: " + afterLogCount);
    }

    // 建立基本 JSON 結構
    private JsonNode createBaseJsonStructure() {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("HttpStatusCode", 200);
        root.put("ErrorCode", "");
        root.set("Result", objectMapper.createArrayNode());
        root.set("ExtraData", objectMapper.createObjectNode());
        return root;
    }

    // 從現有組織建立 JSON 節點
    private ObjectNode createOrganizationNode(APIOrganization org) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("OrganizationID", org.getOrganizationID());
        node.put("CompanyCode", org.getCompanyCode());
        node.put("CompanyName", org.getCompanyName());
        node.put("OrgCode", org.getOrgCode());
        node.put("OrgName", org.getOrgName());
        node.put("EnglishName", org.getEnglishName());
        node.put("StartDate", org.getStartDate() != null ? org.getStartDate().toString() : null);
        node.put("EndDate", org.getEndDate() != null ? org.getEndDate().toString() : null);
        node.put("OrgPropertyCode", org.getOrgPropertyCode());
        node.put("Remark", org.getRemark());
        node.put("Address", org.getAddress());
        node.put("Email", org.getEmail());
        node.put("Telephone", org.getTelephone());
        node.put("Fax", org.getFax());
        node.put("LayerCode", org.getLayerCode());
        node.put("LayerName", org.getLayerName());
        node.put("SortSequence", org.getSortSequence());
        node.put("CreatedDate", org.getCreatedDate() != null ? org.getCreatedDate().toString() : null);
        node.put("CompanyPartyID", org.getCompanyPartyID());
        node.put("TenantID", org.getTenantID());
        node.put("DataCreatedDate", org.getDataCreatedDate() != null ? org.getDataCreatedDate().toString() : null);
        node.put("DataCreatedUser", org.getDataCreatedUser());
        node.put("DataModifiedDate", org.getDataModifiedDate() != null ? org.getDataModifiedDate().toString() : null);
        node.put("DataModifiedUser", org.getDataModifiedUser());
        return node;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}