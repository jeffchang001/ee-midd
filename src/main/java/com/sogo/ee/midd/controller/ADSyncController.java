package com.sogo.ee.midd.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogo.ee.midd.model.dto.ADEmployeeSyncDto;
import com.sogo.ee.midd.service.ADSyncService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "ADSyncController", description = "提供同步 AD 數據的 API")
@RequiredArgsConstructor
public class ADSyncController {

    private final ADSyncService adSyncService;

    @Operation(summary = "獲取 AD 員工同步數據", description = "此 API 端點返回所有 AD 員工同步數據的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到 AD 員工同步數據")
    @ApiResponse(responseCode = "204", description = "未找到 AD 員工同步數據")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/ad-employee-sync-data")
    public ResponseEntity<Object> getADSyncData(
        @Parameter(description = "基準日期：日期之後的資料", schema = @Schema(type = "string", format = "date", example = "2025-03-07")) 
			@RequestParam(name = "base-date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
        try {
            List<ADEmployeeSyncDto> adEmployeeSyncData = adSyncService.getADEmployeeSyncData(baseDate.toString());
            if (adEmployeeSyncData.isEmpty()) {
                log.info("未找到 AD 員工同步數據");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到 AD 員工同步數據, 數量: {}", adEmployeeSyncData.size());
            return ResponseEntity.ok(adEmployeeSyncData);
        } catch (Exception e) {
            log.error("獲取 AD 員工同步數據時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }
}