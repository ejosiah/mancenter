package com.jebhomenye.hazelcast.service;

import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.hazelcast.impl.management.MapConfigRequest;
import com.hazelcast.nio.Address;
import com.hazelcast.util.AddressUtil;
import com.hazelcast.util.AddressUtil.AddressHolder;

public class JobManager {
	private static final JobManager instance = new JobManager();
	private final AtomicInteger jobId = new AtomicInteger(0);
	private final ListMultimap<String, Job> nodeRequests;
	private final ListMultimap<String, Job> clusterRequests;
	private final Map<Integer, Object> responses = new ConcurrentHashMap<>();
	
	private JobManager() {
		ListMultimap<String, Job> unSynced = ArrayListMultimap.create();
		nodeRequests = Multimaps.synchronizedListMultimap(unSynced);
		
		unSynced = ArrayListMultimap.create();
		clusterRequests = Multimaps.synchronizedListMultimap(unSynced);
	}
	
	public static JobManager getInstance(){
		return instance;
	}
	
	public Integer putRequest(Job job){
		int id = jobId.incrementAndGet();
		job.jobId = id;
		if(job.node != null && !job.node.equals("")){
			nodeRequests.put(job.node, job);
		}else{
			clusterRequests.put(job.cluster, job);
		}
		return job.jobId;
	}

	public Object getResponse(Integer jobId) {
		return responses.remove(jobId);
	}
	
	public void putResponse(Integer jobId, Object response){
		responses.put(jobId, response);
	}
	
	public synchronized Job getRequest(String cluster, String member){
		Job job = null;
		
		try {
			if(nodeRequests.containsKey(member)){
				job = nodeRequests.get(member).remove(0);
			}else if(clusterRequests.containsKey(cluster)){
				job = clusterRequests.get(cluster).remove(0);
				if(job.request instanceof MapConfigRequest){
					job.node = member;
					AddressHolder holder = AddressUtil.getAddressHolder(member);
					((MapConfigRequest)job.request).setTarget(new Address(holder.address, holder.port));
				}else if(clusterRequests.containsKey("*")){
					job = clusterRequests.get("*").remove(0);
				}
			}
		} catch (UnknownHostException e) {
			// TODO log error
			e.printStackTrace();
		}
		
		return job;
	}

	
}
