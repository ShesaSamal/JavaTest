package com.jpmc.sales.server;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.jpmc.sales.service.SalesMessageService;
/**
 * 
 * Message Listener.
 */
@WebService
public class SalesMessageListener {

	SalesMessageService messageService = new SalesMessageService();
	
	@WebMethod
	public String processMessage(String message) {
		return messageService.execute(message);
	}
}
