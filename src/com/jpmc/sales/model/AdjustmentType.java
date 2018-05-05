package com.jpmc.sales.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Adjustment type, represent types of adjustment supported in sales adjustment message type
 *
 */
public enum AdjustmentType {
	ADD("add"),
	SUBTRACT("subtract"),
	MULTIPLY("multiply");
	
	private String adjustmentTypeVal;
	
	AdjustmentType(String adjustmentTypeVal) {
		this.adjustmentTypeVal = adjustmentTypeVal;
	}
	
	public String getAdjustmentTypeVal() {
		return adjustmentTypeVal;
	}
	
	private static Set<String> adjustmentTypes = new HashSet<>();
	
	public static boolean checkIfEnumExists(String adjustmentType) {
		if(adjustmentTypes.isEmpty()) {
			for (AdjustmentType mt : AdjustmentType.values()) {
				adjustmentTypes.add(mt.getAdjustmentTypeVal());
		    }
		}
		return adjustmentTypes.contains(adjustmentType);
	}
}
