package com.sogo.ee.midd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogo.ee.midd.model.dto.ADSyncDto;
import com.sogo.ee.midd.service.ADSyncService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "ADSyncController", description = "提供同步 AD 數據的 API")
public class ADSyncController {

    private static final Logger logger = LoggerFactory.getLogger(ADSyncController.class);

    @Autowired
    private ADSyncService adSyncService;

   @Operation(summary = "獲取 AD 同步數據", description = "此 API 端點返回所有 AD 同步數據的列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功檢索到 AD 同步數據"),
        @ApiResponse(responseCode = "404", description = "未找到 AD 同步數據"),
        @ApiResponse(responseCode = "500", description = "服務器內部錯誤")
    })
    @GetMapping("/ad-sync-data")
    public ResponseEntity<?> getADSyncData() {
        try {
            List<ADSyncDto> adSyncData = adSyncService.getADSyncData();
            if (adSyncData.isEmpty()) {
                logger.info("No AD sync data found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No AD sync data available");
            }
            logger.info("Successfully retrieved AD sync data, count: {}", adSyncData.size());
            return ResponseEntity.status(HttpStatus.OK).body(adSyncData);
        } catch (Exception e) {
            logger.error("Error occurred while fetching AD sync data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request: " + e.getMessage());
        }
    }
    

}