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
@Table(name = "api_organization_action_log")
public class APIOrganizationActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OrgCode", nullable = false)
    private String orgCode;

    @Column(name = "OrganizationID")
    private Long organizationID;

    @Column(name = "Action")
    private String action; // C (Create), U (Update), or D (Delete)

    @Column(name = "ActionDate")
    private LocalDateTime actionDate;

    @Column(name = "FieldName", nullable = false)
    private String fieldName; // 變更的欄位名稱，對於新增和刪除操作可以為null

    @Column(name = "OldValue")
    private String oldValue; // 變更前的值，對於新增操作為null

    @Column(name = "NewValue")
    private String newValue; // 變更後的值，對於刪除操作為null

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "IsSync")
    private Boolean isSync = Boolean.FALSE;

    public APIOrganizationActionLog(String orgCode, String action, String fieldName,
            String oldValue, String newValue) {
        this.orgCode = orgCode;
        this.action = action;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.actionDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
    }
}