package com.jpmc.sales.model;

import java.math.BigDecimal;

/**
 * 
 * Sales Message model
 *
 */
public class SalesMessage{

	private String messageType;
	private String productType;
	private String quantity;
	private String price;
	private BigDecimal salesValue;
	private String adjustmentType;
	
	public SalesMessage() {}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public void setSalesValue(BigDecimal salesValue) {
		this.salesValue = salesValue;
	}
	public BigDecimal getSalesValue() {
		return salesValue;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
