package com.jpmc.sales.test;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.jpmc.sales.model.SalesMessage;
import com.jpmc.sales.service.SalesMessageService;
import com.jpmc.sales.util.SalesMessageUtility;

public class SalesMessageTest {
	
	private static final Logger LOGGER = Logger.getLogger( SalesMessageTest.class.getName() );
	
	@Test
	public void testVaidateMessageType1or2() {
		SalesMessage saleMessage = getNewSalesMessageObj("singled", null, "2", "1", "apple");
		Assert.assertEquals("Invalid Message Type value", SalesMessageUtility.validateMessage(saleMessage));
		
		saleMessage = getNewSalesMessageObj("single", null, "2b", "1", "apple");
		Assert.assertEquals("Invalid Price value. Should be numeric.", SalesMessageUtility.validateMessage(saleMessage));
		
		saleMessage = getNewSalesMessageObj("single", null, "2", "1", "apple");
		Assert.assertEquals("", SalesMessageUtility.validateMessage(saleMessage));
	}
	
	@Test
	public void testVaidateMessageType3() {
		SalesMessageService.PRODUCT_SALES_DATA.clear();
		SalesMessageService.ADJUSTMENT_DATA.clear();
		
		SalesMessage saleMessage = getNewSalesMessageObj("multiple", null, "15", "12", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("adjustment", "addition", "3", null, "apple");
		Assert.assertEquals("Invalid Adjustment Type value", SalesMessageUtility.validateMessage(saleMessage));
		
		saleMessage = getNewSalesMessageObj("adjustment", "add", "3b", null, "apple");
		Assert.assertEquals("Invalid Price value. Should be numeric.", SalesMessageUtility.validateMessage(saleMessage));
		
		saleMessage = getNewSalesMessageObj("adjustment", "add", "3", null, "apple");
		Assert.assertEquals("", SalesMessageUtility.validateMessage(saleMessage));
	}
	
	@Test
	public void testProcessMessageType1and2() {
		SalesMessageService.PRODUCT_SALES_DATA.clear();
		SalesMessageService.ADJUSTMENT_DATA.clear();
		
		SalesMessage saleMessage = getNewSalesMessageObj("single", null, "20", "1", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("multiple", null, "15", "12", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		Assert.assertEquals(new BigDecimal(200), SalesMessageService.PRODUCT_SALES_DATA.get("apple").getSalesValue());
		Assert.assertEquals("13", SalesMessageService.PRODUCT_SALES_DATA.get("apple").getQuantity());
	}
	
	@Test
	public void testProcessMessageType1And2And3() {

		SalesMessageService.PRODUCT_SALES_DATA.clear();
		SalesMessageService.ADJUSTMENT_DATA.clear();
		
		SalesMessage saleMessage = getNewSalesMessageObj("single", null, "20", "1", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("multiple", null, "15", "12", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("adjustment", "add", "5", null, "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		Assert.assertEquals(new BigDecimal(265), SalesMessageService.PRODUCT_SALES_DATA.get("apple").getSalesValue());
		Assert.assertEquals("13", SalesMessageService.PRODUCT_SALES_DATA.get("apple").getQuantity());
	}
	
	@Test
	public void testProcessMessageType1And2And3WithReports() {

		SalesMessageService.PRODUCT_SALES_DATA.clear();
		SalesMessageService.ADJUSTMENT_DATA.clear();
		
		SalesMessage saleMessage = getNewSalesMessageObj("single", null, "20", "1", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("multiple", null, "15", "12", "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		saleMessage = getNewSalesMessageObj("adjustment", "add", "5", null, "apple");
		SalesMessageUtility.processMessage(saleMessage);
		
		Assert.assertEquals(new BigDecimal(265), SalesMessageService.PRODUCT_SALES_DATA.get("apple").getSalesValue());
		Assert.assertEquals("13", SalesMessageService.PRODUCT_SALES_DATA.get("apple").getQuantity());
		
		SalesMessageUtility.writeSalesDetails(2);
		SalesMessageUtility.writeAdjustmentDetails(2);
	}
	
	@Test
	public void getNewSalesMessageObjJson() {
		SalesMessage saleMessage = getNewSalesMessageObj("multiple", null, "15", "12", "apple");
		
		Gson gson = new Gson();
		String salesMessageJson = gson.toJson(saleMessage);
		
		LOGGER.log(Level.INFO, StringEscapeUtils.escapeHtml(salesMessageJson));
	}
	
	private SalesMessage getNewSalesMessageObj(String messageType, String adjustmentType,
			String price, String quantity, String productType) {
		SalesMessage saleMessage = new SalesMessage();
		
		saleMessage.setMessageType(messageType);
		saleMessage.setAdjustmentType(adjustmentType);
		saleMessage.setPrice(price);
		saleMessage.setProductType(productType);
		saleMessage.setQuantity(quantity);
		
		return saleMessage;
	}
}
