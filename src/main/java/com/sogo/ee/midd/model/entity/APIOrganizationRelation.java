package com.sogo.ee.midd.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_organization_relation") // 假設您的數據庫表名稱為 api_organization_relation
public class APIOrganizationRelation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("CompanyCode")
    @Column(name = "CompanyCode")
    private String companyCode;

    @JsonProperty("CompanyPartyID")
    @Column(name = "CompanyPartyId")
    private Long companyPartyId;

    @JsonProperty("CreatedDate")
    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

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

    @JsonProperty("OrgCode")
    @Column(name = "OrgCode")
    private String orgCode;

    @JsonProperty("OrgName")
    @Column(name = "OrgName")
    private String orgName;

    @JsonProperty("OrgTreeType")
    @Column(name = "OrgTreeType")
    private String orgTreeType;

    @JsonProperty("OrganizationRelationID")
    @Column(name = "OrganizationRelationId")
    private Long organizationRelationId;

    @JsonProperty("ParentOrgCode")
    @Column(name = "ParentOrgCode")
    private String parentOrgCode;

    @JsonProperty("TenantID")
    @Column(name = "TenantId")
    private String tenantId;

    @Column(name = "Status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrgCode", referencedColumnName = "OrgCode", insertable = false, updatable = false)
    private APIOrganization organization;
}
