package com.jebhomenye.hazelcast.repo;

import java.util.Iterator;

import com.jebhomenye.hazelcast.model.ClusterState;

public class CompositeStore implements DataStore {
	private DataStore dataStore;
	
	public CompositeStore(){
		this(WriteMode.DISK);
	}

	public CompositeStore(WriteMode writeMode) {
		if(writeMode.equals(WriteMode.DISK)){
			dataStore = new DiskDataStore();
		}else{
			dataStore = new MemoryStore();
		}
	}

	@Override
	public ClusterState get(String key) {
		return dataStore.get(key);
	}

	@Override
	public void save(String key, ClusterState state) {
		dataStore.save(key, state);
		
	}

	@Override
	public Iterator<ClusterState> clusterIterator() {
		return dataStore.clusterIterator();
	}

	@Override
	public Iterator<String> keyIterator() {
		return dataStore.keyIterator();
	}

	@Override
	public int size() {
		return dataStore.size();
	}

}
