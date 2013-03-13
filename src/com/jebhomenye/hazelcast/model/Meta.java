package com.jebhomenye.hazelcast.model;

import lombok.Data;

@Data
public class Meta {
	private boolean successful;
	private String message;
	
	public Meta(boolean successful, String message){
		this.successful = successful;
		this.message = message;
	}
	
}
