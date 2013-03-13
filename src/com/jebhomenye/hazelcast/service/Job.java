package com.jebhomenye.hazelcast.service;

import com.hazelcast.impl.management.ConsoleRequest;

import lombok.Data;

@Data
public class Job {
	Integer jobId;
	String cluster;
	String node;
	ConsoleRequest request;
	
	public Job(String cluster, String node, ConsoleRequest request){
		this.cluster = cluster;
		this.node = node;
		this.request = request;
	}
}
