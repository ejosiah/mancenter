package com.jebhomenye.hazelcast.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static com.jebhomenye.hazelcast.util.KeyBuilder.*;

import com.hazelcast.monitor.MemberState;
import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.ClusterState;
import com.jebhomenye.hazelcast.repo.CompositeStore;
import com.jebhomenye.hazelcast.repo.DataStore;
import com.jebhomenye.hazelcast.repo.MemoryStore;

public class StateManager {
	
	private static final long INTERVAL = 5000l;
	private volatile static StateManager instance = new StateManager();
	private final Set<String> clusterNames = new HashSet<String>();
	private final Map<String, List<String>> livemembers = new ConcurrentHashMap<String, List<String>>();
	private final Map<String, Long> lastConnect = new ConcurrentHashMap<String, Long>();
	private DataStore dataStore;
	
	private StateManager(){
		dataStore = new CompositeStore();
	}
	
	public static StateManager getInstance(){
		return instance;
	}
	
	public synchronized void addMemberState(TimedMemberState memberState){
		String cluster = memberState.getClusterName();
		clusterNames.add(cluster);
		long now = System.currentTimeMillis();
		long time = floor(now);
		memberState.setTime(time);
		
		updateLastConnect(memberState, now);
		ClusterState clusterState = updateClusterState(memberState, time);
		
		if(memberState.getMaster()){
			livemembers.put(cluster, memberState.getMemberList());
			List<String> members = livemembers.get(cluster);
			List<String> removeList = new ArrayList<String>();
			
			if(clusterState != null){
				for(TimedMemberState state : clusterState.getMemberStates().values()){
					String addressKey = buildAddressKey(state);
					if(!members.contains(addressKey)){
						removeList.add(addressKey);
					}	
				}
				for(String key : removeList){
					clusterState.getMemberStates().remove(key);
				}
			}
		}
		if(livemembers.get(cluster) == null){
			livemembers.put(cluster, new ArrayList<String>());
		}
		clusterState.setMembers(livemembers.get(cluster));
		clusterState.setExecutors(memberState.getExecutorList());
		clusterState.addState(memberState);
		
		for(String instanceName : memberState.getInstanceNames()){
			if(instanceName.startsWith("c:")){
				clusterState.getMaps().add(instanceName.substring(2));
			}else if(instanceName.startsWith("m:")){
				clusterState.getMultiMaps().add(instanceName.substring(2));
			}else if(instanceName.startsWith("q:")){
				clusterState.getQueues().add(instanceName.substring(2));
			}else if(instanceName.startsWith("t:")){
				clusterState.getTopics().add(instanceName.substring(2));
			}
		}
		
		dataStore.save(buildClusterKey(memberState, time), clusterState);

	}


	private void log(ClusterState clusterState) {
		System.out.println("memberList:");
		for(String member : clusterState.getMembers()){
			System.out.println("\t" + member);
		}
		System.out.print("\n\n");
		System.out.println("maps");
		for(String maps : clusterState.getMaps()){
			System.out.println("\t" + maps);
		}
		System.out.print("\n\n");
		System.out.println("runtime");
		MemberState state = clusterState.getMemberStates().values().iterator().next().getMemberState();
		for(Entry<String, Long> entry: state.getRuntimeProps().entrySet()){
			System.out.format("\t%s : %s\n", entry.getKey(), entry.getValue());
		}
		System.out.print("\n\n");
		System.out.print("\n\n");
		System.out.print("\n\n");
		
		
	}

	private Long floor(Long now) {
		return now - (now % INTERVAL);
	}
	
	private void updateLastConnect(TimedMemberState memberState, Long time) {
		String key = buildConnectKey(memberState);
		lastConnect.put(key, time);
		
	}
	
	private ClusterState updateClusterState(TimedMemberState memberState, long time) {
		ClusterState clusterState = dataStore.get(buildClusterKey(memberState, time));
		if(clusterState == null){
			clusterState = new ClusterState();
			clusterState.setTime(time);
			
			Long prevTime = time - INTERVAL;
			ClusterState prevState = dataStore.get(buildClusterKey(memberState, prevTime));
			
			if(prevState != null && prevState.getMemberStates() != null){
				for(TimedMemberState state : prevState.getMemberStates().values()){
					TimedMemberState copy = state.clone();
					copy.setTime(time);
					clusterState.addState(copy);
				}
			}
		}
		return clusterState;
		
	}
	
	private ClusterState getClusterState(String key){
		return dataStore.get(key);
	}
	
	public ClusterState getClusterState(Long currentTime){
		ClusterState state = null;
		
		if(!clusterNames.isEmpty()){
			Long time = getTime(floor(currentTime));
			String cluster = clusterNames.iterator().next();
			String key =  buildClusterKey(cluster,time);
			if((state = getClusterState(key)) == null){
				time -= INTERVAL;
				key = buildClusterKey(cluster, time);
				state = getClusterState(key);
			}
		}
		return state;
	}
	
	public ClusterState getClusterState(){
		return getClusterState(0l);
	}
	
	public List<ClusterState> getClusterStateHistory(Long limit){
		List<ClusterState> history = new ArrayList<ClusterState>();
		Long time = floor(System.currentTimeMillis());
		
		for(int i = 0;  i <= limit; i++){
			ClusterState state = getClusterState(buildClusterKey(clusterNames.iterator().next(), time)); 
			if (state != null) {
				history.add(state);
			}
			time -= INTERVAL;
		}
		return history;
	}
	
	
	private Long getTime(Long curtime){
		return curtime <= 0L ? floor(System.currentTimeMillis() - 5000L) : floor(curtime.longValue());
	}

	public TimedMemberState getTimedMember(String key) {
		return getClusterState().getMemberStates().get(key);
	}

	public String getCluster() {
		return clusterNames.iterator().next();
	}

}
