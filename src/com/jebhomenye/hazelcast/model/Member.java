package com.jebhomenye.hazelcast.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Member {
	private String label;
	private Long data;
	private Runtime runtime;
	private List<Integer> partitions;
	private Map<String, String> systemProps;
	private String memberConfig;
	
	public Member(String label, Long data){
		this.label = label;
		this.data = data;
	}

	public Member() {
	}
	
}
