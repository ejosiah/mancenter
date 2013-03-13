package com.jebhomenye.hazelcast.service.command.mCenter;

import java.util.Map;

import lombok.SneakyThrows;

import com.hazelcast.impl.management.GetMapEntryRequest;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.JobProcessor;

public class GetMapEntryCommand extends JobCommand<Map<String, String>> {
	private String type;
	private String mapName;
	private String key;
	
	public GetMapEntryCommand(JobExecutor executor, String type, String mapName, String key){
		this(executor);
		this.type = type;
		this.mapName = mapName;
		this.key = key;
	}

	public GetMapEntryCommand(JobExecutor executor) {
		super(executor);
	}

	@Override
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public Map<String, String> execute(String cluster, String node) {
		GetMapEntryRequest request = new GetMapEntryRequest(type, mapName, key);
		JobProcessor processor = new JobProcessor(cluster, node, request);
		return executor.submit(processor, Map.class);
	}

}
