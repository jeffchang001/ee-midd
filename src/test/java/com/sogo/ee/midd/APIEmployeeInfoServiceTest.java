package com.sogo.ee.midd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.repository.IAPIEmployeeInfoRepository;
import com.sogo.ee.midd.services.impl.APIEmployeeInfoServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
public class APIEmployeeInfoServiceTest {

    @Autowired
    private APIEmployeeInfoServiceImpl apiEmployeeInfoService;

    @Autowired
    private IAPIEmployeeInfoRepository employeeInfoRepo;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        cleanDatabase();
    }

    @BeforeEach
    private void cleanDatabase() {
        employeeInfoRepo.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Rollback(false)
    @Transactional
    void testProcessEmployeeInfo() throws Exception {
        // 準備測試數據
        String jsonFileName = "mock_employee_response.json";
        InputStream jsonStream = getClass().getClassLoader().getResourceAsStream(jsonFileName);
        assertNotNull(jsonStream, "Test JSON file not found: " + jsonFileName);

        String jsonContent = new String(jsonStream.readAllBytes());
        ResponseEntity<String> mockResponse = new ResponseEntity<>(jsonContent, HttpStatus.OK);

        // 記錄初始數量
        long initialEmployeeCount = employeeInfoRepo.count();
        System.out.println("Initial employee count: " + initialEmployeeCount);

        // 執行被測試的方法
        apiEmployeeInfoService.processEmployeeInfo(mockResponse);

        // 強制刷新持久化上下文
        entityManager.flush();
        entityManager.clear();

        // 記錄最終數量
        long finalEmployeeCount = employeeInfoRepo.count();
        System.out.println("Final employee count: " + finalEmployeeCount);

        // 驗證數據是否增加
        assertTrue(finalEmployeeCount > initialEmployeeCount,
                "New employee data should be added. Initial: " + initialEmployeeCount + ", Final: "
                        + finalEmployeeCount);

        // 驗證保存的數據
        List<APIEmployeeInfo> savedEmployees = employeeInfoRepo.findAll();
        assertFalse(savedEmployees.isEmpty(), "Saved employees list should not be empty");

    }
}
