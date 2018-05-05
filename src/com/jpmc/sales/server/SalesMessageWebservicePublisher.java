package com.jpmc.sales.server;

import javax.xml.ws.Endpoint;

public class SalesMessageWebservicePublisher {
	
	public static void main(String[] args) {
		   Endpoint.publish("http://localhost:8086/ws/jpmc-sales", new SalesMessageListener());
	    }
}
