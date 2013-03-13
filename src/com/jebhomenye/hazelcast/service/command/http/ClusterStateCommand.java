package com.jebhomenye.hazelcast.service.command.http;

import com.jebhomenye.hazelcast.model.Cluster;
import com.jebhomenye.hazelcast.model.assemby.ClusterAssembler;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.Command;

public class ClusterStateCommand implements Command<Cluster> {

	@Override
	public Cluster execute(Object...args) {
		return ClusterAssembler.assemble(StateManager.getInstance().getClusterState());
	}

}
