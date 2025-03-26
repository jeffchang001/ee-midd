package com.sogo.ee.midd.controller;

import com.sogo.ee.midd.model.dto.Fse7enOrgMemberInfoDto;
import com.sogo.ee.midd.model.dto.MaterializedViewChangeDto;
import com.sogo.ee.midd.model.entity.EmployeeApprovalAmount;
import com.sogo.ee.midd.model.entity.Fse7enOrgDeptGradeInfo;
import com.sogo.ee.midd.model.entity.Fse7enOrgDeptInfo;
import com.sogo.ee.midd.model.entity.Fse7enOrgDeptStruct;
import com.sogo.ee.midd.model.entity.Fse7enOrgJobTitle2Grade;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberInfo;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStruct;
import com.sogo.ee.midd.service.EmployeeApprovalAmountService;
import com.sogo.ee.midd.service.Fse7enOrgDeptGradeInfoService;
import com.sogo.ee.midd.service.Fse7enOrgDeptInfoService;
import com.sogo.ee.midd.service.Fse7enOrgDeptStructService;
import com.sogo.ee.midd.service.Fse7enOrgJobTitle2GradeService;
import com.sogo.ee.midd.service.Fse7enOrgMemberInfoService;
import com.sogo.ee.midd.service.Fse7enOrgMemberStructService;
import com.sogo.ee.midd.service.MaterializedViewChangeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * BPM API 控制器
 * 提供組織結構相關資料的 RESTful API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/bpm")
@Tag(name = "BpmApiController", description = "提供 BPM 相關組織結構資料的 API")
@RequiredArgsConstructor
public class BpmApiController {

    private final Fse7enOrgDeptGradeInfoService deptGradeInfoService;
    private final Fse7enOrgDeptInfoService deptInfoService;
    private final Fse7enOrgDeptStructService deptStructService;
    private final Fse7enOrgJobTitle2GradeService jobTitle2GradeService;
    private final Fse7enOrgMemberInfoService memberInfoService;
    private final Fse7enOrgMemberStructService memberStructService;
    private final MaterializedViewChangeService materializedViewChangeService;
    private final EmployeeApprovalAmountService employeeApprovalAmountService;

