package com.jebhomenye.hazelcast.service;

import java.util.concurrent.Callable;

import com.hazelcast.impl.management.ConsoleRequest;

public class JobProcessor implements Callable<Object> {
	private final Job job;
	private final Integer requestLimit;
	private static final Integer DEFAULT_REQUEST_LIMIT = 30;
	
	public JobProcessor(String cluster, String node, ConsoleRequest request){
		this(new Job(cluster, node, request));
	}
	
	public JobProcessor(Job job){
		this(job, DEFAULT_REQUEST_LIMIT);
	}
	
	public JobProcessor(Job job, Integer redo){
		this.job = job;
		this.requestLimit = redo;
	}
	

	@Override
	public Object call(){
		Object response = null;
		
		JobManager manager = JobManager.getInstance();
		int jobId = manager.putRequest(job);
		System.out.println("sent job request " + job);
		
		try {
			for(int i = 0; response == null && (i < requestLimit || requestLimit == 0); i++){
				Thread.sleep(1000L);
				response = manager.getResponse(jobId);
			}
		} catch (InterruptedException e) {
			// TODO log error
			e.printStackTrace();
		}
		
		System.out.println(response);
		
		return response;
	}

}
