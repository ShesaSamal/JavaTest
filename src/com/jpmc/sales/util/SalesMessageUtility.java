package com.jpmc.sales.util;

import static com.jpmc.sales.service.SalesMessageService.ADJUSTMENT_DATA;
import static com.jpmc.sales.service.SalesMessageService.PRODUCT_SALES_DATA;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.jpmc.sales.model.AdjustmentDetail;
import com.jpmc.sales.model.AdjustmentType;
import com.jpmc.sales.model.MessageType;
import com.jpmc.sales.model.SalesMessage;

public class SalesMessageUtility {
	
	private static final Logger LOGGER = Logger.getLogger( SalesMessageUtility.class.getName() );
	
	/**
	 * Process the incoming sales message
	 */
	public static void processMessage(SalesMessage inputSalesMessage) {
		if ( ! PRODUCT_SALES_DATA.containsKey(inputSalesMessage.getProductType())) {
			inputSalesMessage.setSalesValue(new BigDecimal(inputSalesMessage.getPrice()).multiply(new BigDecimal(inputSalesMessage.getQuantity())));
			PRODUCT_SALES_DATA.put(inputSalesMessage.getProductType(), inputSalesMessage);
			
		} else {
			SalesMessage salesMessage = PRODUCT_SALES_DATA.get(inputSalesMessage.getProductType());

			if (MessageType.ADJUSTMENT.getMessageTypeVal().equals(inputSalesMessage.getMessageType())) {
				BigDecimal presentSalesValue = salesMessage.getSalesValue();
				if (AdjustmentType.ADD.getAdjustmentTypeVal().equals(inputSalesMessage.getAdjustmentType()))
					salesMessage.setSalesValue(presentSalesValue.add(new BigDecimal(salesMessage.getQuantity()).multiply(new BigDecimal(inputSalesMessage.getPrice()))));
				if (AdjustmentType.SUBTRACT.getAdjustmentTypeVal().equals(inputSalesMessage.getAdjustmentType()))
					salesMessage.setSalesValue(presentSalesValue.subtract(new BigDecimal(salesMessage.getQuantity()).multiply(new BigDecimal(inputSalesMessage.getPrice()))));
				if (AdjustmentType.MULTIPLY.getAdjustmentTypeVal().equals(inputSalesMessage.getAdjustmentType()))
					salesMessage.setSalesValue(presentSalesValue.multiply(new BigDecimal(salesMessage.getQuantity()).multiply(new BigDecimal(inputSalesMessage.getPrice()))));
				ADJUSTMENT_DATA.add(new AdjustmentDetail(inputSalesMessage.getProductType(),
						inputSalesMessage.getAdjustmentType(), new BigDecimal(inputSalesMessage.getPrice()), presentSalesValue, salesMessage.getSalesValue()));
			}else {
				salesMessage.setQuantity(String.valueOf(Integer.parseInt(salesMessage.getQuantity())+Integer.parseInt(inputSalesMessage.getQuantity())));
				salesMessage.setSalesValue(salesMessage.getSalesValue().add(new BigDecimal(inputSalesMessage.getQuantity()).multiply(new BigDecimal(inputSalesMessage.getPrice()))));
			}
		}
		
	}
	
	/**
	 * Validate message attributes
	 * @param saleMessage a SalesMessage object
	 * @return an empty String if message is valid
	 */
	public static String validateMessage(SalesMessage saleMessage) {
		String response = "";
		if(!MessageType.checkIfEnumExists(saleMessage.getMessageType())) {
			response = "Invalid Message Type value";
		}else {
			if(MessageType.ADJUSTMENT.getMessageTypeVal().equals(saleMessage.getMessageType())) {
				if(!AdjustmentType.checkIfEnumExists(saleMessage.getAdjustmentType())) {
					response = "Invalid Adjustment Type value";
				}else if(StringUtils.isBlank(saleMessage.getProductType()) || !PRODUCT_SALES_DATA.containsKey(saleMessage.getProductType())){
					response = "Invalid Product Type value for adjustment message type";
				}else if(StringUtils.isBlank(saleMessage.getPrice()) || !StringUtils.isNumeric(saleMessage.getPrice())){
					response = "Invalid Price value. Should be numeric.";
				}
			}else {
				if(StringUtils.isBlank(saleMessage.getPrice()) || !StringUtils.isNumeric(saleMessage.getPrice())){
					response = "Invalid Price value. Should be numeric.";
				}else if(StringUtils.isBlank(saleMessage.getQuantity()) || !StringUtils.isNumeric(saleMessage.getQuantity())){
					response = "Invalid Quantity value. Should be numeric.";
				}
			}
		}
		
		return response;
	}

	public static void writeSalesDetails(long messageCount) {
		LOGGER.log(Level.INFO, "*************** Printing Sales Details after "+messageCount+ "Messages ****************");
		Iterator<Entry<String, SalesMessage>> entryItr = PRODUCT_SALES_DATA.entrySet().iterator();
		
		Entry<String, SalesMessage> entryObj = null;
		while(entryItr.hasNext()) {
			entryObj = entryItr.next();

			LOGGER.log(Level.INFO, "Product Name :: "+entryObj.getValue().getProductType() +", Quantity :: "+entryObj.getValue().getQuantity() + ", Sales Value :: "+entryObj.getValue().getSalesValue());
		}
		LOGGER.log(Level.INFO, "***************** End of Sales Details ************************************");
	}
	
	public static void writeAdjustmentDetails(long messageCount) {
		LOGGER.log(Level.INFO, "*************** Printing Adjustment Details after "+messageCount+ "Messages ****************");
		for(AdjustmentDetail adjDtl : ADJUSTMENT_DATA) {
			LOGGER.log(Level.INFO, "Product Name :: " + adjDtl.getProductType()+", Operation :: "+adjDtl.getAdjustmentType() 
					+", Adjustment Value :: "+adjDtl.getAdjustmentvalue() 
					+ ", Before Sales Value :: "+adjDtl.getBeforeValue()
					+ ", After Adjustment Sales Value :: "+adjDtl.getAfterValue());
		}
		LOGGER.log(Level.INFO, "***************** End of Adjustment Details ************************************");
	}
}
