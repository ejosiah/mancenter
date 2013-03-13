package com.jebhomenye.hazelcast.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.ToString;

import com.hazelcast.monitor.LocalInstanceOperationStats;
import com.hazelcast.monitor.LocalInstanceStats;
import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.util.MethodUtils;

import static com.jebhomenye.hazelcast.util.KeyBuilder.*;

@ToString
public class ClusterState implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private long time;
	private Map<String, TimedMemberState> memberStates = new ConcurrentHashMap<String, TimedMemberState>();
	private List<String> members = new ArrayList<String>();
	private List<String> executors = new ArrayList<String>();
	private Set<String> maps = new LinkedHashSet<String>();
	private Set<String> multiMaps = new LinkedHashSet<String>();
	private Set<String> queues = new LinkedHashSet<String>();
	private Set<String> topics = new LinkedHashSet<String>();
	
	public void setTime(long time) {
		this.time = time;
		
	}

	public Map<String, TimedMemberState> getMemberStates() {
		return memberStates;
	}

	public void addState(TimedMemberState memberState) {
		memberStates.put(buildMemberKey(memberState), memberState);
		
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public List<String> getExecutors() {
		return executors;
	}

	public void setExecutors(List<String> executors) {
		this.executors = executors;
	}

	public Set<String> getMaps() {
		return maps;
	}

	public void setMaps(Set<String> maps) {
		this.maps = maps;
	}

	public Set<String> getMultiMaps() {
		return multiMaps;
	}

	public void setMultiMaps(Set<String> multiMaps) {
		this.multiMaps = multiMaps;
	}

	public Set<String> getQueues() {
		return queues;
	}

	public void setQueues(Set<String> queues) {
		this.queues = queues;
	}

	public long getTime() {
		return time;
	}

	public void setMemberStates(Map<String, TimedMemberState> memberStates) {
		this.memberStates = memberStates;
	}

	public Set<String> getTopics() {
		return topics;
	}

	public void setTopics(Set<String> topics) {
		this.topics = topics;
	}
	
	@SuppressWarnings("rawtypes")
	public Number getInstanceData(String instance, String instanceType, String property){
		Long result = 0L;
		
		for(String member : members){
			TimedMemberState memberState = memberStates.get(member);
			if(memberState != null){
				
				LocalInstanceStats stats = (LocalInstanceStats) MethodUtils.invoke(buildMethod(instanceType), memberState.getMemberState(), instance);
				if(stats != null){
					result += ((Number)MethodUtils.invoke(
							new StringBuilder("get").append(property).toString(), stats)).longValue();
			/*		if(instanceType.equals("Map")){
					}else{
						result += ((Number)MethodUtils.invoke(
								new StringBuilder("get").append("Total").toString(), stats)).longValue();
					}*/
				}
			}
		}
		return result / 1000D;
	}
	
	private String buildMethod(String instanceType){
		return new StringBuilder("getLocal").append(instanceType)
				.append("Stats").toString();
	}
	
	@SuppressWarnings("rawtypes")
	public Number getInstanceOperationData(String instance, String instanceType, String property){
		Double total = 0.0D;
		
		for(String member : members){
			TimedMemberState memberState = memberStates.get(member);
			if(memberState != null){
				
				LocalInstanceStats stats = (LocalInstanceStats) MethodUtils.invoke(buildMethod(instanceType), memberState.getMemberState(), instance);
				if(stats != null){
					LocalInstanceOperationStats opStats = stats.getOperationStats();
					Long result = 0L;
					if(property.equals("total")){
						result = ((Number)MethodUtils.invoke("total", opStats)).longValue();
					}else{
						result = ((Number)MethodUtils.invoke(new StringBuilder("get").append(property).toString(), opStats)).longValue();
								
					}
					
					total += result.doubleValue() * 1000D / (double)(opStats.getPeriodEnd() - opStats.getPeriodStart());
				}
			}
		}
		return Math.round(total);
	}

}
