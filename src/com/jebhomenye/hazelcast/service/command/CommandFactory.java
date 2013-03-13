package com.jebhomenye.hazelcast.service.command;

import java.util.HashMap;
import java.util.Map;

import com.jebhomenye.hazelcast.service.command.http.BrowseMapCommand;
import com.jebhomenye.hazelcast.service.command.http.ClusterStateCommand;
import com.jebhomenye.hazelcast.service.command.http.MemberCommand;
import com.jebhomenye.hazelcast.service.command.http.MemoryChartCommand;
import com.jebhomenye.hazelcast.service.command.http.MemoryTableCommand;
import com.jebhomenye.hazelcast.service.command.http.PieChartCommand;
import com.jebhomenye.hazelcast.service.command.http.QueueMemoryChartCommand;
import com.jebhomenye.hazelcast.service.command.http.QueueThroughputChartCommand;
import com.jebhomenye.hazelcast.service.command.http.ThroughputChartCommand;
import com.jebhomenye.hazelcast.service.command.http.ThroughputTableCommand;


public class CommandFactory {
	private static Map<String, Command<?>> commands = new HashMap<String, Command<?>>();
	
	static{
		commands.put("pie", new PieChartCommand());
		commands.put("cluster", new  ClusterStateCommand());
		commands.put("member", new  MemberCommand());
		commands.put("iChart", new MemoryChartCommand());
		commands.put("iTable", new MemoryTableCommand());
		commands.put("oChart", new ThroughputChartCommand());
		commands.put("oTable", new ThroughputTableCommand());
		commands.put("browseMap", new BrowseMapCommand());
		
	}
	
	public static Command<?> get(String key){
		if(commands.containsKey(key)){
			return commands.get(key);
		}
		throw new RuntimeException("Invalid operatin attented : " + key);
	}
	
}
