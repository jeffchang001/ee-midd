package com.sogo.ee.midd.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_organization_relation_archived")
public class APIOrganizationRelationArchived {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CompanyCode")
    private String companyCode;

    @Column(name = "CompanyPartyId")
    private Long companyPartyId;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "DataCreatedDate")
    private LocalDateTime dataCreatedDate;

    @Column(name = "DataCreatedUser")
    private String dataCreatedUser;

    @Column(name = "DataModifiedDate")
    private LocalDateTime dataModifiedDate;

    @Column(name = "DataModifiedUser")
    private String dataModifiedUser;

    @Column(name = "OrgCode")
    private String orgCode;

    @Column(name = "OrgName")
    private String orgName;

    @Column(name = "OrgTreeType")
    private String orgTreeType;

    @Column(name = "OrganizationRelationId")
    private Long organizationRelationId;

    @Column(name = "ParentOrgCode")
    private String parentOrgCode;

    @Column(name = "TenantId")
    private String tenantId;

    @Column(name = "Status")
    private String status;

    // 用於存儲歸檔時間的欄位
    @Column(name = "ArchivedDate")
    private LocalDateTime archivedDate;

    // 可以添加一個方法來從 APIOrganizationRelation 創建歸檔實體
    public static APIOrganizationRelationArchived fromAPIOrganizationRelation(APIOrganizationRelation relation) {
        APIOrganizationRelationArchived archived = new APIOrganizationRelationArchived();
        archived.setCompanyCode(relation.getCompanyCode());
        archived.setCompanyPartyId(relation.getCompanyPartyId());
        archived.setCreatedDate(relation.getCreatedDate());
        archived.setDataCreatedDate(relation.getDataCreatedDate());
        archived.setDataCreatedUser(relation.getDataCreatedUser());
        archived.setDataModifiedDate(relation.getDataModifiedDate());
        archived.setDataModifiedUser(relation.getDataModifiedUser());
        archived.setOrgCode(relation.getOrgCode());
        archived.setOrgName(relation.getOrgName());
        archived.setOrgTreeType(relation.getOrgTreeType());
        archived.setOrganizationRelationId(relation.getOrganizationRelationId());
        archived.setParentOrgCode(relation.getParentOrgCode());
        archived.setTenantId(relation.getTenantId());
        archived.setStatus(relation.getStatus());
        archived.setArchivedDate(LocalDateTime.now());
        return archived;
    }
}