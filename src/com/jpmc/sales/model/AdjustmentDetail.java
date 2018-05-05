package com.jpmc.sales.model;

import java.math.BigDecimal;

/**
 * Adjustment detail class
 *
 */
public class AdjustmentDetail {
	private String productType;
	private String adjustmentType;
	private BigDecimal adjustmentvalue;
	private BigDecimal beforeValue;
	private BigDecimal afterValue;
	public AdjustmentDetail(String productType, String adjustmentType, BigDecimal adjustmentvalue, BigDecimal beforeValue, BigDecimal afterValue) {
		this.productType = productType;
		this.adjustmentType = adjustmentType;
		this.adjustmentvalue = adjustmentvalue;
		this.beforeValue = beforeValue;
		this.afterValue = afterValue;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}
	public BigDecimal getAdjustmentvalue() {
		return adjustmentvalue;
	}
	public BigDecimal getBeforeValue() {
		return beforeValue;
	}
	public BigDecimal getAfterValue() {
		return afterValue;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}	

}
