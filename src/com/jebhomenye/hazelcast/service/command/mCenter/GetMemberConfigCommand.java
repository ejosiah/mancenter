package com.jebhomenye.hazelcast.service.command.mCenter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.hazelcast.impl.management.MemberConfigRequest;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.JobProcessor;

public class GetMemberConfigCommand extends JobCommand<String> {

	public GetMemberConfigCommand(JobExecutor executor) {
		super(executor);
	}

	@Override
	public String execute(String cluster, String node) {
		try {
			JobProcessor processor = new JobProcessor(cluster, node, new MemberConfigRequest());
			return executor.submit(processor, String.class);
		} catch (InterruptedException e) {
			// TODO log error
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO log error
			e.printStackTrace();
		}
		return null;
	}

}
