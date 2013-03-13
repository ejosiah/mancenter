package com.jebhomenye.hazelcast.model;

import java.util.List;

import lombok.Data;

@Data
public class PieChart {
	private List<Member> members;
	private Meta meta;
	
	public PieChart(List<Member> members, Meta meta){
		this.members = members;
		this.meta = meta;
	}	
}
