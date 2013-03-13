package com.jebhomenye.hazelcast.service.command.mCenter;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hazelcast.impl.management.ClusterPropsRequest;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.JobProcessor;

public class GetClusterPropsCommand extends JobCommand<Map<String, String>> {

	public GetClusterPropsCommand(JobExecutor executor) {
		super(executor);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> execute(String cluster, String node) {
		try{
			JobProcessor processor = new JobProcessor(cluster, node, new ClusterPropsRequest());
			return executor.submit(processor, Map.class);
		}catch(InterruptedException e){
			// TODO log error
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO log error
			e.printStackTrace();
		}
		return null;
	}

}
