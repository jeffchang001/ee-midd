package com.sogo.ee.midd.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogo.ee.midd.model.dto.CoreEhrDepartmentDto;
import com.sogo.ee.midd.model.dto.CoreEhrEmployeeDto;
import com.sogo.ee.midd.model.entity.CoreEhrDepartment;
import com.sogo.ee.midd.model.entity.CoreEhrEmployee;
import com.sogo.ee.midd.service.CoreEhrDepartmentService;
import com.sogo.ee.midd.service.CoreEhrEmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 核心 EHR API 控制器
 * 提供核心 EHR 部門和員工相關資料的 RESTful API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/core")
@Tag(name = "CoreApiController", description = "提供核心 EHR 部門和員工相關資料的 API")
@RequiredArgsConstructor
public class CoreApiController {

    private final CoreEhrDepartmentService coreEhrDepartmentService;
    private final CoreEhrEmployeeService coreEhrEmployeeService;

    // ========== 部門相關 API ==========

    /**
     * 獲取所有部門資訊
     *
     * @return 部門資訊列表
     */
    // @Operation(summary = "獲取所有部門資訊", description = "此 API 端點返回所有部門資訊的列表")
    // @ApiResponse(responseCode = "200", description = "成功檢索到部門資訊")
    // @ApiResponse(responseCode = "204", description = "未找到部門資訊")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/departments")
    // public ResponseEntity<Object> getAllDepartments() {
    //     try {
    //         List<CoreEhrDepartment> departmentList = coreEhrDepartmentService.findAll();
    //         if (departmentList.isEmpty()) {
    //             log.info("未找到部門資訊");
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         // 使用 DTO 轉換
    //         List<CoreEhrDepartmentDto> departmentDtoList = CoreEhrDepartmentDto.fromEntities(departmentList);
            
