package com.jpmc.sales.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jpmc.sales.model.AdjustmentDetail;
import com.jpmc.sales.model.SalesMessage;
import com.jpmc.sales.util.SalesMessageUtility;

/**
 * Sales Mesage processing service class.
 *
 */
public class SalesMessageService {
	
	private static final Logger LOGGER = Logger.getLogger( SalesMessageService.class.getName() );
	
	//Map to hold all the input messages, ordered by insertion sequence
	public static Map<Long, String> INPUT_MESSAGES = new LinkedHashMap<>();
	
	//Sales data consolidated by product type
	public static Map<String, SalesMessage> PRODUCT_SALES_DATA = new HashMap<>();
	
	// Adjustment data consolidated by product type
	public static List<AdjustmentDetail> ADJUSTMENT_DATA = new ArrayList<>();
	
	private static int SALES_LOG_COUNT = 10;
	private static int ADJUSTMENT_PAUSE_COUNT = 50;
	private static long ID = 0;
	private static int ADJ_COUNTER = 0;
	
	
	public SalesMessageService() {
	}

	/**
	 * Execute the input message from client.
	 * @param inputMessage
	 * @return
	 */
	public String execute(String inputMessage) {
		// Check if messages do not need to consume, return -1
		if (ADJ_COUNTER == ADJUSTMENT_PAUSE_COUNT) {
			return "SERVICE_PAUSED";
		}
		
		JsonElement root = new JsonParser().parse(new StringReader(StringEscapeUtils.unescapeHtml(inputMessage)));
		//Get the content of the first map
		JsonObject object = root.getAsJsonObject();

		//Iterate over this map
		Gson gson = new Gson();
		SalesMessage salesMessage = gson.fromJson(object, SalesMessage.class);

		long messageId = -1;
		String validationResponse = SalesMessageUtility.validateMessage(salesMessage);
		if(StringUtils.isBlank(validationResponse)) {
			messageId = storeMessage(inputMessage);
			SalesMessageUtility.processMessage(salesMessage);
			reportDetials();
			
			return "ID:: "+messageId;
		}else {
			return validationResponse;
		}
		
	}
	
	private Long storeMessage(String message) {
		LOGGER.log(Level.INFO, "Input Message :: "+ message);
		INPUT_MESSAGES.put(ID++, message);
		ADJ_COUNTER++;
		return ID;
		
	}
	
	/**
	 * report the details of sales and adjustment made
	 */
	private void reportDetials() {
		if (ID % SALES_LOG_COUNT == 0) {
			SalesMessageUtility.writeSalesDetails(ID);
		}
		if (ADJ_COUNTER == ADJUSTMENT_PAUSE_COUNT) {
			LOGGER.log(Level.INFO, "::................Service going to be paused to print Adjustment details after 50 messages...............:: ");
			SalesMessageUtility.writeAdjustmentDetails(ID);
			
			// Clearing Adjustment Data holder to prepare for next 
			ADJUSTMENT_DATA.clear();
			ADJ_COUNTER = 0;
		}
		
	}	
}
