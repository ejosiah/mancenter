package com.jebhomenye.hazelcast.model.assemby;

import static com.jebhomenye.hazelcast.util.TimeUtil.*;
import java.util.Date;
import java.util.Map;

import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.Cluster;
import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.Runtime;
import com.jebhomenye.hazelcast.util.Util;

public final class ClusterAssembler {
	
	private ClusterAssembler() {}
	
	public static Cluster assemble(ClusterState clusterState){
		Cluster cluster = new Cluster();
		
		if (clusterState != null) {
			cluster.setExecutors(clusterState.getExecutors());
			cluster.setMaps(clusterState.getMaps());
			cluster.setMultiMaps(clusterState.getMultiMaps());
			cluster.setMembers(clusterState.getMembers());
			cluster.setQueues(clusterState.getQueues());
			cluster.setRuntime(getRuntime(clusterState.getMemberStates()));
			cluster.setMeta(new Meta(true, "cluster is live"));
		}else{
			cluster.setMeta(new Meta(false, "cluster is not live"));
		}
		return cluster;
	}

	private static Runtime getRuntime(Map<String, TimedMemberState> memberStates) {
		Map<String, Long> map = null;
		for(TimedMemberState state : memberStates.values()){
			if(state.getMaster()){
				map = state.getMemberState().getRuntimeProps();
			}
		}
		
		return RuntimeAssember.assemble(map);
	}

}
