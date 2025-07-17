package com.sogo.ee.midd.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sogo.ee.midd.model.entity.CoreEhrDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 核心 EHR 部門 DTO
 * 用於轉換 CoreEhrDepartment 實體
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreEhrDepartmentDto {

    /**
     * 部門代碼
     */
    @JsonProperty("orgCode")
    private String orgCode;

    /**
     * 部門簡稱
     */
    @JsonProperty("orgAbbrName")
    private String orgAbbrName;

    /**
     * 部門名稱
     */
    @JsonProperty("orgName")
    private String orgName;

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
     * 上級部門代碼
     */
    @JsonProperty("parentOrgCode")
    private String parentOrgCode;

    /**
     * 開始日期
     */
    @JsonProperty("startDate")
    private String startDate;

    /**
     * 結束日期
     */
    @JsonProperty("endDate")
    private String endDate;

    /**
     * 是否虛擬部門
     */
    @JsonProperty("isVirtual")
    private Boolean isVirtual;

    /**
     * 將 CoreEhrDepartment 實體轉換為 DTO
     *
     * @param entity CoreEhrDepartment 實體
     * @return CoreEhrDepartmentDto
     */
    public static CoreEhrDepartmentDto fromEntity(CoreEhrDepartment entity) {
        if (entity == null) {
            return null;
        }
        
        return CoreEhrDepartmentDto.builder()
                .orgCode(entity.getOrgCode())
                .orgAbbrName(entity.getOrgAbbrName())
                .orgName(entity.getOrgName())
                .managerEmployeeNo(entity.getManagerEmployeeNo())
                .managerFullName(entity.getManagerFullName())
                .parentOrgCode(entity.getParentOrgCode())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isVirtual(entity.getIsVirtual())
                .build();
    }

    /**
     * 將 CoreEhrDepartment 實體列表轉換為 DTO 列表
     *
     * @param entities CoreEhrDepartment 實體列表
     * @return CoreEhrDepartmentDto 列表
     */
    public static List<CoreEhrDepartmentDto> fromEntities(List<CoreEhrDepartment> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(CoreEhrDepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
} 