package com.jebhomenye.hazelcast.service.command.mCenter;

import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.command.Command;

public abstract class JobCommand<T> implements Command<T> {
	
	protected JobExecutor executor;
	
	public JobCommand(JobExecutor executor){
		this.executor = executor;
	}

	@Override
	public T execute(Object... args) {
		return execute((String)args[0], (String)args[1]);
	}
	
	public abstract T execute(String cluster, String node);

}
