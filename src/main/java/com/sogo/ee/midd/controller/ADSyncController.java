package com.sogo.ee.midd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogo.ee.midd.model.dto.ADSyncDto;
import com.sogo.ee.midd.service.ADSyncService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "ADSyncController", description = "提供同步 AD 數據的 API")
public class ADSyncController {

    @Autowired
    private ADSyncService adSyncService;

    @Operation(summary = "獲取 AD 同步數據", description = "此 API 端點返回所有 AD 同步數據的列表")
    @GetMapping("/getADSyncData")
    public ResponseEntity<?> getADSyncData() {
        try {
            List<ADSyncDto> adSyncData = adSyncService.getADSyncData();
            return ResponseEntity.ok(adSyncData);
        } catch (Exception e) {
            // 記錄錯誤
            // logger.error("Error in getADSyncData endpoint", e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}