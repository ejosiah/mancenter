package com.jebhomenye.hazelcast.service.command.http;


import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.Table;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.Command;

public class QueueMemoryChartCommand implements Command<Map<String, Object>> {

	@Override
	public Map<String, Object> execute(Object...args) {
		HttpServletRequest request = (HttpServletRequest) args[0];
		String instance = request.getParameter("instance");
		
		List<ClusterState> history = StateManager.getInstance().getClusterStateHistory(20L);
		Table memoryChat = getChartData(history, instance);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("chart", memoryChat);
		result.put("meta", new Meta(!memoryChat.isEmtpy(), "good stuff"));
		System.out.println(result);
		return result;
	}
	
	private Table getChartData(List<ClusterState> history, String instance){
		Collections.reverse(history);
		
		Table chartData = new Table();
		for(ClusterState state : history){
			
			Number data = state.getInstanceData(instance, "Queue", "OwnedItemCount").doubleValue();
			long time = state.getTime() - TimeZone.getDefault().getOffset(state.getTime());
			
			Map<String, Object> row = new LinkedHashMap<String, Object>();
			row.put("Size", data);
			row.put("time", timeToString(time));
			chartData.addRow(row);
		}
		
		
		return chartData;
	}
	
	private String timeToString(long time){
		Pattern p = Pattern.compile("\\d{1,2}:\\d{1,2}:\\d{1,2}");
		String date = new Date(time).toString();
		System.out.println("date : " + date + ", time: " + time);
		Matcher m = p.matcher(date);
		String result = "";
		if(m.find()){
			result = m.group();
			System.out.println("result: " + result);
		}
		return result;
	}

}
