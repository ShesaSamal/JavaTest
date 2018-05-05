package com.jpmc.sales.model;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Message type enum
 */
public enum MessageType {
	SINGLE_SALE("single"),
	MULTIPLE_SALE("multiple"),
	ADJUSTMENT("adjustment");
	
	private String messageTypeVal;
	
	MessageType(String messageTypeVal) {
		this.messageTypeVal = messageTypeVal;
	}
	
	public String getMessageTypeVal() {
		return messageTypeVal;
	}
	
	private static Set<String> messageTypes = new HashSet<>();
	
	public static boolean checkIfEnumExists(String messageType) {
		if(messageTypes.isEmpty()) {
			for (MessageType mt : MessageType.values()) {
				messageTypes.add(mt.getMessageTypeVal());
		    }
		}
		return messageTypes.contains(messageType);
	}
}
