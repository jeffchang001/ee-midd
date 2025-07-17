package com.sogo.ee.midd.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sogo.ee.midd.model.entity.CoreEhrEmployee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 核心 EHR 員工 DTO
 * 用於轉換 CoreEhrEmployee 實體
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreEhrEmployeeDto {

    /**
     * 員工編號
     */
    @JsonProperty("employeeNo")
    private String employeeNo;

    /**
     * 員工姓名
     */
    @JsonProperty("fullName")
    private String fullName;

    /**
     * 部門代碼
     */
    @JsonProperty("formulaOrgCode")
    private String formulaOrgCode;

    /**
     * 職稱代碼
     */
    @JsonProperty("jobTitleCode")
    private String jobTitleCode;

    /**
     * 職稱名稱
     */
    @JsonProperty("jobTitleName")
    private String jobTitleName;

    /**
     * 職位代碼
     */
    @JsonProperty("positionCode")
    private String positionCode;

    /**
     * 職級代碼
     */
    @JsonProperty("jobGradeCode")
    private String jobGradeCode;

    /**
     * 職務等級代碼
     */
    @JsonProperty("jobLevelCode")
    private String jobLevelCode;

    /**
     * 到職日期
     */
    @JsonProperty("hireDate")
    private String hireDate;

    /**
     * 離職日期
     */
    @JsonProperty("resignationDate")
    private String resignationDate;

    /**
     * 主管員工編號
     */
    @JsonProperty("managerEmployeeNo")
    private String managerEmployeeNo;

    /**
     * 主管姓名
     */
    @JsonProperty("managerFullName")
    private String managerFullName;

    /**
     * 在職狀態
     */
    @JsonProperty("employedStatus")
    private String employedStatus;

    /**
     * 電子郵件地址
     */
    @JsonProperty("emailAddress")
    private String emailAddress;

    /**
     * 將 CoreEhrEmployee 實體轉換為 DTO
     *
     * @param entity CoreEhrEmployee 實體
     * @return CoreEhrEmployeeDto
     */
    public static CoreEhrEmployeeDto fromEntity(CoreEhrEmployee entity) {
        if (entity == null) {
            return null;
        }
        
        return CoreEhrEmployeeDto.builder()
                .employeeNo(entity.getEmployeeNo())
                .fullName(entity.getFullName())
                .formulaOrgCode(entity.getFormulaOrgCode())
                .jobTitleCode(entity.getJobTitleCode())
                .jobTitleName(entity.getJobTitleName())
                .positionCode(entity.getPositionCode())
                .jobGradeCode(entity.getJobGradeCode())
                .jobLevelCode(entity.getJobLevelCode())
                .hireDate(entity.getHireDate())
                .resignationDate(entity.getResignationDate())
                .managerEmployeeNo(entity.getManagerEmployeeNo())
                .managerFullName(entity.getManagerFullName())
                .employedStatus(entity.getEmployedStatus())
                .emailAddress(entity.getEmailAddress())
                .build();
    }

    /**
     * 將 CoreEhrEmployee 實體列表轉換為 DTO 列表
     *
     * @param entities CoreEhrEmployee 實體列表
     * @return CoreEhrEmployeeDto 列表
     */
    public static List<CoreEhrEmployeeDto> fromEntities(List<CoreEhrEmployee> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(CoreEhrEmployeeDto::fromEntity)
                .collect(Collectors.toList());
    }
} 