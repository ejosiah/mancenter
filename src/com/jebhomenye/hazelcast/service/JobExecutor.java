package com.jebhomenye.hazelcast.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class JobExecutor {
	private static JobExecutor instance = new JobExecutor();
	private ExecutorService executor;
	private final int MAX_THREADS = 20;
	
	private JobExecutor(){
		executor = Executors.newFixedThreadPool(MAX_THREADS);
	}
	
	public static JobExecutor getInstance(){
		return instance;
	}

	public <T> T submit(JobProcessor processor, Class<T> returnType) throws InterruptedException, ExecutionException {
		return returnType.cast(executor.submit(processor).get());
	}
}
