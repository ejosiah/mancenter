package com.jebhomenye.hazelcast.service.command.http;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jebhomenye.hazelcast.model.MapEntry;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.assemby.MapEntryAssembler;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.mCenter.GetMapEntryCommand;
import com.jebhomenye.hazelcast.service.command.mCenter.JobCommand;

public class BrowseMapCommand extends HttpCommand<Map<String, Object>>{

	@Override
	public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String key = request.getParameter("key");
		
		String cluster = StateManager.getInstance().getCluster();
		JobCommand<Map<String, String>> command = new GetMapEntryCommand(JobExecutor.getInstance(), type, name, key);
		MapEntry entry = MapEntryAssembler.assemble(command.execute(cluster, null));
		
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("entry", entry);
		result.put("meta", new Meta(true, "entry data available"));
		
		return result;
	}

}
