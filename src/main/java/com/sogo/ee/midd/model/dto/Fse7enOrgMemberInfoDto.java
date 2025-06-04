package com.sogo.ee.midd.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 成員資訊 DTO
 * 用於轉換 Fse7enOrgMemberInfo 實體，並將 hireDate 格式化為 yyyy-MM-dd
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fse7enOrgMemberInfoDto {

    /**
     * 員工編號
     */
    @JsonProperty("employeeNo")
    private String employeeNo;

    /**
     * 員工全名
     */
    @JsonProperty("fullName")
    private String fullName;

    /**
     * 分機號碼
     */
    @JsonProperty("extNo")
    private String extNo;

    /**
     * 電子郵件地址
     */
    @JsonProperty("emailAddress")
    private String emailAddress;

    /**
     * 是否離職 (0:在職, 1:離職)
     */
    @JsonProperty("isterminated")
    private String isTerminated;

    /**
     * 入職日期 (格式化為 yyyy-MM-dd)
     */
    @JsonProperty("hireDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;

    /**
     * Azure 帳號
     */
    @JsonProperty("azureAccount")
    private String azureAccount;

    /**
     * job flag
     */
    @JsonProperty("jobFlag")
    private String jobFlag;

    /**
     * 將 Fse7enOrgMemberInfo 實體轉換為 DTO
     *
     * @param entity Fse7enOrgMemberInfo 實體
     * @return Fse7enOrgMemberInfoDto
     */
    public static Fse7enOrgMemberInfoDto fromEntity(Fse7enOrgMemberInfo entity) {
        if (entity == null) {
            return null;
        }
        
        return Fse7enOrgMemberInfoDto.builder()
                .employeeNo(entity.getEmployeeNo())
                .fullName(entity.getFullName())
                .extNo(entity.getExtNo())
                .emailAddress(entity.getEmailAddress())
                .isTerminated(entity.getIsTerminated())
                .hireDate(entity.getHireDate())
                .azureAccount(entity.getAzureAccount())
                .jobFlag(entity.getJobFlag())
                .build();
    }

    /**
     * 將 Fse7enOrgMemberInfo 實體列表轉換為 DTO 列表
     *
     * @param entities Fse7enOrgMemberInfo 實體列表
     * @return Fse7enOrgMemberInfoDto 列表
     */
    public static List<Fse7enOrgMemberInfoDto> fromEntities(List<Fse7enOrgMemberInfo> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(Fse7enOrgMemberInfoDto::fromEntity)
                .collect(Collectors.toList());
    }
} 