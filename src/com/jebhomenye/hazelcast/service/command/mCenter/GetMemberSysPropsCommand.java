package com.jebhomenye.hazelcast.service.command.mCenter;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hazelcast.impl.management.GetMemberSystemPropertiesRequest;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.JobProcessor;

public class GetMemberSysPropsCommand extends JobCommand<Map<String, String>>{

	public GetMemberSysPropsCommand(JobExecutor executor) {
		super(executor);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> execute(String cluster, String node) {
		try {
			JobProcessor processor = new JobProcessor(cluster, node, new GetMemberSystemPropertiesRequest());
			return executor.submit(processor, Map.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
