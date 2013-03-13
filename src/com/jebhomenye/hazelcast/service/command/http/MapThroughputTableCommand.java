package com.jebhomenye.hazelcast.service.command.http;

import static com.jebhomenye.hazelcast.util.KeyBuilder.buildAddressKey;
import static java.lang.String.format;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.monitor.LocalMapOperationStats;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.MemberState;
import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.Table;
import com.jebhomenye.hazelcast.service.StateManager;

public class MapThroughputTableCommand extends HttpCommand<Map<String, Object>> {

	@Override
	public Map<String, Object> execute(HttpServletRequest request,	HttpServletResponse response) {
		String instance = request.getParameter("instance");
		
		List<ClusterState> history = StateManager.getInstance().getClusterStateHistory(20L);
		Table mapStats = getOperationsTableData(history.get(0), instance);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("mapStats", mapStats);
		result.put("meta", new Meta(!mapStats.isEmtpy(), ""));
		return result;
	}
	
	private Table getOperationsTableData(ClusterState clusterState, String instance){
		Table table = new Table();
		
		List<String> members = clusterState.getMembers();
		
		Integer id = 0;
		for(String member : members){
			TimedMemberState memberState = clusterState.getMemberStates().get(member);
			if(memberState != null){
				MemberState state = memberState.getMemberState();
				LocalMapStats mapStats = state.getLocalMapStats(instance);
				if(mapStats != null){
					id++;
					LocalMapOperationStats opStats = mapStats.getOperationStats();
					id++;
					table.addRow(format("id:%s, member:%s, total:%s, gets:%s, avg_get_letency:%s, puts:%s, avg_put_lentency:%s, removes:%s, avg_remove_le:%s, other_opts:%s, events:%s"
							,id ,buildAddressKey(memberState), getTotal(opStats), getGets(opStats),
							getAvgGetLetency(opStats), getPuts(opStats), getAvgPutLentency(opStats),
							getRemoves(opStats), getAvgRemoveLentency(opStats), getOtherOps(opStats)
							, getEvents(opStats)));
				}
			}
		}
		
		return table;
	}
	private Long getTotal(LocalMapOperationStats opStats){
		return (opStats.getNumberOfGets() + opStats.getNumberOfPuts() + opStats.getNumberOfRemoves() * 1000L)
				/getPeriodDiffs(opStats);
	}
	
	private Long getGets(LocalMapOperationStats opStats){
		return opStats.getNumberOfGets() * 1000l / getPeriodDiffs(opStats);
	}
	
	private Long getAvgGetLetency(LocalMapOperationStats opStats){
		return opStats.getNumberOfGets() != 0L ? opStats.getTotalGetLatency() / opStats.getNumberOfGets() : 0L;
	}
	
	private Long getPuts(LocalMapOperationStats opStats){
		return opStats.getNumberOfPuts() * 1000L / getPeriodDiffs(opStats);
	}
	
	private Long getAvgPutLentency(LocalMapOperationStats opStats){
		return opStats.getNumberOfPuts() != 0L ? opStats.getTotalPutLatency() / opStats.getNumberOfPuts() : 0L;
	}
	
	private Long getRemoves(LocalMapOperationStats opStats) {
		return opStats.getNumberOfRemoves() * 1000L / getPeriodDiffs(opStats);
	}
	private Long getAvgRemoveLentency(LocalMapOperationStats opStats) {
		return opStats.getNumberOfRemoves() != 0L ? opStats.getTotalRemoveLatency() / opStats.getNumberOfPuts() : 0L;
	}
	
	private Long getOtherOps(LocalMapOperationStats opStats) {
		return opStats.getNumberOfOtherOperations() * 1000L / getPeriodDiffs(opStats);
	}
	
	private Long getEvents(LocalMapOperationStats opStats){
		return opStats.getNumberOfEvents() * 1000L / getPeriodDiffs(opStats);
	}
	
	private Long getPeriodDiffs(LocalMapOperationStats opStats){
		return Math.max(1L, opStats.getPeriodEnd() - opStats.getPeriodStart());
	}

}
