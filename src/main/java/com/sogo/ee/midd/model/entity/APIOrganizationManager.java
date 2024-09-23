package com.sogo.ee.midd.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_organization_manager")
public class APIOrganizationManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("OrganizationManagerID")
    @Column(name = "OrganizationManagerID")
    private Long organizationManagerID;

    @JsonProperty("CompanyCode")
    @Column(name = "CompanyCode")
    private String companyCode;

    @JsonProperty("OrgCode")
    @Column(name = "OrgCode")
    private String orgCode;

    @JsonProperty("EmployeeNo")
    @Column(name = "EmployeeNo")
    private String employeeNo;

    @JsonProperty("FullName")
    @Column(name = "FullName")
    private String fullName;

    @JsonProperty("ManagerRoleType")
    @Column(name = "ManagerRoleType")
    private String managerRoleType;

    @JsonProperty("EffectiveDate")
    @Column(name = "EffectiveDate")
    private LocalDateTime effectiveDate;

    @JsonProperty("EndDate")
    @Column(name = "EndDate")
    private LocalDateTime endDate;

    @JsonProperty("IsDeputy")
    @Column(name = "IsDeputy")
    private Boolean isDeputy;

    @JsonProperty("CreatedDate")
    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @JsonProperty("CompanyPartyID")
    @Column(name = "CompanyPartyID")
    private Long companyPartyID;

    @JsonProperty("TenantID")
    @Column(name = "TenantID")
    private String tenantID;

    @JsonProperty("DataCreatedDate")
    @Column(name = "DataCreatedDate")
    private LocalDateTime dataCreatedDate;

    @JsonProperty("DataCreatedUser")
    @Column(name = "DataCreatedUser")
    private String dataCreatedUser;

    @JsonProperty("DataModifiedDate")
    @Column(name = "DataModifiedDate")
    private LocalDateTime dataModifiedDate;

    @JsonProperty("DataModifiedUser")
    @Column(name = "DataModifiedUser")
    private String dataModifiedUser;

    @Column(name = "Status")
    private String status;

}
