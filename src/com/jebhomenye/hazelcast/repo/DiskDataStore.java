package com.jebhomenye.hazelcast.repo;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jebhomenye.hazelcast.model.ClusterState;

public class DiskDataStore implements DataStore {
	
	private LoadingCache<String, ClusterState> cache;
	private ConcurrentNavigableMap<String, ClusterState> diskMap;
	
	public DiskDataStore(){
		initialize();
	}

	private void initialize() {
		DB db = DBMaker.newFileDB(new File("cluster.db"))
					.closeOnJvmShutdown()
					.make();
		diskMap = db.getTreeMap("clusterMap");
		
		cache = CacheBuilder.newBuilder()
				 .maximumSize(1000L)
				 .expireAfterWrite(2, TimeUnit.MINUTES)
				 .build(new CacheLoader<String, ClusterState>(){

					@Override
					public ClusterState load(String key) throws Exception {
						
						return diskMap.get(key);
					}
					 
				 });
	}

	@Override
	public ClusterState get(String key) {
		try {
			return cache.get(key);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void save(String key, ClusterState state) {
		cache.put(key, state);
		diskMap.put(key, state);
	}

	@Override
	public Iterator<ClusterState> clusterIterator() {
		return null;
	}

	@Override
	public Iterator<String> keyIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
