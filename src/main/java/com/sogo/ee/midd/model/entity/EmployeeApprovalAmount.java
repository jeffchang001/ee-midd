package com.sogo.ee.midd.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 員工審批金額實體類
 * 用於映射 view_employee_approval_amount 視圖
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "view_employee_approval_amount")
public class EmployeeApprovalAmount {

    @Id
    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "job_title_code")
    private String jobTitleCode;

    @Column(name = "job_title_name")
    private String jobTitleName;

    @Column(name = "layer_code")
    private String layerCode;

    @Column(name = "layer_name")
    private String layerName;

    @Column(name = "layer_description")
    private String layerDescription;

    @Column(name = "form_org_code")
    private String formOrgCode;

    @Column(name = "form_org_name")
    private String formOrgName;

    @Column(name = "formula_org_code")
    private String formulaOrgCode;

    @Column(name = "formula_org_name")
    private String formulaOrgName;

    @Column(name = "max_expense_fee")
    private BigDecimal maxExpenseFee;

    @Column(name = "max_capital_fee")
    private BigDecimal maxCapitalFee;

    @Column(name = "max_payment_penalty_fee")
    private BigDecimal maxPaymentPenaltyFee;

    @Column(name = "max_payment_relation_fee")
    private BigDecimal maxPaymentRelationFee;

    @Column(name = "max_payment_current_capital_fee")
    private BigDecimal maxPaymentCurrentCapitalFee;

    @Column(name = "max_payment_regular_expense_fee")
    private BigDecimal maxPaymentRegularExpenseFee;

    @Column(name = "max_payment_other_capital_fee")
    private BigDecimal maxPaymentOtherCapitalFee;

    @Column(name = "max_payment_other_expense_fee")
    private BigDecimal maxPaymentOtherExpenseFee;
} 