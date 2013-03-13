package com.jebhomenye.hazelcast.service.command.http;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.model.Member;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.PieChart;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.Command;

import static com.jebhomenye.hazelcast.util.KeyBuilder.*;

public class PieChartCommand  implements Command<PieChart>{

	@Override
	public PieChart execute(Object...args) {
		ClusterState state = StateManager.getInstance().getClusterState();
		List<Member> members = new ArrayList<Member>();
		if(state != null){
			for(TimedMemberState memberState : state.getMemberStates().values()){
				members.add(new Member(buildAddressKey(memberState), (long)memberState.getMemberState().getPartitions().size()));
			}
		}
		
		PieChart chart = new PieChart(members, new Meta(state != null, ""));
		//System.out.println("chart: " + chart);
		return chart;
	}
	


}