    /**
     * 獲取所有部門等級資訊
     *
     * @return 部門等級資訊列表
     */
    @Operation(summary = "獲取所有部門等級資訊", description = "此 API 端點返回所有部門等級資訊的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到部門等級資訊")
    @ApiResponse(responseCode = "204", description = "未找到部門等級資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/dept-grade-info")
    public ResponseEntity<Object> getAllDeptGradeInfo() {
        try {
            List<Fse7enOrgDeptGradeInfo> deptGradeInfoList = deptGradeInfoService.findAll();
            if (deptGradeInfoList.isEmpty()) {
                log.info("未找到部門等級資訊");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到部門等級資訊, 數量: {}", deptGradeInfoList.size());
            return ResponseEntity.ok(deptGradeInfoList);
        } catch (Exception e) {
            log.error("獲取部門等級資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有部門資訊
     *
     * @return 部門資訊列表
     */
    @Operation(summary = "獲取所有部門資訊", description = "此 API 端點返回所有部門資訊的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到部門資訊")
    @ApiResponse(responseCode = "204", description = "未找到部門資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/dept-info")
    public ResponseEntity<Object> getAllDeptInfo() {
        try {
            List<Fse7enOrgDeptInfo> deptInfoList = deptInfoService.findAll();
            if (deptInfoList.isEmpty()) {
                log.info("未找到部門資訊");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到部門資訊, 數量: {}", deptInfoList.size());
            return ResponseEntity.ok(deptInfoList);
        } catch (Exception e) {
            log.error("獲取部門資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有部門結構資訊
     *
     * @return 部門結構資訊列表
     */
    @Operation(summary = "獲取所有部門結構資訊", description = "此 API 端點返回所有部門結構資訊的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到部門結構資訊")
    @ApiResponse(responseCode = "204", description = "未找到部門結構資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/dept-struct")
    public ResponseEntity<Object> getAllDeptStruct() {
        try {
            List<Fse7enOrgDeptStruct> deptStructList = deptStructService.findAll();
            if (deptStructList.isEmpty()) {
                log.info("未找到部門結構資訊");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到部門結構資訊, 數量: {}", deptStructList.size());
            return ResponseEntity.ok(deptStructList);
        } catch (Exception e) {
            log.error("獲取部門結構資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有職稱對應等級資訊
     *
     * @return 職稱對應等級資訊列表
     */
    @Operation(summary = "獲取所有職稱對應等級資訊", description = "此 API 端點返回所有職稱對應等級資訊的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到職稱對應等級資訊")
    @ApiResponse(responseCode = "204", description = "未找到職稱對應等級資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/job-title-grade")
    public ResponseEntity<Object> getAllJobTitleGrade() {
        try {
            List<Fse7enOrgJobTitle2Grade> jobTitleGradeList = jobTitle2GradeService.findAll();
            if (jobTitleGradeList.isEmpty()) {
                log.info("未找到職稱對應等級資訊");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到職稱對應等級資訊, 數量: {}", jobTitleGradeList.size());
            return ResponseEntity.ok(jobTitleGradeList);
        } catch (Exception e) {
            log.error("獲取職稱對應等級資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有成員資訊
     * 使用 DTO 轉換，將 hireDate 格式化為 yyyy-MM-dd
     *
     * @return 成員資訊列表
     */
    @Operation(summary = "獲取所有成員資訊", description = "此 API 端點返回所有成員資訊的列表，hireDate 格式化為 yyyy-MM-dd")
    @ApiResponse(responseCode = "200", description = "成功檢索到成員資訊")
    @ApiResponse(responseCode = "204", description = "未找到成員資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/member-info")
    public ResponseEntity<Object> getAllMemberInfo() {
        try {
            List<Fse7enOrgMemberInfo> memberInfoList = memberInfoService.findAll();
            if (memberInfoList.isEmpty()) {
                log.info("未找到成員資訊");
                return ResponseEntity.noContent().build();
            }
            
            // 使用 DTO 轉換，將 hireDate 格式化為 yyyy-MM-dd
            List<Fse7enOrgMemberInfoDto> memberInfoDtoList = Fse7enOrgMemberInfoDto.fromEntities(memberInfoList);
            
            log.info("成功檢索到成員資訊, 數量: {}", memberInfoDtoList.size());
            return ResponseEntity.ok(memberInfoDtoList);
        } catch (Exception e) {
            log.error("獲取成員資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有成員結構資訊
     *
     * @return 成員結構資訊列表
     */
    @Operation(summary = "獲取所有成員結構資訊", description = "此 API 端點返回所有成員結構資訊的列表")
    @ApiResponse(responseCode = "200", description = "成功檢索到成員結構資訊")
    @ApiResponse(responseCode = "204", description = "未找到成員結構資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/member-struct")
    public ResponseEntity<Object> getAllMemberStruct() {
        try {
            List<Fse7enOrgMemberStruct> memberStructList = memberStructService.findAll();
            if (memberStructList.isEmpty()) {
                log.info("未找到成員結構資訊");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到成員結構資訊, 數量: {}", memberStructList.size());
            return ResponseEntity.ok(memberStructList);
        } catch (Exception e) {
            log.error("獲取成員結構資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 根據日期和視圖名稱查詢實體化視圖變更資訊
     *
     * @param date     查詢日期
     * @param viewName 視圖名稱 (可選)
     * @return 實體化視圖變更資訊列表
     */
    @Operation(summary = "查詢實體化視圖變更資訊", description = "此 API 端點根據日期和視圖名稱查詢實體化視圖變更資訊")
    @ApiResponse(responseCode = "200", description = "成功檢索到實體化視圖變更資訊")
    @ApiResponse(responseCode = "204", description = "未找到實體化視圖變更資訊")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/view-changes")
    public ResponseEntity<Object> getViewChangesByDate(
            @Parameter(description = "查詢日期 (格式: yyyy-MM-dd)", required = true, schema = @Schema(type = "string", format = "date", example = "2025-03-11"))
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            
            @Parameter(description = "視圖名稱 (可選)", schema = @Schema(type = "string", example = "fse7en_org_deptgradeinfo"))
            @RequestParam(value = "view_name", required = false) String viewName) {
        try {
            log.info("查詢實體化視圖變更資訊, 日期: {}, 視圖名稱: {}", date, viewName);
            
            List<MaterializedViewChangeDto> changesList = materializedViewChangeService.getViewChangesByDate(date, viewName);
            
            if (changesList.isEmpty()) {
                log.info("未找到實體化視圖變更資訊");
                return ResponseEntity.noContent().build();
            }
            
            log.info("成功檢索到實體化視圖變更資訊, 數量: {}", changesList.size());
            return ResponseEntity.ok(changesList);
        } catch (Exception e) {
            log.error("查詢實體化視圖變更資訊時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取所有員工審批金額信息
     * 
     * 返回信息包括：
     * - 員工基本信息（工號、姓名、職稱）
     * - 組織信息（簽核單位、實體單位、層級）
     * - 審批金額上限（費用、資本支出、罰款、關係企業款項等）
     *
     * @return 員工審批金額信息列表
     */
    @Operation(
        summary = "獲取所有員工審批金額信息", 
        description = "此 API 端點返回所有員工審批金額信息的列表，包含各種類型審批金額上限"
    )
    @ApiResponse(responseCode = "200", description = "成功檢索到員工審批金額信息")
    @ApiResponse(responseCode = "204", description = "未找到員工審批金額信息")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/approval-amounts")
    public ResponseEntity<Object> getAllApprovalAmounts() {
        try {
            log.info("查詢所有員工審批金額信息");
            List<EmployeeApprovalAmount> amountsList = employeeApprovalAmountService.findAll();
            if (amountsList.isEmpty()) {
                log.info("未找到員工審批金額信息");
                return ResponseEntity.noContent().build();
            }
            log.info("成功檢索到員工審批金額信息, 數量: {}", amountsList.size());
            return ResponseEntity.ok(amountsList);
        } catch (Exception e) {
            log.error("獲取員工審批金額信息時發生錯誤", e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 重新整理所有的 materialized views
     * 
     * @return 重新整理結果
     */
    @Operation(
        summary = "重新整理所有的 materialized views",
        description = "此 API 端點會呼叫 refresh_all_materialized_views() 函數來重新整理所有的 materialized views"
    )
    @ApiResponse(responseCode = "200", description = "成功重新整理所有 materialized views")
    @ApiResponse(responseCode = "500", description = "重新整理過程中發生錯誤")
    @PostMapping("/refresh-views")
    public ResponseEntity<Object> refreshAllMaterializedViews() {
        try {
            log.info("開始重新整理所有 materialized views");
            materializedViewChangeService.refreshAllMaterializedViews();
            log.info("成功重新整理所有 materialized views");
            return ResponseEntity.ok("成功重新整理所有 materialized views");
        } catch (Exception e) {
            log.error("重新整理 materialized views 時發生錯誤", e);
            return ResponseEntity.internalServerError().body("重新整理 materialized views 失敗: " + e.getMessage());
        }
    }
} 