package com.jebhomenye.hazelcast.repo;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.ToString;

import com.jebhomenye.hazelcast.model.ClusterState;

@ToString
public class MemoryStore implements DataStore {
	private final Map<String, ClusterState> map = new ConcurrentHashMap<String, ClusterState>();

	@Override
	public ClusterState get(String key) {
		return map.get(key);
	}

	@Override
	public void save(String key, ClusterState state) {
		map.put(key, state);	
	}

	@Override
	public Iterator<ClusterState> clusterIterator() {
		return map.values().iterator();
	}

	@Override
	public Iterator<String> keyIterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}
	
	
}
