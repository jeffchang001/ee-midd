package com.sogo.ee.midd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIEmployeeInfoDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;
import com.sogo.ee.midd.repository.APIEmployeeInfoArchivedRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoActionLogRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIEmployeeInfoServiceImplTest {

    @InjectMocks
    private APIEmployeeInfoServiceImpl service;

    @Mock
    private APIEmployeeInfoRepository employeeInfoRepo;

    @Mock
    private APIEmployeeInfoArchivedRepository archivedRepo;

    @Mock
    private APIEmployeeInfoActionLogRepository actionLogRepo;

    // 建立一個 ObjectMapper 並註冊 JavaTimeModule 以便處理日期格式
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 測試 processEmployeeInfo 當 JSON 中有資料時的情形：
     * 1. 解析 JSON 後取得新員工資料
     * 2. 呼叫 truncateTable() 清空原有資料
     * 3. 呼叫 saveAll() 儲存新資料
     */
    @Test
    void testProcessEmployeeInfoWithData() throws Exception {
        // 建立測試用的 APIEmployeeInfo 物件
        APIEmployeeInfo emp = new APIEmployeeInfo();
        emp.setEmployeeNo("E001");
        emp.setEmployedStatus("1");
        emp.setDataCreatedDate(LocalDateTime.of(2025, 1, 1, 0, 0));
        emp.setDataModifiedDate(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<APIEmployeeInfo> empList = new ArrayList<>();
        empList.add(emp);

        APIEmployeeInfoDto dto = new APIEmployeeInfoDto();
        dto.setResult(empList);

        // 將 DTO 轉成 JSON 字串
        String json = objectMapper.writeValueAsString(dto);
        ResponseEntity<String> response = ResponseEntity.ok(json);

        when(employeeInfoRepo.saveAll(anyList())).thenReturn(empList);

        // 執行測試方法
        service.processEmployeeInfo(response);

        // 驗證清空資料表及儲存新資料有被呼叫
        verify(employeeInfoRepo, times(1)).truncateTable();
        verify(employeeInfoRepo, times(1)).saveAll(empList);
    }

    /**
     * 測試 processEmployeeInfo 當 JSON 中無資料時的情形，
     * 此時不應呼叫 truncateTable() 與 saveAll() 方法
     */
    @Test
    void testProcessEmployeeInfoWithoutData() throws Exception {
        // 建立空的結果
        APIEmployeeInfoDto dto = new APIEmployeeInfoDto();
        dto.setResult(new ArrayList<>());

        String json = objectMapper.writeValueAsString(dto);
        ResponseEntity<String> response = ResponseEntity.ok(json);

        service.processEmployeeInfo(response);

        // 驗證沒有呼叫 truncateTable 與 saveAll
        verify(employeeInfoRepo, never()).truncateTable();
        verify(employeeInfoRepo, never()).saveAll(anyList());
    }

    /**
     * 測試 initEmployeeInfo 方法：
     * 1. 清空 employeeInfo 表格，儲存新資料
     * 2. 轉換資料後清空 archived 表格，再儲存 archived 資料
     */
    @Test
    void testInitEmployeeInfo() throws Exception {
        // 建立測試用的 APIEmployeeInfo 物件
        APIEmployeeInfo emp = new APIEmployeeInfo();
        emp.setEmployeeNo("E002");
        emp.setEmployedStatus("1");
        emp.setDataCreatedDate(LocalDateTime.of(2025, 1, 2, 0, 0));
        emp.setDataModifiedDate(LocalDateTime.of(2025, 1, 2, 0, 0));

        List<APIEmployeeInfo> empList = new ArrayList<>();
        empList.add(emp);

        APIEmployeeInfoDto dto = new APIEmployeeInfoDto();
        dto.setResult(empList);

        String json = objectMapper.writeValueAsString(dto);
        ResponseEntity<String> response = ResponseEntity.ok(json);

        when(employeeInfoRepo.saveAll(empList)).thenReturn(empList);
        when(archivedRepo.saveAll(anyList())).thenReturn(new ArrayList<>());

        service.initEmployeeInfo(response);

        verify(employeeInfoRepo, times(1)).truncateTable();
        verify(employeeInfoRepo, times(1)).saveAll(empList);
        verify(archivedRepo, times(1)).truncateTable();
        verify(archivedRepo, times(1)).saveAll(anyList());
    }

    /**
     * 測試 compareAndProcessEmployeeInfo 當員工為新資料時：
     * 1. 資料庫中找不到該員工 (findByEmployeeNo 回傳 null)
     * 2. 呼叫 save() 新增員工資料
     * 3. 產生新增 (C) 的 Action Log
     */
    @Test
    void testCompareAndProcessEmployeeInfo_NewEmployee() throws Exception {
        // 建立新員工資料
        APIEmployeeInfo emp = new APIEmployeeInfo();
        emp.setEmployeeNo("E003");
        emp.setEmployedStatus("1");
        emp.setDataCreatedDate(LocalDateTime.of(2025, 1, 3, 0, 0));
        emp.setDataModifiedDate(LocalDateTime.of(2025, 1, 3, 0, 0));

        List<APIEmployeeInfo> empList = new ArrayList<>();
        empList.add(emp);

        APIEmployeeInfoDto dto = new APIEmployeeInfoDto();
        dto.setResult(empList);

        String json = objectMapper.writeValueAsString(dto);
        ResponseEntity<String> response = ResponseEntity.ok(json);

        // 模擬資料庫中找不到該員工
        when(employeeInfoRepo.findByEmployeeNo("E003")).thenReturn(null);
        when(employeeInfoRepo.save(any(APIEmployeeInfo.class))).thenReturn(emp);

        service.compareAndProcessEmployeeInfo(response);

        verify(employeeInfoRepo, times(1)).findByEmployeeNo("E003");
        verify(employeeInfoRepo, times(1)).save(emp);

        // 檢查是否有產生新增 (C) 的 Action Log
        verify(actionLogRepo, times(1)).saveAll(argThat(new ArgumentMatcher<List<APIEmployeeInfoActionLog>>() {
            @Override
            public boolean matches(List<APIEmployeeInfoActionLog> logs) {
                return logs.stream().anyMatch(log -> "C".equals(log.getAction()));
            }
        }));
    }

    /**
     * 測試 compareAndProcessEmployeeInfo 當資料庫中已有該員工時：
     * 1. 比對新舊資料產生更新 (U) 的 Action Log（例如修改日期不同）
     * 2. 透過 BeanUtils.copyProperties 更新資料，並呼叫 save() 儲存更新後資料
     */
    @Test
    void testCompareAndProcessEmployeeInfo_UpdateEmployee() throws Exception {
        // 建立新資料與資料庫中已存在的員工資料，以觸發更新邏輯
        APIEmployeeInfo empNew = new APIEmployeeInfo();
        empNew.setEmployeeNo("E004");
        empNew.setEmployedStatus("1");
        empNew.setDataCreatedDate(LocalDateTime.of(2025, 1, 4, 0, 0));
        empNew.setDataModifiedDate(LocalDateTime.of(2025, 1, 4, 0, 0));

        APIEmployeeInfo empDb = new APIEmployeeInfo();
        empDb.setEmployeeNo("E004");
        empDb.setEmployedStatus("1");
        empDb.setDataCreatedDate(LocalDateTime.of(2025, 1, 4, 0, 0));
        // 模擬舊資料的修改日期與新資料不同
        empDb.setDataModifiedDate(LocalDateTime.of(2025, 1, 3, 0, 0));

        List<APIEmployeeInfo> empList = new ArrayList<>();
        empList.add(empNew);

        APIEmployeeInfoDto dto = new APIEmployeeInfoDto();
        dto.setResult(empList);

        String json = objectMapper.writeValueAsString(dto);
        ResponseEntity<String> response = ResponseEntity.ok(json);

        when(employeeInfoRepo.findByEmployeeNo("E004")).thenReturn(empDb);
        when(employeeInfoRepo.save(any(APIEmployeeInfo.class))).thenReturn(empNew);
        when(actionLogRepo.saveAll(anyList())).thenReturn(new ArrayList<>());

        service.compareAndProcessEmployeeInfo(response);

        verify(employeeInfoRepo, times(1)).findByEmployeeNo("E004");
        verify(employeeInfoRepo, times(1)).save(empDb);
        verify(actionLogRepo, times(1)).saveAll(argThat(new ArgumentMatcher<List<APIEmployeeInfoActionLog>>() {
            @Override
            public boolean matches(List<APIEmployeeInfoActionLog> logs) {
                return logs != null && !logs.isEmpty();
            }
        }));
    }
}
