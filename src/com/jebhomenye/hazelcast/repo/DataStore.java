package com.jebhomenye.hazelcast.repo;

import java.util.Iterator;

import com.jebhomenye.hazelcast.model.ClusterState;

public interface DataStore {
	
	ClusterState get(String key);
	
	void save(String key, ClusterState state);
	
	Iterator<ClusterState> clusterIterator();
	
	Iterator<String> keyIterator();

	int size();
}
