package com.jebhomenye.hazelcast.service.command.http;

import static com.jebhomenye.hazelcast.util.KeyBuilder.buildAddressKey;
import static java.lang.String.format;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.MemberState;
import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.Table;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.Command;

public class MemoryTableCommand implements Command<Map<String, Object>> {

	@Override
	public Map<String, Object> execute(Object...args) {
		HttpServletRequest request = (HttpServletRequest)args[0];
		String instance = request.getParameter("instance");
		
		List<ClusterState> history = StateManager.getInstance().getClusterStateHistory(20L);
		Table mapStats = getMemoryTableData(history.get(0), instance);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("mapStats", mapStats);
		result.put("meta", new Meta(!mapStats.isEmtpy(), ""));
		return result;
	}
	
	private Table getMemoryTableData(ClusterState clusterState, String instance){
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
					table.addRow(format("id:%s, member:%s, entry:%s, entry_memory:%s, backups:%s, backup_memory:%s, dirty:%s, marked:%s, marked_mem:%s, lock:%s",
							id, buildAddressKey(memberState)  ,mapStats.getOwnedEntryCount(), mapStats.getOwnedEntryMemoryCost()
							,mapStats.getBackupEntryCount(), mapStats.getBackupEntryMemoryCost(),
							mapStats.getDirtyEntryCount(), mapStats.getMarkedAsRemovedEntryCount(),
							mapStats.getMarkedAsRemovedMemoryCost(), mapStats.getLockedEntryCount()));
				}
			}
		}
		return table;
	}

}