    //         log.info("成功檢索到部門資訊, 數量: {}", departmentDtoList.size());
    //         return ResponseEntity.ok(departmentDtoList);
    //     } catch (Exception e) {
    //         log.error("獲取部門資訊時發生錯誤", e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據部門代碼獲取部門資訊
     *
     * @param orgCode 部門代碼
     * @return 部門資訊
     */
    @Operation(summary = "根據部門代碼獲取部門資訊", description = "此 API 端點根據部門代碼返回單筆部門資訊")
    @ApiResponse(responseCode = "200", description = "成功檢索到部門資訊")
    @ApiResponse(responseCode = "404", description = "未找到指定部門")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/departments/{orgCode}")
    public ResponseEntity<Object> getDepartmentByOrgCode(
            @Parameter(description = "部門代碼", required = true, schema = @Schema(type = "string", example = "100192012"))
            @PathVariable String orgCode) {
        try {
            Optional<CoreEhrDepartment> department = coreEhrDepartmentService.findByOrgCode(orgCode);
            if (!department.isPresent()) {
                log.info("未找到部門代碼為 {} 的部門", orgCode);
                return ResponseEntity.notFound().build();
            }
            
            CoreEhrDepartmentDto departmentDto = CoreEhrDepartmentDto.fromEntity(department.get());
            log.info("成功檢索到部門資訊: {}", orgCode);
            return ResponseEntity.ok(departmentDto);
        } catch (Exception e) {
            log.error("根據部門代碼獲取部門資訊時發生錯誤: {}", orgCode, e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 根據部門名稱搜尋部門
     *
     * @param orgName 部門名稱 (支援模糊搜尋)
     * @return 部門資訊列表
     */
    // @Operation(summary = "根據部門名稱搜尋部門", description = "此 API 端點根據部門名稱進行模糊搜尋，返回匹配的部門列表")
    // @ApiResponse(responseCode = "200", description = "成功檢索到部門資訊")
    // @ApiResponse(responseCode = "204", description = "未找到匹配的部門")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/departments/search")
    // public ResponseEntity<Object> searchDepartmentsByName(
    //         @Parameter(description = "部門名稱 (支援模糊搜尋)", required = true, schema = @Schema(type = "string", example = "資訊"))
    //         @RequestParam("orgName") String orgName) {
    //     try {
    //         List<CoreEhrDepartment> departmentList = coreEhrDepartmentService.findByOrgNameContaining(orgName);
    //         if (departmentList.isEmpty()) {
    //             log.info("未找到名稱包含 {} 的部門", orgName);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrDepartmentDto> departmentDtoList = CoreEhrDepartmentDto.fromEntities(departmentList);
    //         log.info("成功檢索到部門資訊, 搜尋關鍵字: {}, 數量: {}", orgName, departmentDtoList.size());
    //         return ResponseEntity.ok(departmentDtoList);
    //     } catch (Exception e) {
    //         log.error("根據部門名稱搜尋部門時發生錯誤: {}", orgName, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據上級部門代碼獲取子部門
     *
     * @param parentOrgCode 上級部門代碼
     * @return 子部門列表
     */
    // @Operation(summary = "根據上級部門代碼獲取子部門", description = "此 API 端點根據上級部門代碼返回所有子部門")
    // @ApiResponse(responseCode = "200", description = "成功檢索到子部門資訊")
    // @ApiResponse(responseCode = "204", description = "未找到子部門")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/departments/children")
    // public ResponseEntity<Object> getChildDepartments(
    //         @Parameter(description = "上級部門代碼", required = true, schema = @Schema(type = "string", example = "D001"))
    //         @RequestParam("parentOrgCode") String parentOrgCode) {
    //     try {
    //         List<CoreEhrDepartment> departmentList = coreEhrDepartmentService.findByParentOrgCode(parentOrgCode);
    //         if (departmentList.isEmpty()) {
    //             log.info("未找到上級部門代碼為 {} 的子部門", parentOrgCode);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrDepartmentDto> departmentDtoList = CoreEhrDepartmentDto.fromEntities(departmentList);
    //         log.info("成功檢索到子部門資訊, 上級部門: {}, 數量: {}", parentOrgCode, departmentDtoList.size());
    //         return ResponseEntity.ok(departmentDtoList);
    //     } catch (Exception e) {
    //         log.error("根據上級部門代碼獲取子部門時發生錯誤: {}", parentOrgCode, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    // ========== 員工相關 API ==========

    /**
     * 獲取所有員工資訊
     *
     * @return 員工資訊列表
     */
    // @Operation(summary = "獲取所有員工資訊", description = "此 API 端點返回所有員工資訊的列表")
    // @ApiResponse(responseCode = "200", description = "成功檢索到員工資訊")
    // @ApiResponse(responseCode = "204", description = "未找到員工資訊")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/employees")
    // public ResponseEntity<Object> getAllEmployees() {
    //     try {
    //         List<CoreEhrEmployee> employeeList = coreEhrEmployeeService.findAll();
    //         if (employeeList.isEmpty()) {
    //             log.info("未找到員工資訊");
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         // 使用 DTO 轉換
    //         List<CoreEhrEmployeeDto> employeeDtoList = CoreEhrEmployeeDto.fromEntities(employeeList);
            
    //         log.info("成功檢索到員工資訊, 數量: {}", employeeDtoList.size());
    //         return ResponseEntity.ok(employeeDtoList);
    //     } catch (Exception e) {
    //         log.error("獲取員工資訊時發生錯誤", e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據員工編號獲取員工資訊
     *
     * @param employeeNo 員工編號
     * @return 員工資訊
     */
    @Operation(summary = "根據員工編號獲取員工資訊", description = "此 API 端點根據員工編號返回單筆員工資訊")
    @ApiResponse(responseCode = "200", description = "成功檢索到員工資訊")
    @ApiResponse(responseCode = "404", description = "未找到指定員工")
    @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    @GetMapping("/employees/{employeeNo}")
    public ResponseEntity<Object> getEmployeeByEmployeeNo(
            @Parameter(description = "員工編號", required = true, schema = @Schema(type = "string", example = "019242"))
            @PathVariable String employeeNo) {
        try {
            Optional<CoreEhrEmployee> employee = coreEhrEmployeeService.findByEmployeeNo(employeeNo);
            if (!employee.isPresent()) {
                log.info("未找到員工編號為 {} 的員工", employeeNo);
                return ResponseEntity.notFound().build();
            }
            
            CoreEhrEmployeeDto employeeDto = CoreEhrEmployeeDto.fromEntity(employee.get());
            log.info("成功檢索到員工資訊: {}", employeeNo);
            return ResponseEntity.ok(employeeDto);
        } catch (Exception e) {
            log.error("根據員工編號獲取員工資訊時發生錯誤: {}", employeeNo, e);
            return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 根據員工姓名搜尋員工
     *
     * @param fullName 員工姓名 (支援模糊搜尋)
     * @return 員工資訊列表
     */
    // @Operation(summary = "根據員工姓名搜尋員工", description = "此 API 端點根據員工姓名進行模糊搜尋，返回匹配的員工列表")
    // @ApiResponse(responseCode = "200", description = "成功檢索到員工資訊")
    // @ApiResponse(responseCode = "204", description = "未找到匹配的員工")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/employees/search")
    // public ResponseEntity<Object> searchEmployeesByName(
    //         @Parameter(description = "員工姓名 (支援模糊搜尋)", required = true, schema = @Schema(type = "string", example = "張"))
    //         @RequestParam("fullName") String fullName) {
    //     try {
    //         List<CoreEhrEmployee> employeeList = coreEhrEmployeeService.findByFullNameContaining(fullName);
    //         if (employeeList.isEmpty()) {
    //             log.info("未找到姓名包含 {} 的員工", fullName);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrEmployeeDto> employeeDtoList = CoreEhrEmployeeDto.fromEntities(employeeList);
    //         log.info("成功檢索到員工資訊, 搜尋關鍵字: {}, 數量: {}", fullName, employeeDtoList.size());
    //         return ResponseEntity.ok(employeeDtoList);
    //     } catch (Exception e) {
    //         log.error("根據員工姓名搜尋員工時發生錯誤: {}", fullName, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據部門代碼獲取該部門的員工
     *
     * @param formulaOrgCode 部門代碼
     * @return 員工資訊列表
     */
    // @Operation(summary = "根據部門代碼獲取員工", description = "此 API 端點根據部門代碼返回該部門的所有員工")
    // @ApiResponse(responseCode = "200", description = "成功檢索到員工資訊")
    // @ApiResponse(responseCode = "204", description = "未找到該部門的員工")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/employees/by-department")
    // public ResponseEntity<Object> getEmployeesByDepartment(
    //         @Parameter(description = "部門代碼", required = true, schema = @Schema(type = "string", example = "D001"))
    //         @RequestParam("formulaOrgCode") String formulaOrgCode) {
    //     try {
    //         List<CoreEhrEmployee> employeeList = coreEhrEmployeeService.findByFormulaOrgCode(formulaOrgCode);
    //         if (employeeList.isEmpty()) {
    //             log.info("未找到部門代碼為 {} 的員工", formulaOrgCode);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrEmployeeDto> employeeDtoList = CoreEhrEmployeeDto.fromEntities(employeeList);
    //         log.info("成功檢索到員工資訊, 部門代碼: {}, 數量: {}", formulaOrgCode, employeeDtoList.size());
    //         return ResponseEntity.ok(employeeDtoList);
    //     } catch (Exception e) {
    //         log.error("根據部門代碼獲取員工時發生錯誤: {}", formulaOrgCode, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據在職狀態獲取員工
     *
     * @param employedStatus 在職狀態
     * @return 員工資訊列表
     */
    // @Operation(summary = "根據在職狀態獲取員工", description = "此 API 端點根據在職狀態返回員工列表")
    // @ApiResponse(responseCode = "200", description = "成功檢索到員工資訊")
    // @ApiResponse(responseCode = "204", description = "未找到該狀態的員工")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/employees/by-status")
    // public ResponseEntity<Object> getEmployeesByStatus(
    //         @Parameter(description = "在職狀態", required = true, schema = @Schema(type = "string", example = "ACTIVE"))
    //         @RequestParam("employedStatus") String employedStatus) {
    //     try {
    //         List<CoreEhrEmployee> employeeList = coreEhrEmployeeService.findByEmployedStatus(employedStatus);
    //         if (employeeList.isEmpty()) {
    //             log.info("未找到在職狀態為 {} 的員工", employedStatus);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrEmployeeDto> employeeDtoList = CoreEhrEmployeeDto.fromEntities(employeeList);
    //         log.info("成功檢索到員工資訊, 在職狀態: {}, 數量: {}", employedStatus, employeeDtoList.size());
    //         return ResponseEntity.ok(employeeDtoList);
    //     } catch (Exception e) {
    //         log.error("根據在職狀態獲取員工時發生錯誤: {}", employedStatus, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }

    /**
     * 根據主管員工編號獲取下屬員工
     *
     * @param managerEmployeeNo 主管員工編號
     * @return 下屬員工資訊列表
     */
    // @Operation(summary = "根據主管員工編號獲取下屬員工", description = "此 API 端點根據主管員工編號返回所有下屬員工")
    // @ApiResponse(responseCode = "200", description = "成功檢索到下屬員工資訊")
    // @ApiResponse(responseCode = "204", description = "未找到下屬員工")
    // @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    // @GetMapping("/employees/subordinates")
    // public ResponseEntity<Object> getSubordinateEmployees(
    //         @Parameter(description = "主管員工編號", required = true, schema = @Schema(type = "string", example = "M001"))
    //         @RequestParam("managerEmployeeNo") String managerEmployeeNo) {
    //     try {
    //         List<CoreEhrEmployee> employeeList = coreEhrEmployeeService.findByManagerEmployeeNo(managerEmployeeNo);
    //         if (employeeList.isEmpty()) {
    //             log.info("未找到主管員工編號為 {} 的下屬員工", managerEmployeeNo);
    //             return ResponseEntity.noContent().build();
    //         }
            
    //         List<CoreEhrEmployeeDto> employeeDtoList = CoreEhrEmployeeDto.fromEntities(employeeList);
    //         log.info("成功檢索到下屬員工資訊, 主管編號: {}, 數量: {}", managerEmployeeNo, employeeDtoList.size());
    //         return ResponseEntity.ok(employeeDtoList);
    //     } catch (Exception e) {
    //         log.error("根據主管員工編號獲取下屬員工時發生錯誤: {}", managerEmployeeNo, e);
    //         return ResponseEntity.internalServerError().body("處理請求時發生錯誤: " + e.getMessage());
    //     }
    // }
}
